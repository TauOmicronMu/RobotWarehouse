package warehouse.networking;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lejos.pc.comm.NXTCommException;
import samtebbs33.net.SocketStream;
import samtebbs33.net.event.SocketEvent;
import warehouse.event.ActionCompleteEvent;
import warehouse.event.BeginAssigningEvent;
import warehouse.event.DropOffReachedEvent;
import warehouse.event.Event;
import warehouse.event.JobAssignedEvent;
import warehouse.event.JobCancellationEvent;
import warehouse.event.JobCompleteEvent;
import warehouse.event.PickupCompleteEvent;
import warehouse.event.PickupReachedEvent;
import warehouse.event.RobotLostEvent;
import warehouse.event.RobotOffEvent;
import warehouse.event.StartingCoordinatesEvent;
import warehouse.event.WrongPlaceEvent;
import warehouse.event.manager.RobotEventManager;
import warehouse.util.Direction;
import warehouse.util.EventDispatcher;
import warehouse.util.Location;
import warehouse.util.MultiSubscriber;

import warehouse.util.Robot;

public class WarehouseServer extends Server {
	
	private SocketStream[] robotConnections = new SocketStream[3];
	private int numRobots = 0;
	public static final List<Robot> robots = new ArrayList();

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
		Event e = (Event)event.packet;
		
		if(e instanceof ActionCompleteEvent){
			RobotEventManager.actionCompleteManager.onEvent((ActionCompleteEvent) e);
		}
		else if(e instanceof BeginAssigningEvent){
			RobotEventManager.beginAssigningManager.onEvent((BeginAssigningEvent) e);
		}
		else if(e instanceof DropOffReachedEvent){
			RobotEventManager.dropOffReachedManager.onEvent((DropOffReachedEvent) e);
		}
		else if(e instanceof JobAssignedEvent){
			RobotEventManager.jobAssignedManager.onEvent((JobAssignedEvent) e);
		}
		else if(e instanceof JobCancellationEvent){
			RobotEventManager.jobCancellationMAnager.onEvent((JobCancellationEvent) e);
		}
		else if(e instanceof JobCompleteEvent){
			RobotEventManager.jobCompleteManager.onEvent((JobCompleteEvent) e);
		}
		else if(e instanceof PickupCompleteEvent){
			RobotEventManager.pickupCompleteManager.onEvent((PickupCompleteEvent) e);
		}
		else if(e instanceof PickupReachedEvent){
			RobotEventManager.pickupReachedManager.onEvent((PickupReachedEvent) e);
		}
		else if(e instanceof RobotLostEvent){
			RobotEventManager.robotLostManager.onEvent((RobotLostEvent) e);
		}
		else if(e instanceof RobotOffEvent){
			RobotEventManager.robotOffManager.onEvent((RobotOffEvent) e);
		}
		else if(e instanceof WrongPlaceEvent){
			RobotEventManager.wrongPlaceManager.onEvent((WrongPlaceEvent) e);
		}
		else {
			//╭━━━━━━━━━━╮
			//┃ ● ══　 █ ┃
			//┃██████████┃
			//┃██████████┃
			//┃██████████┃
			//┃█ ur adop█┃
			//┃█ -Mum   █┃
			//┃██████████┃
			//┃██████████┃
			//┃██████████┃
			//┃　　　○　  ┃
			//╰━━━━━━━━━━╯ 
		}
		
		
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
	
	public void send(int robotID, Event event) {
		
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
