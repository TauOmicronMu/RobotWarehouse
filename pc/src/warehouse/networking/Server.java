package warehouse.networking;


import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import samtebbs33.net.SocketStream;
import samtebbs33.net.event.SocketEventListener;
import samtebbs33.net.event.SocketEventManager;

/**
 * Created by samtebbs on 30/01/2016.
 */
public abstract class Server implements SocketEventListener, Closeable {

    String[] robotNames = {"Charmander"};

    public Server() throws IOException, NXTCommException {
        for (String robot : robotNames) {
            NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
            NXTInfo[] info = nxtComm.search(robot);
            nxtComm.open(info[0]);
            onClientConnected(new SocketStream(nxtComm.getOutputStream(), nxtComm.getInputStream()));
        }
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
        for (SocketStream stream : getClients()) {
            try {
                send(packet, stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /* getClients().forEach(client -> {
            try {
                send(packet, client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }); */
    }

    public void broadcastExcluding(Serializable packet, SocketStream client) {
        for (SocketStream stream : getClients()) {
            try {
                if(!stream.equals(client)) send(packet, stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /* getClients().forEach(socket -> {
            if (!socket.equals(client)) try {
                send(packet, socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }); */
    }

    public abstract Set<SocketStream> getClients();

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


}
