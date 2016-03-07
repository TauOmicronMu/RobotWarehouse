package com.github.samtebbs33.net;

import com.github.samtebbs33.net.event.SocketEvent;
import com.github.samtebbs33.net.event.SocketEventListener;
import com.github.samtebbs33.net.event.SocketEventManager;

import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by samtebbs on 30/01/2016.
 */
public abstract class Client implements SocketEventListener {

    protected final String ip;
    protected final int port;
    private SocketStream stream;

    public Client(String ip, int port, int timeout) throws IOException {
        this.ip = ip;
        this.port = port;
        try {
            Socket socket = new Socket(ip, port);
            socket.setSoTimeout(timeout);
            stream = new SocketStream(socket);
            SocketEventManager eventManager = new SocketEventManager(stream);
            eventManager.addListener(this);
            eventManager.start();
            onConnected();
        } catch (ConnectException e) {
            onConnectionRefused(new SocketEvent(stream));
        } catch (SocketException e) {
            onDisconnection(new SocketEvent.SocketExceptionEvent(stream, e));
        }
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
