package warehouse.event.manager;

import warehouse.event.PickupCompleteEvent;
import warehouse.event.listener.PickupCompleteListener;

/**
 * Created by sxt567 on 23/03/16.
 */
public class PickupCompleteManager extends RobotEventManager<PickupCompleteListener, PickupCompleteEvent> {
    @Override
    public void each(PickupCompleteListener pickupCompleteListener, PickupCompleteEvent pickupCompleteEvent) {
        pickupCompleteListener.onPickupComplete(pickupCompleteEvent);
    }
}
