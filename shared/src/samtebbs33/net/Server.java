package samtebbs33.net;

import samtebbs33.event.Startable;
import samtebbs33.net.event.SocketEventListener;
import samtebbs33.net.event.SocketEventManager;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

/**
 * Created by samtebbs on 30/01/2016.
 */
public abstract class Server implements SocketEventListener, Startable, Closeable {

    protected ServerSocket socket;
    private int port;
    private Thread clientConnectionThread;

    public Server(int port, int maxClients, int timeout) throws IOException {
        this.port = port;
        this.socket = new ServerSocket(port);
        this.socket.setSoTimeout(timeout);
        clientConnectionThread = new Thread(() -> {
            while (true) {
                SocketStream client = null;
                try {
                    Socket socket = this.socket.accept();
                    socket.setSoTimeout(timeout);
                    client = new SocketStream(socket);
                    if (getNumClients() < maxClients) {
                        onClientConnected(client);
                    } else {
                        onClientRefused(client);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Returns the number of clients connected to the server
     *
     * @return
     */
    public abstract int getNumClients();

    /**
     * Send a packet to a client socket
     *
     * @param packet
     * @param clients
     * @throws IOException
     */
    public void send(Serializable packet, SocketStream... clients) throws IOException {
        for (SocketStream client : clients) client.write(packet);
    }

    public void send(Serializable packet, Iterable<SocketStream> clients) throws IOException {
        for (SocketStream client : clients) client.write(packet);
    }

    /**
     * Broadcast a packet to all connected clients
     *
     * @param packet
     */
    public void broadcast(Serializable packet) {
        getClients().forEach(client -> {
            try {
                send(packet, client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void broadcastExcluding(Serializable packet, SocketStream client) {
        getClients().forEach(socket -> {
            if (!socket.equals(client)) try {
                send(packet, socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public abstract Set<SocketStream> getClients();

    /**
     * Stop the slient acceptance thread
     */
    public void stopAcceptingClients() {
        this.clientConnectionThread.interrupt();
    }

    /**
     * Restart the client acceptance thread after it has been stopped
     */
    public void restartAcceptingClients() {
        if (!this.clientConnectionThread.isAlive()) this.clientConnectionThread.start();
    }

    /**
     * Called when a client connects to the server
     *
     * @param client
     * @throws IOException
     */
    protected void onClientConnected(SocketStream client) throws IOException {
        SocketEventManager manager = new SocketEventManager(client);
        manager.addListener(this);
        manager.start();
    }

    /**
     * Called when a client connection is refused
     *
     * @param client
     */
    protected void onClientRefused(SocketStream client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the server socket and stop the client acceptance thread
     */
    @Override
    public void close() {
        if (clientConnectionThread.isAlive()) clientConnectionThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start the server
     */
    @Override
    public void start() {
        if (!clientConnectionThread.isAlive()) clientConnectionThread.start();
    }

}
