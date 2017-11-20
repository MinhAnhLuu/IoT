import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class server {
	public static void main(String[] args) {
		try {
			//Tao socket
			DatagramSocket server = new DatagramSocket(5000);
									
			while (true) {
				//Tao packet rong
				byte[] sendData = new byte[1024];
				byte[] receiveData = new byte[1024];
				
				System.out.println("Waiting for packet...");
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				
				//Nhan Packet
				server.receive(receivePacket);
				
				//Lay du lieu tu Packet: data - IP - Port
				String thangnam = new String(receivePacket.getData());
				InetAddress ipclient = receivePacket.getAddress();
				int portclient = receivePacket.getPort();
				
				//GUI DU LIEU LEN CLOUD
				System.out.println("TopicPublisher initializing...");

		        String host = "tcp://m14.cloudmqtt.com:19557";
		        String username = "uqgxmipu";
		        String password = "kJKLA8Y62Qmq";

		        if (!host.startsWith("tcp://")) {
		            host = "tcp://" + host;
		        }

		        try {
		           // Create an Mqtt client
		            MqttClient mqttClient = new MqttClient(host, "HelloWorldPub");
		            MqttConnectOptions connOpts = new MqttConnectOptions();

		            connOpts.setCleanSession(true);
		            connOpts.setUserName(username);
		            connOpts.setPassword(password.toCharArray());

		    
		            // Connect the client
		            System.out.println("Connecting to MQTT Cloud at " + host);
		            mqttClient.connect(connOpts);
		            System.out.println("Connected \n");

		            // Create a Mqtt message
		            String content = thangnam;//"Hello world from MQTT!";
		            MqttMessage message = new MqttMessage(content.getBytes());

		            // Set the QoS on the Messages - 

		            // Here we are using QoS of 0 (equivalent to Direct Messaging in Solace)
		            message.setQos(0);

		            System.out.println("Publishing message: \n" + content);
		           
		            // Publish the message
		            mqttClient.publish("T/GettingStarted/pubsub", message);
		  

		            // Disconnect the client
		            mqttClient.disconnect();

		       
		            //System.out.println("Message published. Exiting");
		            //System.exit(0);

		        } catch (MqttException me) {
		            System.out.println("reason " + me.getReasonCode());
		            System.out.println("msg " + me.getMessage());
		            System.out.println("loc " + me.getLocalizedMessage());
		            System.out.println("cause " + me.getCause());
		            System.out.println("excep " + me);
		            me.printStackTrace();
		        }
				//Xu li du lieu
//				String ngay = songay(thangnam);
				
				//Tao byte data - Dong goi
//				sendData = ngay.getBytes();
				
				//Gui Packet
//				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipclient, portclient);
//				server.send(sendPacket);
//				System.out.println("Gui thanh cong");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String songay(String a) {
		a = a.trim();
 		String[] st = a.split("/");
		int thang = Integer.parseInt(st[0]);
		int nam = Integer.parseInt(st[1]);
		int ngay = 0;
		switch (thang) {
		case 1: case 3: case 5: case 7: case 9: case 11: ngay = 31; break;
		case 4: case 6: case 8: case 10: case 12: ngay = 31; break;
		case 2:
			if (nam % 4 == 0) ngay = 29;
			else ngay = 28;
			break;
		}
		String kq = "thang " + thang + " nam " + nam + " co " + ngay + " ngay";
		return kq;
	}
}
