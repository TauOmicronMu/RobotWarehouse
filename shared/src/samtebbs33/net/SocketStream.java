package samtebbs33.net;

import com.sun.tools.doclets.internal.toolkit.util.DocFinder;

import java.io.*;
import java.net.Socket;

/**
 * Created by samtebbs on 31/01/2016.
 */
public class SocketStream implements Closeable {

    public long id;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public SocketStream(OutputStream out, InputStream in) throws IOException {
        this.out = new ObjectOutputStream(out);
        this.in = new ObjectInputStream(in);
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
    }
}
