package samtebbs33.net.packet;

import java.io.Serializable;

/**
 * Created by samtebbs on 30/01/2016.
 */
public class ClientConnectionPacket implements Serializable {

    /**
     * The client ID allocated by the server
     */
    public long clientID;

    public ClientConnectionPacket(long clientID) {
        this.clientID = clientID;
    }
}
