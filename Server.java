import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class Server {

	public static void main(String[] args) {
		//random names of the robots 
		String[] names = new String[3];
		String nameRobot1 = "Nick";
		String nameRobot2 = "NickTheSecond";
		String nameRobot3 = "NickTheThird";
		names[0] = nameRobot1;
		names[1] = nameRobot2;
		names[2] = nameRobot3;
		
		
		//client table to keep information about the clients
		ClientTable table = new ClientTable();

		try {
			//creating nxt communication via bluetooth
			NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);

			//all the robots added to NXT info
			//need to add robots adress to the last place of the constructors
			NXTInfo[] nxts = { 
					new NXTInfo(NXTCommFactory.BLUETOOTH, nameRobot1, "00165308E539"),
					
					new NXTInfo(NXTCommFactory.BLUETOOTH, nameRobot2 , "00165312F1F0"),

					new NXTInfo(NXTCommFactory.BLUETOOTH, nameRobot3 , "0016530A971B"), };
			
			//arraylist of thtreads
			ArrayList<Thread> threads = new ArrayList<>(nxts.length*2);
			
			//add robots to the table
			table.add(nameRobot1);
			table.add(nameRobot2);
			table.add(nameRobot3);
		
			int counter = 0;
			for (NXTInfo nxt : nxts) {

				//open the communication with the 3 robots
				ObjectOutputStream toClient = new ObjectOutputStream(nxtComm.getOutputStream());
				ObjectInputStream fromClient = new ObjectInputStream(nxtComm.getInputStream());
				
				//create two threads for communicating with the robot (receiving and sending  info)
				ServerReceiver rec = new ServerReceiver(fromClient, table, names[counter] );
				rec.start();
				ServerSender send = new ServerSender(toClient, table.getQueue(names[counter++]));
				send.start();
				
				//add threads to the arraylist
				threads.add(rec);
				threads.add(send);
			}
			
			//wait for threads them to join
			for (Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		} catch (NXTCommException | IOException e) {
			e.printStackTrace();
		}

	}

}
