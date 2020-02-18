package Replica;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;

import Models.Enums;
import Models.UDPMessage;
import ReliableUDP.Sender;
import ReplicaManager.ReplicaManagerMain;
import StaticContent.*;
import Utilities.Serializer;

public class TestReplica {

	public static void main(String[] args) {
		try {

			// Booking Flight ...
			UDPMessage udpMsg = new UDPMessage(Enums.UDPSender.Sequencer, 1,
					Enums.getFlightCitiesFromString("Montreal"), Enums.Operations.bookFlight,
					Enums.UDPMessageType.Request);
			HashMap<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put("firstName", "Ulan");
			parameterMap.put("lastName", "Baitassov");
			parameterMap.put("address", "Verdun");
			parameterMap.put("phone", "5145606164");
			parameterMap.put("destination", "Washington");
			parameterMap.put("date", "02/12/2016");
			parameterMap.put("classFlight", "economy");
			udpMsg.setParamters(parameterMap);
			udpMsg.setManagerID("-1");
			udpMsg.setFrontEndPort(400);

			DatagramSocket socket;

			socket = new DatagramSocket();

			System.out.println("try to Send .... ");
			// Sender s = new Sender(destinationIP, destinationPort,
			// acknowledgementPort, false, socket);

			DatagramSocket senderSocket = new DatagramSocket();

			byte[] sendData = Serializer.serialize(udpMsg);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
					InetAddress.getByName(StaticContent.REPLICA_UMER_IP_ADDRESS),
					StaticContent.REPLICA_UMER_lISTENING_PORT);
			senderSocket.send(sendPacket);

			// Clear Send buffer
			sendData = new byte[StaticContent.UDP_REQUEST_BUFFER_SIZE];

			try {
				// Set Time Out, and Wait For Ack
				socket.setSoTimeout(StaticContent.UDP_RECEIVE_TIMEOUT);
				byte[] receiveData = new byte[StaticContent.UDP_REQUEST_BUFFER_SIZE];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				socket.receive(receivePacket);
				byte[] message = Arrays.copyOf(receivePacket.getData(), receivePacket.getLength());

				//
				// Deserialize Data to udpMessage Object.
				UDPMessage udpMessageReceived = Serializer.deserialize(message);
				receiveData = new byte[StaticContent.UDP_REQUEST_BUFFER_SIZE];

				System.out.println("Reply came with status: " + udpMessageReceived.getStatus() + " with operation:"
						+ udpMessageReceived.getOpernation());

			} catch (ClassNotFoundException e) {
				System.out.println("Time Out occor!");
				// resend
				// socket.send(data);
				// continue;
			} catch(SocketTimeoutException e){
				
			}

			senderSocket.close();

			// Sender s = new Sender(StaticContent.REPLICA_UMER_IP_ADDRESS,
			// StaticContent.REPLICA_UMER_lISTENING_PORT, false, socket);
			// if(s.send(udpMsg))
			// {
			// //release Port
			// if(socket != null && !socket.isClosed())
			// socket.close();
			//
			// System.out.println("Successfully Send ..");
			// }
			// else
			// {
			// System.out.println("Fail to Send ..");
			// }
			//
			if (socket != null && !socket.isClosed())
				socket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
