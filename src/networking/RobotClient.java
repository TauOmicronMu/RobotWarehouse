package networking;

import java.io.IOException;

import com.github.samtebbs33.net.Client;
import com.github.samtebbs33.net.event.SocketEvent;
import com.github.samtebbs33.net.event.SocketEvent.SocketExceptionEvent;
import com.github.samtebbs33.net.event.SocketEvent.SocketPacketEvent;

public class RobotClient extends Client {

	public RobotClient(String ip, int port, int timeout) throws IOException {
		super(ip, port, timeout);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPacketReceived(SocketPacketEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTimeout(SocketEvent socket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnection(SocketExceptionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionRefused(SocketEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onConnected() {
		// TODO Auto-generated method stub

	}

}
