package ReliableUDP;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.zip.CRC32;

import Models.UDPMessage;
import Utilities.Serializer;

// The following implementation uses the Go-Back-N protocol
public class SenderOld {
	static int data_size = 4000; // (checksum:8, seqNum:4, data<=988) Bytes :
								// 1000 Bytes total
	static int win_size = 4000;
	static int timeoutVal = 300; // 300ms until timeout

	int base; // base sequence number of window
	int nextSeqNum; // next sequence number in window
	Vector<byte[]> packetsList; // list of generated packets
	Timer timer; // for timeouts
	Semaphore s; // guard CS for base, nextSeqNum
	boolean isTransferComplete; // if receiver has completely received the file
	UDPMessage data;
	String destinationAddress;

	// to start or stop the timer
	public void setTimer(boolean isNewTimer) {
		if (timer != null)
			timer.cancel();
		if (isNewTimer) {
			timer = new Timer();
			timer.schedule(new Timeout(), timeoutVal);
		}
	}

	// CLASS OutThread
	public class OutThread extends Thread {
		private DatagramSocket sk_out;
		private int dst_port;
		private InetAddress dst_addr;
		private int recv_port;
		private boolean isCloseOutGoingSocket;

		// OutThread constructor
		public OutThread(DatagramSocket sk_out, int dst_port, int recv_port,
				boolean isCloseOutGoingSocket) {
			this.sk_out = sk_out;
			this.dst_port = dst_port;
			this.recv_port = recv_port;
			this.isCloseOutGoingSocket = isCloseOutGoingSocket;
		}

		// constructs the packet prepended with header information
		public byte[] generatePacket(int seqNum, byte[] dataBytes) {
			byte[] seqNumBytes = ByteBuffer.allocate(4).putInt(seqNum).array(); // Seq
																				// num
																				// (4
																				// bytes)

			// generate checksum
			CRC32 checksum = new CRC32();
			checksum.update(seqNumBytes);
			checksum.update(dataBytes);
			byte[] checksumBytes = ByteBuffer.allocate(8)
					.putLong(checksum.getValue()).array(); // checksum (8 bytes)

			// generate packet
			ByteBuffer pktBuf = ByteBuffer.allocate(8 + 4 + dataBytes.length);
			pktBuf.put(checksumBytes);
			pktBuf.put(seqNumBytes);
			pktBuf.put(dataBytes);
			return pktBuf.array();
		}

		// sending process (updates nextSeqNum)
		public void run() {
			try {
				dst_addr = InetAddress.getByName(destinationAddress); // resolve
																		// dst_addr

				// ByteArrayInputStream fis = new
				// ByteArrayInputStream(data.getBytes());
				ByteArrayInputStream fis = new ByteArrayInputStream(
						Serializer.serialize(data));

				try {
					// while there are still packets yet to be received by
					// receiver
					while (!isTransferComplete) {
						// send packets if window is not yet full
						if (nextSeqNum < base + win_size) {

							s.acquire();
							/***** enter CS *****/
							if (base == nextSeqNum)
								setTimer(true); // if first packet of window,
												// start timer

							byte[] out_data = new byte[10];
							boolean isFinalSeqNum = false;

							// if packet is in packetsList, retrieve from list
							if (nextSeqNum < packetsList.size()) {
								out_data = packetsList.get(nextSeqNum);
							}
							// else construct packet and add to list
							else {
								// if first packet, special handling: prepend
								// file information
								if (nextSeqNum == 0) {
									byte[] dataBuffer = new byte[data_size];
									int dataLength = fis.read(dataBuffer, 0,
											data_size);
									byte[] dataBytes = copyOfRange(dataBuffer,
											0, dataLength);
									ByteBuffer BB = ByteBuffer
											.allocate(dataBytes.length);
									BB.put(dataBytes); // file data slice
									out_data = generatePacket(nextSeqNum,
											BB.array());
								}
								// else if subsequent packets
								else {
									byte[] dataBuffer = new byte[data_size];
									int dataLength = fis.read(dataBuffer, 0,
											data_size);
									// if no more data to be read, send empty
									// data. i.e. finalSeqNum
									if (dataLength == -1) {
										isFinalSeqNum = true;
										out_data = generatePacket(nextSeqNum,
												new byte[0]);
									}
									// else if valid data
									else {
										byte[] dataBytes = copyOfRange(
												dataBuffer, 0, dataLength);
										out_data = generatePacket(nextSeqNum,
												dataBytes);
									}
								}
								packetsList.add(out_data); // add to packetsList
							}

							// send the packet
							sk_out.send(new DatagramPacket(out_data,
									out_data.length, dst_addr, dst_port));
							System.out.println("Sender: Sent seqNum "
									+ nextSeqNum);

							// update nextSeqNum if currently not at FinalSeqNum
							if (!isFinalSeqNum)
								nextSeqNum++;
							s.release();
							/***** leave CS *****/
						}
						sleep(5);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					setTimer(false); // close timer
					if (isCloseOutGoingSocket) {
				//		sk_out.close(); // close outgoing socket
					} else {

					}
					fis.close(); // close FileInputStream
			//		System.out.println("Sender: sk_out closed!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}// END CLASS OutThread

	// CLASS InThread
	public class InThread extends Thread {
		private DatagramSocket sk_in;

		// InThread constructor
		public InThread(DatagramSocket sk_in) {
			this.sk_in = sk_in;
		}

		// returns -1 if corrupted, else return Ack number
		int decodePacket(byte[] pkt) {
			byte[] received_checksumBytes = copyOfRange(pkt, 0, 8);
			byte[] ackNumBytes = copyOfRange(pkt, 8, 12);
			CRC32 checksum = new CRC32();
			checksum.update(ackNumBytes);
			byte[] calculated_checksumBytes = ByteBuffer.allocate(8)
					.putLong(checksum.getValue()).array();// checksum (8 bytes)
			if (Arrays.equals(received_checksumBytes, calculated_checksumBytes))
				return ByteBuffer.wrap(ackNumBytes).getInt();
			else
				return -1;
		}

		// receiving process (updates base)
		public void run() {
			try {
				byte[] in_data = new byte[12]; // ack packet with no data
				DatagramPacket in_pkt = new DatagramPacket(in_data,
						in_data.length);
				try {
					// while there are still packets yet to be received by
					// receiver
					while (!isTransferComplete) {

						sk_in.receive(in_pkt);
						int ackNum = decodePacket(in_data);
						System.out.println("Sender: Received Ack " + ackNum);

						// if ack is not corrupted
						if (ackNum != -1) {
							// if duplicate ack
							if (base == ackNum + 1) {
								s.acquire();
								/***** enter CS *****/
								setTimer(false); // off timer
								nextSeqNum = base; // resets nextSeqNum
								s.release();
								/***** leave CS *****/
							}
							// else if teardown ack
							else if (ackNum == -2)
								isTransferComplete = true;
							// else normal ack
							else {
								base = ackNum++; // update base number
								s.acquire();
								/***** enter CS *****/
								if (base == nextSeqNum)
									setTimer(false); // if no more
														// unacknowledged
														// packets in pipe, off
														// timer
								else
									setTimer(true); // else packet acknowledged,
													// restart timer
								s.release();
								/***** leave CS *****/
							}
						}
						// else if ack corrupted, do nothing
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					sk_in.close();
					System.out.println("Sender: sk_in closed!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}// END CLASS InThread

	// Timeout task
	public class Timeout extends TimerTask {
		public void run() {
			try {
				s.acquire();
				/***** enter CS *****/
				System.out.println("Sender: Timeout!");
				nextSeqNum = base; // resets nextSeqNum
				s.release();
				/***** leave CS *****/
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}// END CLASS Timeout

	int sk1_dst_port;
	int sk4_dst_port;
	boolean isCloseOutGoingSocket = true;
	DatagramSocket sk1;

	// sender constructor
	public SenderOld(String destinationAddress, int sk1_dst_port,
			int sk4_dst_port, boolean isCloseOutGoingSocket, DatagramSocket p) {
		base = 0;
		nextSeqNum = 0;
		this.packetsList = new Vector<byte[]>(win_size);
		this.sk1_dst_port = sk1_dst_port;
		this.sk4_dst_port = sk4_dst_port;
		isTransferComplete = false;
		s = new Semaphore(1);
		System.out.println("Sender: sk1_dst_port=" + sk1_dst_port
				+ ", sk4_dst_port=" + sk4_dst_port);
		// this.data = data;
		this.destinationAddress = destinationAddress;
		this.isCloseOutGoingSocket = isCloseOutGoingSocket;
		this.sk1 = p;
		// outgoing channel

	}// END Sender constructor

	public boolean send(UDPMessage data) {
		this.data = data;
		DatagramSocket sk4;
		try {
			// create sockets
			sk4 = new DatagramSocket(sk4_dst_port); // incoming channel
			// create threads to process data
			InThread th_in = new InThread(sk4);
			OutThread th_out = new OutThread(this.sk1, sk1_dst_port,
					sk4_dst_port, isCloseOutGoingSocket);
			th_in.start();
			th_out.start();
			th_in.join();
			th_out.join();

			return isTransferComplete;

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
			return false;
		}

	}

	// same as Arrays.copyOfRange in 1.6
	public byte[] copyOfRange(byte[] srcArr, int start, int end) {
		int length = (end > srcArr.length) ? srcArr.length - start : end
				- start;
		byte[] destArr = new byte[length];
		System.arraycopy(srcArr, start, destArr, 0, length);
		return destArr;
	}

	public DatagramSocket getOutGoingSocket() {
		return sk1 == null ? null : sk1;
	}

}