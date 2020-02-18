package ReliableUDP;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

import Models.Enums;
import Models.UDPMessage;

public class Adding {

	public int addingTwoNymber(int x, int y) {
		return x + y;
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws SocketException 
	 */
	public static void main(String[] args) throws InterruptedException, SocketException {
//		// String msg = "Hello ; ";
//
		DatagramSocket socket1 = new DatagramSocket();
		System.out.println("bbb="+socket1.getLocalPort());
//		// Fornt End Send Request
		
		UDPMessage udpMsg = new UDPMessage(Enums.UDPSender.FrontEnd, -1, Enums.FlightCities.Montreal,
				Enums.Operations.bookFlight, Enums.UDPMessageType.Request);
		HashMap<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("firstName", "abc");
		parameterMap.put("lastName", "asdasd");
		parameterMap.put("address", "asdasd");
		parameterMap.put("phone", "1234567890");
		parameterMap.put("destination", Enums.FlightCities.Montreal.toString());
		parameterMap.put("date", "2016/12/1");
		parameterMap.put("classFlight", Enums.Class.Economy.toString());
		udpMsg.setParamters(parameterMap);
		udpMsg.setFrontEndPort(socket1.getLocalPort());

		
		//Sender s = new Sender("127.0.0.2", 10091, 2000, false, new DatagramSocket());
		Sender s = new Sender("127.0.0.1", 10091, false, socket1);
		Boolean status = s.send(udpMsg);
		System.err.println("isTransferComplete : " + s.isTransferComplete);
		System.err.println("status : " + status);

		
		//DatagramSocket scoket = new DatagramSocket(10091);
		boolean isrun = true;
	//	while (isrun) {
			// Reciever r = new Reciever(10091,2000);

			Reciever r = new Reciever(socket1);
			udpMsg = r.getData();
			// Thread.sleep(1000);
			// System.err.println("isTransferComplete : "+
			// s.isTransferComplete);
			System.out.println("the data received is : ");
			System.out.println("sender: " + r.getData().getSender());
			System.out.println("front end ip: " + r.getData().getFrontEndIP());
			System.out.println("front end port: " + r.getData().getFrontEndPort());
			System.out.println("server name: " + r.getData().getServerName());
			System.out.println("manager id: " + r.getData().getManagerID());
			System.out.println("sequence number: " + r.getData().getSequencerNumber());
			System.out.println("operation: " + r.getData().getOpernation());
			System.out.println("reply message: " + r.getData().getReplyMsg());
			System.out.println("message parameters: " + r.getData().getParamters());
			
		
			
		//	 socket1.close();
	//	}
		socket1.close();
		
		
//		DatagramSocket socket = s.getOutGoingSocket();
//
//		// do FE staff here etc etc
//		System.out.println("Get Port # used : " + socket.getLocalPort());
//
//		socket.close();

		// System.err.println("isTransferComplete : "+ s.isTransferComplete);
		// Reciever r = new Reciever(1000,2000);

		// Thread.sleep(1000);

		// System.out.print("data recieved is : "+ r.getData());
		
//		UDPMessage udpMsg = new UDPMessage(Enums.UDPSender.FrontEnd, -1, Enums.getFlightCitiesFromString("Montreal"),
//				Enums.Operations.bookFlight, Enums.UDPMessageType.Request);
//		HashMap<String, String> parameterMap = new HashMap<String, String>();
//		parameterMap.put("firstName", "Ulan");
//		parameterMap.put("lastName", "Baitassov");
//		parameterMap.put("address", "Verdun");
//		parameterMap.put("phone", "5145606164");
//		parameterMap.put("destination", "Washington");
//		parameterMap.put("date", "02/12/2016");
//		parameterMap.put("classFlight", "economy");
//		udpMsg.setParamters(parameterMap);		
//		udpMsg.setManagerID("-1");		
//		udpMsg.setFrontEndPort(-1);	
//		
//		FEBookingImpl bookingObject = null;
//		bookingObject = new FEBookingImpl();
//		
//		String result = bookingObject.send(udpMsg).trim();

	}

}
