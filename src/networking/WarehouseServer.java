package networking;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.github.samtebbs33.net.Server;
import com.github.samtebbs33.net.SocketStream;
import com.github.samtebbs33.net.event.SocketEvent;
import com.github.samtebbs33.net.event.SocketEvent.SocketExceptionEvent;
import com.github.samtebbs33.net.event.SocketEvent.SocketPacketEvent;

import warehouse.util.EventDispatcher;

public class WarehouseServer extends Server {
	
	private SocketStream[] robotConnections = new SocketStream[3];
	private int numRobots = 0;

	public WarehouseServer(int port, int maxClients, int timeout) throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onClientConnected(SocketStream stream) {
		try {
			super.onClientConnected(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		robotConnections[numRobots++] = stream;  
	}

	@Override
	public void onPacketReceived(SocketPacketEvent event) {
		EventDispatcher.onEvent2(event.packet);
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
	public int getNumClients() {
		// TODO Auto-generated method stub
		return numRobots;
	}

	@Override
	public Set<SocketStream> getClients() {
		HashSet<SocketStream> set = new HashSet();
		for(int i = 0; i < numRobots; i++) {set.add(robotConnections[i]);}
		return set;
	}
	
	public void send(int robotID, Serializable event) {
		
		try {
			robotConnections[robotID].write(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
