package warehouse.job;

import warehouse.util.ItemPickup;
import warehouse.util.Location;

import java.util.LinkedList;

public class Job {

    private Location dropLocation;
    private LinkedList<ItemPickup> pickups;

    public Job(Location dropLocation, LinkedList<ItemPickup> pickups) {
        this.dropLocation = dropLocation;
        this.pickups = pickups;

    }
}
