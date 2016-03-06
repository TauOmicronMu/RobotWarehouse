package warehouse.job;

import warehouse.util.ItemPickup;
import warehouse.util.Location;

import java.util.LinkedList;

public class Job {

    public Location dropLocation;
    public LinkedList<ItemPickup> pickups;

    public Job(Location dropLocation, LinkedList<ItemPickup> pickups) {
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
