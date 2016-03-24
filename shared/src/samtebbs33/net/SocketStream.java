package samtebbs33.net;


import warehouse.event.Event;

import java.io.*;

/**
 * Created by samtebbs on 31/01/2016.
 */
public class SocketStream implements Closeable {

    public long id;
    private InputStream in;
    private OutputStream out;

    public SocketStream(OutputStream out, InputStream in) throws IOException {
        this.out = out;
        this.in = in;
    }

    public void write(Event obj) throws IOException {
        byte[] bytes = obj.toPacketString().getBytes();
        out.write(bytes.length);
        out.write(bytes);
    }

    public Event read() throws IOException, ClassNotFoundException {
        byte[] bytes = new byte[in.read()];
        in.read(bytes);
        return Event.fromPacketString(new String(bytes));
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
    }
}
