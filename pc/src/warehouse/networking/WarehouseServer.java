package warehouse.networking;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import lejos.pc.comm.NXTCommException;
import samtebbs33.net.SocketStream;
import samtebbs33.net.event.SocketEvent;
import warehouse.event.Event;
import warehouse.util.Direction;
import warehouse.util.EventDispatcher;
import warehouse.util.Location;
import warehouse.util.MultiSubscriber;

import warehouse.util.Robot;

public class WarehouseServer extends Server {
	
	private SocketStream[] robotConnections = new SocketStream[3];
	private int numRobots = 0;
	public static final ArrayList<Robot> robots = new ArrayList(); 

	public WarehouseServer() throws IOException, NXTCommException {
		super();
		EventDispatcher.subscribe2(this);
	}
	
	@Override
	public void onClientConnected(SocketStream stream) throws IOException {
		super.onClientConnected(stream);
		robots.add(new Robot( robotNames[numRobots], new Location(0, 0), Direction.NORTH, numRobots));;
		robotConnections[numRobots++] = stream;  
	}

	@Override
	public void onPacketReceived(SocketEvent.SocketPacketEvent event) {
		EventDispatcher.onEvent2(event.packet);
	}

	@Override
	public void onTimeout(SocketEvent socket) {
		System.err.println("Timeout");
	}

	@Override
	public void onDisconnection(SocketEvent.SocketExceptionEvent event) {
		System.err.println("Disconnected");
	}

	@Override
	public int getNumClients() {
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

	@Override
	public void close() throws IOException {
		System.err.println("Why did you call this method?");
	}

	@MultiSubscriber
	public void onEvent(Event event) {
		if (event.robot != null) {
			send(event.robot.id, event);
		}
		else {
			broadcast(event);
		}

	}
}
