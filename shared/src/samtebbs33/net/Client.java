package samtebbs33.net;

import samtebbs33.net.event.SocketEvent;
import samtebbs33.net.event.SocketEventListener;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;

/**
 * Created by samtebbs on 30/01/2016.
 */
public abstract class Client implements SocketEventListener {

    private SocketStream stream;

    public Client(String name) throws IOException {
        NXTConnection conn = Bluetooth.waitForConnection();
        stream = new SocketStream(conn.openDataOutputStream(), conn.openDataInputStream());
        onConnected();
    }

    /**
     * Send a packet to the connected server
     *
     * @param packet
     * @throws IOException
     */
    public void send(Serializable packet) throws IOException {
        try {
            stream.write(packet);
        } catch (SocketException e) {
            onDisconnection(new SocketEvent.SocketExceptionEvent(stream, e));
        }
    }

    /**
     * Called when a server connection is refused
     *
     * @param event
     */
    public abstract void onConnectionRefused(SocketEvent event);

    /**
     * Called when a server connection is established
     */
    protected abstract void onConnected();

}
