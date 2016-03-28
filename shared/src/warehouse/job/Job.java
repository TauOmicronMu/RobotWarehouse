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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (cancelledInTrainingSet != job.cancelledInTrainingSet) return false;
        if (dropLocation != null ? !dropLocation.equals(job.dropLocation) : job.dropLocation != null) return false;
        if (!pickups.equals(job.pickups)) return false;
        return id.equals(job.id);

    }

    @Override
    public int hashCode() {
        int result = dropLocation != null ? dropLocation.hashCode() : 0;
        result = 31 * result + pickups.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + (cancelledInTrainingSet ? 1 : 0);
        return result;
    }

    /*
         * "assigned?,numberofpickups,<name,location,itemcount>*n,id"
         */
    public String toPacketString() {
        String s = "";
        s += isAssigned();
        s += ",";
        s += dropLocation.toPacketString();
        s += ",";
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
