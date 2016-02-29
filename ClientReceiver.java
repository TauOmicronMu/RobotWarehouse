import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientReceiver extends Thread {

	private ClientState state;
	private ObjectInputStream server;

	public ClientReceiver(ObjectInputStream server, ClientState state) {
		this.server = server;
		this.state = state;
	}

	public void run() {
		
		try {
			while (true) {
				//reads the information from the server and then responds accordingly
				Object obj = server.readObject();
				if (obj != null) {
					
						//Do some stuff
				}

				else {
					server.close(); 
					throw new IOException("Got null from server"); 
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Server seems to have died " + e.getMessage());
			System.exit(1); // Give up.
		}
	}

}
