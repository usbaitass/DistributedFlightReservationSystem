package ReliableUDP;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.CRC32;

import Models.UDPMessage;
import StaticContent.StaticContent;
import Utilities.Serializer;

public class Reciever {
	static int pkt_size = 1000;
	UDPMessage data;
	DatagramSocket socket;

	/**
	 * @return the socket
	 */
	public DatagramSocket getSocket() {
		return socket;
	}

//	public void closeSocket() {
//
//		if (socket != null && !socket.isClosed())
//			socket.close();
//	}

	// Receiver constructor
	public Reciever(DatagramSocket socket/* , int sk3_dst_port */) {
		// DatagramSocket socket/*, sk3*/;
		// System.out.println("Receiver: sk2_dst_port=" + sk2_dst_port + ", " +
		// "sk3_dst_port=" + sk3_dst_port + ".");

		int prevSeqNum = -1; // previous sequence number received in-order
		int nextSeqNum = 0; // next expected sequence number
		boolean isTransferComplete = false; // (flag) if transfer is complete

		// socket = new DatagramSocket(sk2_dst_port); // incoming channel
		// sk3 = new DatagramSocket(); // outgoing channel
		System.out.println("Receiver: Listening");
		try {
			byte[] in_data = new byte[pkt_size]; // message data in packet
			DatagramPacket in_pkt = new DatagramPacket(in_data, in_data.length); // incoming
																					// packet
			// InetAddress dst_addr = InetAddress.getByName("127.0.0.1");
			// InetAddress dst_addr = InetAddress.getByName(ip);

			ByteArrayOutputStream fos = null;

			// listen on sk2_dst_port
			while (!isTransferComplete) {
				// receive packet
				socket.receive(in_pkt);

				byte[] received_checksum = copyOfRange(in_data, 0, 8);
				CRC32 checksum = new CRC32();
				checksum.update(copyOfRange(in_data, 8, in_pkt.getLength()));
				byte[] calculated_checksum = ByteBuffer.allocate(8).putLong(checksum.getValue()).array();

				// if packet is not corrupted
				if (Arrays.equals(received_checksum, calculated_checksum)) {
					int seqNum = ByteBuffer.wrap(copyOfRange(in_data, 8, 12)).getInt();
		//			System.out.println("Receiver: Received sequence number: " + seqNum);

					// if packet received in order
					if (seqNum == nextSeqNum) {
						// if final packet (no data), send teardown ack
						if (in_pkt.getLength() == 12) {
							byte[] ackPkt = generatePacket(-2); // construct
																// teardown
																// packet (ack
																// -2)
							// send 20 acks in case last ack is not received by
							// Sender (assures Sender teardown)
							for (int i = 0; i < 20; i++)
								socket.send(new DatagramPacket(ackPkt, ackPkt.length, in_pkt.getAddress(),
										in_pkt.getPort()));
							isTransferComplete = true; // set flag to true
							System.out.println("Receiver: All packets received! File Created!");
							continue; // end listener
						}
						// else send ack
						else {
							byte[] ackPkt = generatePacket(seqNum);
							socket.send(
									new DatagramPacket(ackPkt, ackPkt.length, in_pkt.getAddress(), in_pkt.getPort()));
							System.out.println("Receiver: Sent Ack " + seqNum);
						}

						// if first packet of transfer
						if (seqNum == 0 && prevSeqNum == -1) {

							// init fos
							fos = new ByteArrayOutputStream();

							// write initial data to fos
							fos.write(in_data, 12, in_pkt.getLength() - 12);

							// write initial data to fos
						}

						// else if not first packet write to FileOutputStream
						else
							fos.write(in_data, 12, in_pkt.getLength() - 12);

						nextSeqNum++; // update nextSeqNum
						prevSeqNum = seqNum; // update prevSeqNum
					}

					// if out of order packet received, send duplicate ack
					else {
						byte[] ackPkt = generatePacket(prevSeqNum);
						socket.send(new DatagramPacket(ackPkt, ackPkt.length, in_pkt.getAddress(), in_pkt.getPort()));
				//		System.out.println("Receiver: Sent duplicate Ack " + prevSeqNum);
					}
				}

				// else packet is corrupted
				else {
					System.out.println("Receiver: Corrupt packet dropped");
					byte[] ackPkt = generatePacket(prevSeqNum);
					socket.send(new DatagramPacket(ackPkt, ackPkt.length, in_pkt.getAddress(), in_pkt.getPort()));
					System.out.println("Receiver: Sent duplicate Ack " + prevSeqNum);
				}
			}
			if (fos != null) {
				fos.close();

				byte[] message = Arrays.copyOf(fos.toByteArray(), fos.size());
				// byte[] message = Arrays.copyOf(receivePacket.getData(),
				// receivePacket.getLength());
				// receiveData = new
				// byte[StaticContent.UDP_REQUEST_BUFFER_SIZE];

				// Deserialize Data to udpMessage Object.
				// UDPMessage udpMessageReceived =
				// Serializer.deserialize(message);

				data = Serializer.deserialize(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			// socket.close();
			// sk3.close();
	//		System.out.println("Receiver: sk2 closed!");
	//		System.out.println("Receiver: sk3 closed!");
		}
	}// END constructor

	// generate Ack packet
	public byte[] generatePacket(int ackNum) {
		byte[] ackNumBytes = ByteBuffer.allocate(4).putInt(ackNum).array();
		// calculate checksum
		CRC32 checksum = new CRC32();
		checksum.update(ackNumBytes);
		// construct Ack packet
		ByteBuffer pktBuf = ByteBuffer.allocate(12);
		pktBuf.put(ByteBuffer.allocate(8).putLong(checksum.getValue()).array());
		pktBuf.put(ackNumBytes);
		return pktBuf.array();
	}

	// same as Arrays.copyOfRange in 1.6
	public byte[] copyOfRange(byte[] srcArr, int start, int end) {
		int length = (end > srcArr.length) ? srcArr.length - start : end - start;
		byte[] destArr = new byte[length];
		System.arraycopy(srcArr, start, destArr, 0, length);
		return destArr;
	}

	public UDPMessage getData() {
		return data;
	}

}