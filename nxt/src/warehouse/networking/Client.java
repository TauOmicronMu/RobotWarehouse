package warehouse.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import samtebbs33.net.SocketStream;
import samtebbs33.net.event.SocketEvent;
import samtebbs33.net.event.SocketEventListener;

/**
 * Created by samtebbs on 30/01/2016.
 */
public abstract class Client implements SocketEventListener {

    private OutputStream out;
    private InputStream in;
    
    public Client() throws IOException {
        NXTConnection connection = Bluetooth.waitForConnection();
        this.out = connection.openOutputStream();
        this.in = connection.openInputStream();
        onConnected();
    }

    /**
     * Send a String to the connected server
     *
     * @param string
     * @throws IOException
     */
    public void send(String s) throws IOException {
        try {
            out.write(s.getBytes(), 0, s.length());
        } catch (IOException e) {
            //TODO : DO something here.
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
