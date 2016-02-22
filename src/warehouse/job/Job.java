package warehouse.job;

import warehouse.ItemPickup;
import warehouse.Location;

import java.util.LinkedList;

/**
 * Created by samtebbs on 22/02/2016.
 */
public class Job {

    public Location dropLocation;
    public LinkedList<ItemPickup> pickups;

    public Job(Location dropLocation, LinkedList<ItemPickup> pickups) {
        this.dropLocation = dropLocation;
        this.pickups = pickups;
    }
}
