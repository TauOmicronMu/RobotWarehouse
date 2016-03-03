package com.github.samtebbs33.net.packet;

import java.io.Serializable;

/**
 * Created by samtebbs on 30/01/2016.
 */
public class ServerConnectionPacket implements Serializable {

    /**
     * The username requested by the client
     */
    public String username;

    public ServerConnectionPacket(String username) {
        this.username = username;
    }
}
