package Replica;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.logging.Logger;

import DFRS.BookingImp;
import DFRS.Server;
import Models.Enums;
import Models.UDPMessage;
import ReliableUDP.Reciever;
import ReliableUDP.Sender;
import ReplicaManager.RMHeartBeat;
import ReplicaManager.ReplicaManagerListner;

import StaticContent.StaticContent;
import Utilities.CLogger;

public class ReplicaMain {
	private static CLogger clogger;
	private final static Logger LOGGER = Logger.getLogger(ReplicaMain.class.getName());
	public static HashMap<String, BookingImp> servers = new HashMap<String, BookingImp>();

	/*
	 * Reason of this class: 1) Create MTL, WSL, NDH server objects using ORB.
	 * 2) Listen to all incoming messages from sequencer and respective RM. 3)
	 * Parse the incoming UDP request and call it in the actual server object.
	 * 4) Replica always reply to FrontEnd port that is in UDP Object. 5) Any
	 * request that comes from Sequencer should be saved in a file as backup so
	 * that it can be restored when the replica is started again.
	 */

	public ReplicaMain(boolean restoreBackup) {
		String msg = "";

		String[] corbaArgs = new String[4];
		corbaArgs[0] = "-ORBInitialPort";
		corbaArgs[1] = "1050";
		corbaArgs[2] = "-ORBInitialHost";
		corbaArgs[3] = "localhost";

		try {
			// initialize logger
			clogger = new CLogger(LOGGER, "Replica/Replica.log");
			msg = "Replica is UP!";
			clogger.log(msg);
			System.out.println(msg);

			// Start UDP Server
			ReplicaListner server = new ReplicaListner(clogger, StaticContent.REPLICA_ULAN_lISTENING_PORT,
					Enums.UDPSender.ReplicaUlan);
			server.start();

			createServerObjects(corbaArgs, restoreBackup);

			
			
			// server.executeTestMessage();
			// server.join();

		} catch (Exception e) {
			System.out.println("Sequencer Exception: " + e.getMessage());
		}
	}
	
	public void shutDownReplica()
	{
		for(Entry<String, BookingImp> entry : servers.entrySet())
		{
			BookingImp temp = entry.getValue();
			temp.shutDownServer();
		}
	}

	private void createServerObjects(String[] orbArgs, boolean restoreBackup) {
		Server montreal = new Server();
		Server washington = new Server();
		Server newDelhi = new Server();

		montreal.startServer(Enums.FlightCities.Montreal.toString(), "", orbArgs);
		washington.startServer(Enums.FlightCities.Washington.toString(), "", orbArgs);
		newDelhi.startServer(Enums.FlightCities.NewDelhi.toString(), "", orbArgs);
		
		servers.put(Enums.FlightCities.Montreal.toString(), montreal.getBookingObj());
		servers.put(Enums.FlightCities.Washington.toString(), washington.getBookingObj());
		servers.put(Enums.FlightCities.NewDelhi.toString(), newDelhi.getBookingObj());

		if (restoreBackup) {
			restoreServerTransactions();
		}
	}

	private void restoreServerTransactions() {
		
		File folder = new File(StaticContent.RM_TRANSACTION_LOGS_PATH); // Get the input file folder.
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					UDPMessage temp;
					try {
						temp = deserialize(listOfFiles[i].getName());
						restoreLogFile(temp);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch(ClassNotFoundException e){
						
					}
					
				}
		}
		
	}

	public static UDPMessage deserialize(String fName) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(StaticContent.RM_TRANSACTION_LOGS_PATH+fName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		ObjectInputStream ois = new ObjectInputStream(bis);
		UDPMessage obj = (UDPMessage) ois.readObject();
		ois.close();
		return obj;
	}
	
	private void restoreLogFile(UDPMessage udpMessage)
	{
	
			String msg = Enums.UDPSender.ReplicaUlan + " Restore in process!";
			UDPMessage replyMessage = null;
			
				switch (udpMessage.getSender()) {
				case Sequencer:
					// Perform Operations.
					msg = "Executing Opernation : " + udpMessage.getOpernation() + ", on Server :"
							+ udpMessage.getServerName();
					System.out.println(msg);
					clogger.log(msg);
					
					replyMessage = new UDPMessage(Enums.UDPSender.ReplicaUlan, udpMessage.getSequencerNumber(),
							udpMessage.getServerName(), udpMessage.getOpernation(), Enums.UDPMessageType.Reply);

					BookingImp obj = ReplicaMain.servers.get(udpMessage.getServerName().toString());
					String res = "";
					switch (udpMessage.getOpernation()) {

					case bookFlight:
						
						res = obj.bookFlight(udpMessage.getParamters().get("firstName"),
								udpMessage.getParamters().get("lastName"), udpMessage.getParamters().get("address"),
								udpMessage.getParamters().get("phone"), udpMessage.getParamters().get("destination"),
								udpMessage.getParamters().get("date"), udpMessage.getParamters().get("classFlight"));

						replyMessage.setReplyMsg(res);

						break;

					case getBookedFlightCount:
						
						res = obj.getBookedFlightCount(udpMessage.getParamters().get("recordType"));

						replyMessage.setReplyMsg(res);

						break;

					case editFlightRecord:
					
						res = obj.editFlightRecord(udpMessage.getParamters().get("recordID"),
								udpMessage.getParamters().get("fieldName"), udpMessage.getParamters().get("newValue"));
						replyMessage.setReplyMsg(res);
						break;

					case transferReservation:
						
						res = obj.transferReservation(udpMessage.getParamters().get("passengerID"),
								udpMessage.getParamters().get("currentCity"),
								udpMessage.getParamters().get("otherCity"));
						replyMessage.setReplyMsg(res);
						break;

					}
					break;

				case RMUlan:
				case RMSajjad:
				case RMUmer:
				case RMFeras:
					
					
					msg = udpMessage.getSender().toString() + " Server contacted for HeartBeat.";
					System.out.println(msg);
					clogger.log(msg);
					
					break;

				default:
					msg = "Unknow Sender : " + udpMessage.getSender();
					System.out.println(msg);
					clogger.log(msg);
					
					break;
				}
				ReplicaListner.sequencerNumber = udpMessage.getSequencerNumber();


	}

	public static void main(String[] args) {
		ReplicaMain main = new ReplicaMain(false);

	}
}
