package warehouse.event.listener;

import warehouse.event.PickupCompleteEvent;

/**
 * Created by sxt567 on 23/03/16.
 */
public interface PickupCompleteListener {
    public void onPickupComplete(PickupCompleteEvent e);
}
