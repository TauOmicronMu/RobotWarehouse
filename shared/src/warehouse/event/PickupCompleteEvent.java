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
        s += "PickupComplete,";
        s += super.toPacketString();
        s += ",";
        s += pickup.toPacketString();
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PickupCompleteEvent that = (PickupCompleteEvent) o;

        return pickup != null ? pickup.equals(that.pickup) : that.pickup == null;

    }

    @Override
    public int hashCode() {
        return pickup != null ? pickup.hashCode() : 0;
    }
}
