package com.github.samtebbs33.net.event;

import com.github.samtebbs33.net.SocketStream;

import java.io.Serializable;

/**
 * Created by samtebbs on 29/01/2016.
 */
public class SocketEvent {

    public SocketStream stream;

    public SocketEvent(SocketStream client) {
        this.stream = client;
    }

    public static class SocketExceptionEvent extends SocketEvent {

        public Exception e;

        public SocketExceptionEvent(SocketStream client, Exception e) {
            super(client);
            this.e = e;
        }
    }

    public static class SocketPacketEvent extends SocketEvent {

        public Serializable packet;

        public SocketPacketEvent(SocketStream socket, Serializable packet) {
            super(socket);
            this.packet = packet;
        }
    }

}
