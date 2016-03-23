package samtebbs33.net.event;

import samtebbs33.event.EventManager;
import samtebbs33.net.SocketStream;

import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;

/**
 * Created by samtebbs on 29/01/2016.
 */
public class SocketEventManager extends EventManager<SocketEventListener> implements Runnable {

    SocketStream client;

    public SocketEventManager(SocketStream socket) throws IOException {
        this.client = socket;
    }

    /**
     * Start the event manager and begin listening for socket events
     */
    @Override
    public void start() {
        try {
            super.start(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called by the event manager thread
     */
    @Override
    public void run() {
        try {
            while (true) {
                final Serializable obj = client.read();
                for (SocketEventListener listener : listeners)
                    listener.onPacketReceived(new SocketEvent.SocketPacketEvent(client, obj));
                //forEach(listener -> listener.onPacketReceived(new SocketEvent.SocketPacketEvent(client, obj)));
            }
        } catch (SocketException | EOFException e) {
            for (SocketEventListener listener : listeners)
                listener.onDisconnection(new SocketEvent.SocketExceptionEvent(client, e));
            //forEach(listener -> listener.onDisconnection(new SocketEvent.SocketExceptionEvent(client, e)));
        } catch (Exception e) {
            for (SocketEventListener listener : listeners) listener.onTimeout(new SocketEvent(client));
        }
    }
}
