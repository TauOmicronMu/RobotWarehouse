package warehouse.event;

import warehouse.util.Location;

/**
 * Event to send to subscribers when a job is completed.
 * 
 * @author Owen
 *
 */
public class JobCompleteEvent {

	private Location currentLocation;
	
	/**
	 * Create a new JobCompleteEvent which contains a location
	 * 
	 * @param l the location
	 */
	public JobCompleteEvent(Location l){
		
		this.currentLocation = l;
	}
	
	/**
	 * Get the location this event contains
	 * 
	 * @return the location
	 */
	public Location getLocation(){
		
		return this.currentLocation;
	}
}
