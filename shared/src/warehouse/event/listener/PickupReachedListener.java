package warehouse.event.listener;

import warehouse.event.PickupReachedEvent;

/**
 * Created by sxt567 on 23/03/16.
 */
public interface PickupReachedListener {
    public void onPickupReached(PickupReachedEvent e);
}
