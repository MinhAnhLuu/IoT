import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class client {

	public static void main(String[] args) throws UnknownHostException, IOException {
			
		while (true) {
			try {
				//Tao socket
				DatagramSocket client = new DatagramSocket(7000);
				
				//Nhap du lieu tu ban phim
				Scanner nhap = new Scanner(System.in);
				System.out.println("Me: ");
				String thangnam = nhap.nextLine();
								
				//Tao byte chua du lieu: Data - IP - Port
				byte[] sendData = new byte[1024];
				byte[] receiveData = new byte[1024];
				sendData = thangnam.getBytes();
				InetAddress ipserver = InetAddress.getByName("localhost");
				int portclient = 5000;
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipserver, portclient);
				
				//Gui pacet len server
				client.send(sendPacket);
				
				//Nhan Packet
//				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//				client.receive(receivePacket);
//				String ngay = new String(receivePacket.getData());
//				System.out.println(ngay);
				
				//Nhan du lieu tu TopicSubscriber qua TCP
				try {
					//Ket noi server TCP
					Socket s = new Socket("localhost",1234);
					InputStream is = s.getInputStream(); //Nhan du lieu
										
					//Nhan tu Server TCP
					int ch2 = is.read(receiveData);
					String chuoi = new String(receiveData);
					System.out.println("Server: "+ chuoi);
					//s.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}