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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PickupReachedEvent that = (PickupReachedEvent) o;

        return pickup != null ? pickup.equals(that.pickup) : that.pickup == null;

    }

    @Override
    public int hashCode() {
        return pickup != null ? pickup.hashCode() : 0;
    }
}
