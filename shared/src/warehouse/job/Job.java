package warehouse.job;

import warehouse.util.ItemPickup;
import warehouse.util.Location;

import java.util.List;

public class Job {

    public final Location dropLocation;
    public final List<ItemPickup> pickups;
    public final String id;

    public Job(Location dropLocation, List<ItemPickup> pickups, String id) {
        this.dropLocation = dropLocation;
        this.pickups = pickups;
        this.id = id;
    }
    
    public boolean isAssigned() {
        return false;
    }

    @Override
    public String toString() {
        return id;
    }
}
