package com.github.samtebbs33.net;

import java.io.*;
import java.net.Socket;

/**
 * Created by samtebbs on 31/01/2016.
 */
public class SocketStream implements Closeable {

    public long id;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;

    public SocketStream(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void write(Serializable obj) throws IOException {
        this.out.writeObject(obj);
    }

    public Serializable read() throws IOException, ClassNotFoundException {
        return (Serializable) this.in.readObject();
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
