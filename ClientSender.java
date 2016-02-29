import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientSender extends Thread {

	private ObjectOutputStream server;
	private ClientState state;

	ClientSender(ObjectOutputStream toServer, ClientState state) {
		this.server = toServer;
		this.state = state;
	}

	public void run() {

//		try {
//
//			while (true) {
//				Send information from the robot to the server, needs to be taken from robot
//				Object o = new Object(); //Random object
//				server.writeObject(o); //sending to the server
			}
//		} catch (IOException e) {
//			System.err.println("Communication broke in ClientSender" + e.getMessage());
//			System.exit(1);
//		}
//	}

}
