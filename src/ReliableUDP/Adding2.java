package ReliableUDP;

import java.net.DatagramSocket;
import java.net.SocketException;

import Models.UDPMessage;

public class Adding2 {

	public int addingTwoNymber(int x, int y) {
		return x + y;
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws SocketException
	 */
	public static void main(String[] args) throws InterruptedException, SocketException {
		DatagramSocket socket = new DatagramSocket(10091);
		boolean isrun = true;
	//	while (isrun) {
			// Reciever r = new Reciever(10091,2000);
			System.out.println("asd="+socket.getLocalPort());
			Reciever r = new Reciever(socket);
			UDPMessage udpMsg = r.getData();
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
			
			Sender s = new Sender("127.0.0.1", r.getData().getFrontEndPort(), false, socket);
			Boolean status = s.send(udpMsg);
			
	//		System.exit(0);
			
	//	}
		socket.close();

	}

}
