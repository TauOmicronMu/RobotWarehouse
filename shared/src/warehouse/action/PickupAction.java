package warehouse.action;

import warehouse.util.ItemPickup;

/**
 * Created by samtebbs on 05/03/2016.
 */
public class PickupAction extends Action {

    public ItemPickup pickup;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PickupAction that = (PickupAction) o;

        return pickup != null ? pickup.equals(that.pickup) : that.pickup == null;

    }

    @Override
    public int hashCode() {
        return pickup != null ? pickup.hashCode() : 0;
    }

    @Override
    public String toPacketString() {
        return "Pickup";
    }
}
