package warehouse.job;

import warehouse.util.ItemPickup;
import warehouse.util.Location;

import java.util.List;

public class Job {

    public Location dropLocation;
    public final List<ItemPickup> pickups;
    public final String id;
    public boolean cancelledInTrainingSet;

    public Job(Location dropLocation, List<ItemPickup> pickups, String id) {
        this.dropLocation = dropLocation;
        this.pickups = pickups;
        this.id = id;
    }
    
    public Job(Location dropLocation, List<ItemPickup> pickups) {
        this(dropLocation, pickups, "123test");
    }
    
    public boolean isAssigned() {
        return false;
    }

    @Override
    public String toString() {
        return "{" +
                "dropLocation=" + dropLocation +
                ", pickups=" + pickups +
                ", id='" + id + '\'' +
                ", cancelledInTrainingSet=" + cancelledInTrainingSet +
                '}';
    }

    /*
     * "numberofpickups,<name,location,itemcount>*n,id"
     */
    public String toPacketString() {
        String s = "";
        s += pickups.size();
        s += ",";
        for(ItemPickup p : pickups) {
            s += p.itemName;
            s += ",";
            s += p.location;
            s += ",";
            s += p.itemCount;
            s += ",";
        }
        s += id;
        return s;
    }
}
