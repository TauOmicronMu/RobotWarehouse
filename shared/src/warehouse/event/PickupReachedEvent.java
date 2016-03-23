package warehouse.event;

import warehouse.util.ItemPickup;
import warehouse.util.Robot;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class PickupReachedEvent extends Event {
    public final ItemPickup pickup;
    public PickupReachedEvent(ItemPickup pickup, Robot robot) {
        super(robot);
        this.pickup = pickup;
    }

    public String toPacketString() {
        String s = "";
        return s;
    }

}
