package warehouse.event.manager;

import samtebbs33.event.EventManager;

/**
 * Created by sxt567 on 23/03/16.
 */
public abstract class RobotEventManager<E, T> extends EventManager<E> {
    public void onEvent(T t) {
        for(E e : listeners) each(e, t);
    }

    public abstract void each(E e, T t);
}
