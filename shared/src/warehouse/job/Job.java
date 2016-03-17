package warehouse.job;

import warehouse.util.ItemPickup;
import warehouse.util.Location;

import java.util.List;

public class Job {

    public Location dropLocation;
    public List<ItemPickup> pickups;

    public Job(Location dropLocation, List<ItemPickup> pickups) {
        this.dropLocation = dropLocation;
        this.pickups = pickups;
    }
    
    public boolean isAssigned() {
        return false;
    }

    @Override
    public String toString() {
        return "Job{" +
                "dropLocation=" + dropLocation +
                ", pickups=" + pickups +
                '}';
    }
}
