package warehouse.networking;

import java.io.IOException;

import lejos.nxt.LCD;
import samtebbs33.net.event.SocketEvent;
import samtebbs33.net.event.SocketEvent.SocketExceptionEvent;
import samtebbs33.net.event.SocketEvent.SocketPacketEvent;
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
import warehouse.event.WrongPlaceEvent;
import warehouse.event.manager.RobotEventManager;
import warehouse.util.EventDispatcher;
import warehouse.util.MultiSubscriber;

public class WarehouseClient extends Client {

	public WarehouseClient() throws IOException {
		// TODO Auto-generated constructor stub
		super();
		EventDispatcher.subscribe2(this);
	}

	@Override
	public void onPacketReceived(SocketPacketEvent event) {
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
		// TODO Auto-generated method stub
		p("Timeout");
		
	}

	@Override
	public void onDisconnection(SocketExceptionEvent event) {
		// TODO Auto-generated method stub
		p("Disconnected");
	}

	@Override
	public void onConnectionRefused(SocketEvent event) {
		// TODO Auto-generated method stub
		p("Con Refused");
	}

	@Override
	protected void onConnected() {
		// TODO Auto-generated method stub
		p("Connected ;)");
		
	}
	
	@MultiSubscriber
	public void onEvent(Event event) throws IOException {
		send(event.toPacketString());
	}
	
	private void p(String s) {
		LCD.clearDisplay();
		LCD.drawString(s, 1, 1);
	}
	

}
