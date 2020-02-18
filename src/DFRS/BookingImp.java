package DFRS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;

import org.omg.CORBA.ORB;

public class BookingImp extends BookingPOA {
	
	private ORB orb;
	private BookingImpService bookingService;
	
	
	/**
	 * Constructor
	 * @param new_bis reference to booking service object
	 */
	public BookingImp(BookingImpService new_bis){
		bookingService = new_bis;
	}

	/**
	 * This method sets the orb
	 * @param new_orb
	 */
	public void setORB(ORB new_orb){
		orb = new_orb;
	}
	
	/**
	 * This method shut downs the orb
	 */
	public void shutDownServer(){
		orb.shutdown(false);
	}
	
	@Override
	public String bookFlight(String firstName, String lastName, String address, String phone, String destination, String date, String classFlight) {
		// TODO Auto-generated method stub
		//find value by key in hash map
		
		List<Flight> temp = bookingService.getHashMapFlight().get(destination + date);
		if (temp != null) {
			synchronized(temp){
			for (int i = 0; i < temp.size(); i++) {
				if (classFlight.equalsIgnoreCase("first")) {
					if (temp.get(i).getFirstClassOccupied() < temp.get(i).getFirstClass()) {
						long id = bookingService.addPassengerRecord(firstName, lastName, address, phone, destination, classFlight, date);
						temp.get(i).insertRecord(0, Long.toString(id));
						return "true:"+Long.toString(id);
					}
				} else if (classFlight.equalsIgnoreCase("business")) {
					if (temp.get(i).getBusinessClassOccupied() < temp.get(i).getBusinessClass()) {
						long id = bookingService.addPassengerRecord(firstName, lastName, address, phone, destination, classFlight, date);
						temp.get(i).insertRecord(1, Long.toString(id));
						return "true:"+Long.toString(id);
					}
				} else if (classFlight.equalsIgnoreCase("economy")) {
					if (temp.get(i).getEconomyClassOccupied() < temp.get(i).getEconomyClass()) {
						long id = bookingService.addPassengerRecord(firstName, lastName, address, phone, destination, classFlight, date);
						temp.get(i).insertRecord(2, Long.toString(id));
						return "true:"+Long.toString(id);
					}
				}
			}
			bookingService.getLogger().info("BOOKING REQUEST FAILED: NO SPACE! client: "+firstName+" "+lastName+" desination: "+destination+" date:"+date+" flightclass: "+classFlight);
			return "false:1";//"nospace";
			}
		} else {
			bookingService.getLogger().info("BOOKING REQUEST FAILED: NO FLIGHT ON THIS DATE! client: "+firstName+" "+lastName+" desination: "+destination+" date:"+date+" flightclass: "+classFlight);
			return "false:0";//"noflight";
		}
		
		
	}

	@Override
	public String getBookedFlightCount(String recordType) {
		System.err.println("HERERERE: "+ recordType);
		// TODO Auto-generated method stub
		try {
			String[] arr2 = recordType.split(":");
			//arr2[0] server
			String managerID = arr2[0];
			recordType = arr2[1];
			DatagramSocket socketUDPC = null;
			String otherserverName = "";
			int otherserverPort = 0;
			String sum = "";
			bookingService.getLogger().info("REQUESTED BOOOKED FLIGHT COUNT by manager: "+managerID);
			//Open common repository to fetch other servers UDP ports and IP addresses
			File file = new File("common.repository");
			if (file.exists()) {
				Scanner input = new Scanner(file);
				String line = "";
				String[] arr;
				while (input.hasNextLine()) {
					line = input.nextLine();
					arr = line.split(";");
					//System.err.println("0 = "+arr[0]);
					//System.err.println("0 = "+arr[1]);
					otherserverName = arr[0];
					otherserverPort = Integer.parseInt(arr[1]);
					if(!otherserverName.equalsIgnoreCase(bookingService.getNameServer())){
						//send UDP request message to other server
						socketUDPC = new DatagramSocket();
						InetAddress aHost = InetAddress.getByName("127.0.0.1"); //localhost
						String message = "getcount;"+bookingService.getNameServer()+";"+recordType;
						bookingService.getLogger().info("SENT UDP request to SERVER: "+otherserverName+" on port: "+otherserverPort+" message: "+message);
						//System.out.println("SENT UDP request to SERVER: "+otherserverName+" on port: "+otherserverPort+" message: "+message);
						DatagramPacket requestPacket = new DatagramPacket(message.getBytes(), message.length(), aHost, otherserverPort);
						socketUDPC.send(requestPacket);
						//receive UDP reply message from other server containing number of passenger on the flights
						byte[] buffer = new byte[1000];
						DatagramPacket replyPacket = new DatagramPacket(buffer, buffer.length);	
						socketUDPC.receive(replyPacket);
						//System.out.println("Reply: " + new String(replyPacket.getData()));	
						bookingService.getLogger().info("RECEIVED UDP reply from SERVER "+otherserverName+" message: "+new String(replyPacket.getData()));
						//System.out.println("RECEIVED UDP reply from SERVER "+otherserverName+" message: "+new String(replyPacket.getData()));
						sum+=new String(replyPacket.getData());
						socketUDPC.close();
					}
				}
				input.close();
			}
		//combine all UDP reply messages in one message and send back result to manager
		sum = bookingService.getBookedCountFlight2(recordType)+sum;
		bookingService.getLogger().info("REQUESTED BOOKED FLIGHT COUNT Result: "+ sum +" sent to manager: "+managerID);
		return sum;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";	
	}

	@Override
	public String editFlightRecord(String recordID, String fieldName, String newValue) {
		// TODO Auto-generated method stub
		
		
		char operation = fieldName.charAt(0);
//		String operation = fieldName;
		String[] arr1 = fieldName.split(":");
		String[] arr2 = newValue.split(":");
//		String[] arr2 = newValue.split(":");
//		String newFclass = arr2[0];
//		String newBclass = arr2[1];
//		String newEclass = arr2[2];
//		String newDate = arr2[3];
//		String newTime = arr2[4];
//		String newDestination =arr2[5]; 
//		
		if(operation=='0'){
//		if(operation.equalsIgnoreCase("createFlight")){	
			if(bookingService.createNewFlight(recordID, arr1[1], arr1[2], arr1[3])){
//			if(bookingService.createNewFlight(recordID, newDestination, newDate, newTime)){
				return "true:"+bookingService.countFlight++;
			}else{
				bookingService.getLogger().info("FAILED TO CREATE FLIGHT from "+bookingService.getNameServer()+" to "+ arr1[1]+" on " + arr1[2] + " at " + arr1[3]+" by manager: "+recordID);
//				bookingService.getLogger().info("FAILED TO CREATE FLIGHT from "+bookingService.getNameServer()+" to "+ arr2[1]+" on " + arr2[2] + " at " + arr2[3]+" by manager: "+recordID);
				return "false";
			}
		}else if(operation=='1'){
//		}else if(operation.equalsIgnoreCase("editFlight")){	
			return bookingService.editFlight(recordID, arr1[1], arr1[2], arr1[3], arr2[0], arr2[1], arr2[2], arr2[3], arr2[4]);
//			return Integer.toString(bookingService.editFlight(recordID, arr2[1], arr2[2], arr2[3], arr2[0], arr2[1], arr2[2], arr2[3], arr2[4]));
		}else if(operation=='2'){
//		}else if(operation.equalsIgnoreCase("deleteFlight")){
			return bookingService.deleteFlight(recordID, arr1[1], arr1[2], arr1[3]);
//			return Integer.toString(bookingService.deleteFlight(recordID, arr2[1], arr2[2], arr2[3]));
		}else if(operation=='3'){
//		}else if(operation.equalsIgnoreCase("showAllFlights")){
			return bookingService.showAllFlight();
		}
		return "false";	
	}

	@Override
	public String transferReservation(String passengerID, String currentCity, String otherCity) {
		// TODO Auto-generated method stub
		String[] arr3 = passengerID.split(":");
		//arr3[0] server
		//arr3[1] manager id
		passengerID = arr3[2];
		String message = "";
		Passenger p = bookingService.findPassengerInPassengerHMap(passengerID);
	//	String[] pid = bookingService.findPassengerInPassengerHMap(passengerID).split(";");
	//	Passenger p;
	//	if(!pid[0].equalsIgnoreCase("-1")){
	//		p = bookingService.getHashMap().get(pid[0].charAt(0)).get(Integer.parseInt(pid[1]));
		if(p!=null){
			
			synchronized(bookingService.getHashMap().get(p.getLastName().charAt(0))){//asdasdad
				
			if(!otherCity.equalsIgnoreCase(p.getDestination())){
			//System.out.println(p.getFirstName()+" "+p.getLastName());
				try {
					File file = new File("common.repository");
					if (file.exists()) {
						Scanner input = new Scanner(file);
						while (input.hasNextLine()) {
							String line = input.nextLine();
							String[] arr = line.split(";");
							String otherserverName = arr[0];
							int otherserverPort = Integer.parseInt(arr[1]);
							if(otherserverName.equalsIgnoreCase(otherCity)){
								DatagramSocket socketUDPC = new DatagramSocket();
								InetAddress aHost = InetAddress.getByName("127.0.0.1"); //localhost
								message = "book;"+p.getDestination()+";"+p.getDateFlight()+";"+p.getFirstName()+";"+p.getLastName()+";"+p.getClassFlight()+";"+p.getAddress()+";"+p.getPhone();
								DatagramPacket requestPacket = new DatagramPacket(message.getBytes(), message.length(), aHost, otherserverPort);
								socketUDPC.send(requestPacket);
								//receive UDP reply message from other server containing number of passenger on the flights
								byte[] buffer = new byte[1000];
								DatagramPacket replyPacket = new DatagramPacket(buffer, buffer.length);	
								socketUDPC.receive(replyPacket);
								message = new String(replyPacket.getData());
								System.err.println("message1 = "+message);	
								socketUDPC.close();
								socketUDPC = null;
								message = message.trim();
								if(!message.equalsIgnoreCase("nospace") && !message.equalsIgnoreCase("noflight")){
									String strclass = p.getClassFlight();
									int iclass = 0;
									if(strclass.equalsIgnoreCase("first")){
										iclass = 0;
									}else if(strclass.equalsIgnoreCase("business")){
										iclass = 1;
									}else if(strclass.equalsIgnoreCase("economy")){
										iclass = 2;
									}
									if(bookingService.deleteRecordFromFlightHMap(p.getDestination()+p.getDateFlight(), iclass, passengerID)){
										//System.err.println("record was deleted from flight hash map");
										bookingService.getLogger().info("PASSENGER RECORD WAS TRANSFERED TO OTHER FLIGHT: "+p.getFirstName()+" "+p.getLastName()+" record id: "+ p.getRecordID());
										if(bookingService.deletePassenger(passengerID, p.getLastName())){
											//System.err.println("passenger was deleted from passenger hash map");
										}
									}
									return "true";
								}
								return message;
							}
						}
					}
				}catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				return "false";
			}
			}//asdasdasdasdasda
		//}
		}else{
			return "false";
		}
		return "false";
	}

}
