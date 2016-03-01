package networking;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ServerSender extends Thread {

	private ObjectOutputStream toServer;
	private BlockQueue queue;

	public ServerSender(ObjectOutputStream toServer, BlockQueue queue) {
		this.toServer = toServer;
		this.queue = queue;

	}

	public void run() {
		while (true) {
			Message msg = queue.take();
			try {
				toServer.writeObject(msg);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}

}
