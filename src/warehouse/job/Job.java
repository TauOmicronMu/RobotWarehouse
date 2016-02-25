package warehouse.job;

import warehouse.ItemPickup;
import warehouse.Location;

import java.util.LinkedList;

/**
 * Created by samtebbs on 22/02/2016.
 * 
 * Modified by Owen on 25/02/2016:
 * 
 * - Added constructor which initialises droplocation and pickups fields
 * - Made fields private and added get and set methods
 * - Added toString method for debugging
 * - Added javadocs
 */
public class Job {

    private Location dropLocation;
    private LinkedList<ItemPickup> pickups;

    /**
     * Create a new job from a dropLocation (the end location to which the
     * items must be delivered) and a linked list of 'Item Pickups' (object which 
     * describes a location for an item to be picked up, the number of items to be picked up,
     * and an item name).
     *  
     * @param dropLocation location where the items must be delivered to
     * @param pickups the linked list of item pickup objects
     */
    public Job(Location dropLocation, LinkedList<ItemPickup> pickups){
    	
    	this.dropLocation = dropLocation;
    	this.pickups = pickups;
    }
    
    /**
     * Get method for the drop location.
     * 
     * @return the location object that describes the location where the items
     * 			must be delivered to
     */
    public Location getDropLocation(){
    	
    	return this.dropLocation;
    }
    
    /**
     * Set method for the drop location.
     * 
     * @param location the new drop location to be set for the job
     */
    public void setDropLocation(Location location){
    	
    	this.dropLocation = location;
    }
    
    /**
     * Get method for the linked list of item pickups.
     * 
     * @return the linked list of item pickup objects
     */
    public LinkedList<ItemPickup> getPickups(){
    	
    	return this.pickups;
    }
    
    /**
     * Set method fot the linked list of item pickups
     * 
     * @param pickups the new linked list of pickup objects
     */
    public void setPickups(LinkedList<ItemPickup> pickups){
    	
    	this.pickups = pickups;
    }
    
    /**
     * To String method for debugging.
     */
    public String toString(){
    	
    	return "Job of pickups :" + this.pickups.toString() 
    		+ " to location " + this.dropLocation.toString();
    }
}
