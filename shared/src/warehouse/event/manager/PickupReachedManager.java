package warehouse.event.manager;

import warehouse.event.PickupReachedEvent;
import warehouse.event.listener.PickupReachedListener;

/**
 * Created by sxt567 on 24/03/16.
 */
public class PickupReachedManager extends RobotEventManager<PickupReachedListener, PickupReachedEvent> {
    @Override
    public void each(PickupReachedListener pickupReachedListener, PickupReachedEvent pickupReachedEvent) {
        pickupReachedListener.onPickupReached(pickupReachedEvent);
    }
}
