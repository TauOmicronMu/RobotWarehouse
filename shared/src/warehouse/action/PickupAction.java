package warehouse.action;

import warehouse.util.ItemPickup;

/**
 * Created by samtebbs on 05/03/2016.
 */
public class PickupAction extends Action {

    public ItemPickup pickup;

    @Override
    public String toPacketString() {
        return "Pickup";
    }
}
