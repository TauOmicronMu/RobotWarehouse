package samtebbs33.event;

import java.util.ArrayList;

/**
 * Created by samtebbs on 29/01/2016.
 */
public abstract class EventManager<E> {

    /**
     * The list of listeners that get notified about any event that occurs
     */
    protected ArrayList<E> listeners = new ArrayList<>();

    /**
     * Start the event manager's thread with a runnable
     *
     * @param runnable
     * @throws InterruptedException
     */
    protected final void start(Runnable runnable) throws InterruptedException {
        (new Thread(runnable)).start();
    }

    /**
     * Add to the listener list
     *
     * @param listener
     */
    public void addListener(E listener) {
        listeners.add(listener);
    }

}
