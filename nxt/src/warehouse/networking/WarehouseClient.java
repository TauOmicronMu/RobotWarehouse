package warehouse.networking;

import java.io.IOException;

import samtebbs33.net.event.SocketEvent;
import samtebbs33.net.event.SocketEvent.SocketExceptionEvent;
import samtebbs33.net.event.SocketEvent.SocketPacketEvent;
import warehouse.event.Event;
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
		EventDispatcher.onEvent2(event.packet);
		
	}

	@Override
	public void onTimeout(SocketEvent socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnection(SocketExceptionEvent event) {
		// TODO Auto-generated method stub
		System.err.println("Disconnected");
	}

	@Override
	public void onConnectionRefused(SocketEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onConnected() {
		// TODO Auto-generated method stub
		
	}
	
	@MultiSubscriber
	public void onEvent(Event event) throws IOException {
		send(event);
	}
	

}
