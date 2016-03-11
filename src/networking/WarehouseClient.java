package networking;

import com.github.samtebbs33.net.Client;
import com.github.samtebbs33.net.event.SocketEvent;
import com.github.samtebbs33.net.event.SocketEvent.SocketExceptionEvent;
import com.github.samtebbs33.net.event.SocketEvent.SocketPacketEvent;

import lejos.nxt.remote.NXTCommand;
import warehouse.util.EventDispatcher;

public class WarehouseClient extends Client {

	public WarehouseClient() {
		// TODO Auto-generated constructor stub
		super();
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
	public void onConnectionRefused(SocketEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onConnected() {
		// TODO Auto-generated method stub
		
	}
	

}
