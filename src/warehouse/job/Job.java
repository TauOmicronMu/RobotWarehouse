package warehouse.job;

import warehouse.ItemPickup;
import warehouse.Location;

import java.util.LinkedList;

/**
 * Created by samtebbs on 22/02/2016.
 */
public class Job {

    private Location dropLocation;
    private LinkedList<ItemPickup> pickups;

    public Job(Location dropLocation, LinkedList<ItemPickup> pickups){
    	
    	this.dropLocation = dropLocation;
    	this.pickups = pickups;
    }
    
    public Location getDropLocation(){
    	
    	return this.dropLocation;
    }
    
    public LinkedList<ItemPickup> getPickups(){
    	
    	return this.pickups;
    }
    
    public String toString(){
    	
    	return "Job of pickups :" + this.pickups.toString() + " to location " + this.dropLocation.toString();
    }
}
