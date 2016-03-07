package com.github.samtebbs33.event;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by samtebbs on 29/01/2016.
 */
public abstract class EventManager<E> implements Startable {

    /**
     * The list of listeners that get notified about any event that occurs
     */
    protected ArrayList<E> listeners = new ArrayList<>();

    /**
     * Notify all listeners about the event
     *
     * @param consumer - The consumer's accept method is called with each listener
     * @example forEach(listener -> listener.onSomeEvent())
     */
    public final void forEach(Consumer<E> consumer) {
        listeners.forEach((l) -> consumer.accept(l));
    }

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
