package warehouse.event.manager;

import warehouse.event.DropOffReachedEvent;
import warehouse.event.listener.DropOffReachedListener;

/**
 * Created by sxt567 on 23/03/16.
 */
public class DropOffReachedManager extends RobotEventManager<DropOffReachedListener, DropOffReachedEvent> {
    @Override
    public void each(DropOffReachedListener dropOffReachedListener, DropOffReachedEvent dropOffReachedEvent) {
        dropOffReachedListener.onDropOffReached(dropOffReachedEvent);
    }
}
