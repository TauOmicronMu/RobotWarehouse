package warehouse.event;

import warehouse.util.ItemPickup;
import warehouse.util.Robot;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class PickupCompleteEvent extends Event {
    public final ItemPickup pickup;

    public PickupCompleteEvent(Robot robot, ItemPickup pickup) {
        super(robot);
        this.pickup = pickup;
    }

    public String toPacketString() {
        String s = "";
        s += "PickupComplete";
        s += ",";
        s += pickup.toPacketString();
        return s;
    }
}
