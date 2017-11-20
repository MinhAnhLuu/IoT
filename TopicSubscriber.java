import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttCallback;

import java.io.DataOutputStream;
//TCP
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TopicSubscriber {
	public void run(String... args) {
        System.out.println("TopicSubscriber initializing...");

        String host = "tcp://m14.cloudmqtt.com:19557";
        String username = "uqgxmipu";
        String password = "kJKLA8Y62Qmq";

        if (!host.startsWith("tcp://")) {
            host = "tcp://" + host;
        }
        
        while (true) {
        	try {
                // Create an Mqtt client
                MqttClient mqttClient = new MqttClient(host, "HelloWorldSub");
                MqttConnectOptions connOpts = new MqttConnectOptions();
                
                connOpts.setCleanSession(true);
                connOpts.setUserName(username);
                connOpts.setPassword(password.toCharArray());
              
                // Connect the client
                System.out.println("Connecting to Solace messaging at "+host);
                mqttClient.connect(connOpts);
                System.out.println("Connected");

                // Latch used for synchronizing b/w threads
                final CountDownLatch latch = new CountDownLatch(1);
                
                // Topic filter the client will subscribe to
                final String subTopic = "T/GettingStarted/pubsub";
              
                // Callback - Anonymous inner-class for receiving messages
                mqttClient.setCallback(new MqttCallback() {

                    public void messageArrived(String topic, MqttMessage message) throws Exception {

                        // matches any subscription made by the client
                        String time = new Timestamp(System.currentTimeMillis()).toString();
                        String mess = new String(message.getPayload());
                                                                        
                        System.out.println("\nReceived a Message!" +
                                "\n\tTime:    " + time + 
                                "\n\tTopic:   " + topic + 
                                "\n\tMessage: " + mess + 
                                "\n\tQoS:     " + message.getQos() + "\n");
                        
                        
                        //Gui du lieu ve Client qua TCP
                        //Mo cong ket noi
            			ServerSocket ss = new ServerSocket(1234);
            			System.out.println("Server dang mo tren port 1234 ");
            			
            			//Listen clientTCP ket noi vao
            			Socket s = ss.accept();
            			System.out.println("Co client TCP dang ket noi");
            			
            			// Gui du lieu
            			OutputStream os = s.getOutputStream();
            						
            			//Gui 1 ki tu tu clientTCP
            			byte[] sendData = new byte[1024];
            			mess = mess.trim();
            			String data = xulidulieu(mess);         				
            			sendData = data.getBytes();
            			os.write(sendData);
            			
            			ss.close();
            			s.close();
                        
            			// unblock main thread
                        latch.countDown(); 
                    }

                    public void connectionLost(Throwable cause) {
                        System.out.println("Connection to Solace messaging lost!" + cause.getMessage());
                        latch.countDown();
                    }
                    public void deliveryComplete(IMqttDeliveryToken token) {
                    }
                });
                                           
                
                // Subscribe client to the topic filter and a QoS level of 0
                System.out.println("Subscribing client to topic: " + subTopic);
                mqttClient.subscribe(subTopic, 0);
                System.out.println("Subscribed");

                // Wait for the message to be received
                try {
                    latch.await(); // block here until message received, and latch will flip
                } catch (InterruptedException e) {
                    System.out.println("I was awoken while waiting");
                }
          
                // Disconnect the client
                mqttClient.disconnect();               
                
            } catch (MqttException me) {
                System.out.println("reason " + me.getReasonCode());
                System.out.println("msg " + me.getMessage());
                System.out.println("loc " + me.getLocalizedMessage());
                System.out.println("cause " + me.getCause());
                System.out.println("excep " + me);
                me.printStackTrace();
            }
        }  
    }
	
	public static String xulidulieu(String s) {
		String ret = "";
				
		switch(s) {
			case "Return": case "return":
				ret = "Please choose a type of car you need help with: Sedan - SUV - Pickup - MVP";
				break;
				
			//---------------------------------------
			case "Sedan": case "sedan": case "SEDAN":
				ret = "Please choose a car that you wanna find out more information: Toyota Altis - Kia K3 - Mazda 3";
				break;
			case "Toyota Altis": case "Altis": case "altis":
				ret = "Toyota Altis: Cost: 707-864 million. 1798cc. For more information: http://www.google.com";
				break;
			case "Mazda 3": case "mazda 3": case "mazda3":
				ret = "Mazda 3: Cost: 660-689 million. 1496cc. For more information: http://www.google.com";
				break;
			case "Kia K3": case "k3": case "K3":
				ret = "Kia K3: Cost: 543-661 million. 1591cc. For more information: http://www.google.com";
				break;
			
			//-----------------------------------------
			case "SUV": case "suv": case "Suv":
				ret = "Please choose a car that you wanna find out more information: Mazda CX-5 - Huyndai Tucson - Honda CR-V";
				break;
			case "Mazda CX-5": case "cx-5": case "CX-5":
				ret = "Mazda CX-6: Cost: 819-989 million. 1989cc. For more information: http://www.google.com";
				break;	
			case "Huyndai Tucson": case "Tucson": case "tucson":
				ret = "Huyndai Tucson: Cost: 760-890 million. 1989cc. For more information: http://www.google.com";
				break;
			case "Honda CR-V": case "CR-V": case "cr-v":
				ret = "Honda CR-V: Cost: 788-898 million. 2010cc. For more information: http://www.google.com";
				break;	
			
			//-----------------------------------------
			case "Pickup": case "PICKUP": case "pickup":
				ret = "Please choose a car that you wanna find out more information: Ford Ranger - Mazda BT-50 - Toyota Hilux";
				break;
			case "Ford Ranger": case "Ranger": case "ranger":
				ret = "Ford Ranger: Cost: 634-866 million. 2196cc. For more information: http://www.google.com";
				break;
			case "Mazda BT-50": case "bt-50": case "BT-50":
				ret = "Mazda BT-50: Cost: 640-825 million. 1197cc. For more information: http://www.google.com";
				break;
			case "Toyota Hilux": case "hilux": case "Hilux":
				ret = "Toyata Hilux: Cost: 631-775 million. 2386cc. For more information: http://www.google.com";
				break;
			
			//-----------------------------------------
			case "MVP": case "mvp": case "Mvp":
				ret = "Please choose a car that you wanna find out more information: Toyota Innova - Kia Rondo";
				break;
			case "Toyota Innova": case "Innova": case "innova":
				ret = "Toyota Innova: Cost: 743-945 million. 2007cc. For more information: http://www.google.com";
				break;
			case "Kia Rondo": case "Rondo": case "rondo":
				ret = "Kia Rondo: Cost: 819-832 million. 1989cc. For more information: http://www.google.com";
				break;
			
			default :
				ret = "Invalid parameter! Please re-type exactly!";
				break;
		}
		return ret;
		
	}

    public static void main(String[] args) {
        new TopicSubscriber().run(args);
    }
}