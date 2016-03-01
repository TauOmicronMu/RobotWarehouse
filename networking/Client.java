package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class Client {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Waiting for Bluetooth connection...");
		BTConnection connection = Bluetooth.waitForConnection();
		System.out.println("OK!");
		ObjectOutputStream toServer;
		ObjectInputStream fromServer;

		//opens object stream between server and robot
		toServer = new ObjectOutputStream(connection.openOutputStream());
		fromServer = new ObjectInputStream(connection.openInputStream());

		//the information about the client shared between all the threads
		ClientState state = new ClientState();//information about client
		
		//two threads for sending to and receiving information from server
		ClientSender sender = new ClientSender(toServer, state);
		ClientReceiver receiver = new ClientReceiver(fromServer, state);

		// Run two threads in parallel
		sender.start();
		receiver.start();

		//wait until they join
		try {
			sender.join();
			toServer.close();
			receiver.join();
			fromServer.close();
			connection.close();
		} catch (IOException e) {
			System.err.println("Something wrong " + e.getMessage());
			System.exit(1); // Give up.
		} catch (InterruptedException e) {
			System.err.println("Unexpected interruption " + e.getMessage());
			System.exit(1); // Give up.
		}

	}
}
