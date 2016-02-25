package warehouse;

/**
 * Created by samtebbs on 22/02/2016.
 * 
 * Modified by Owen on 25/02/2016:
 * 
 * - Added boolean 'busy' field for use by job selector
 * - Added set method for the busy boolean flag
 * - Added constructor to initialise fields
 * - Made fields private and added get methods
 * - Added toString method for debugging
 * - Added javadocs
 */
public class Robot {

    private String robotName;
    private Location position;
    private boolean busy;
    
    /**
     * Create a robot object which contains a name, position and busy boolean flag.
     * 
     * @param name the name of this robot
     */
    public Robot(String name){
    	
    	this.robotName = name;
    	this.busy = false;
    }
    
    /**
     * Get method for the robot name.
     * 
     * @return the name of this robot
     */
    public String getName(){
    	
    	return this.robotName;
    }
    
    /**
     * Get method for the current position of the robot.
     * 
     * @return the location object that describes the current position of the robot
     */
    public Location getPosition(){
    	
    	return this.position;
    }
    
    /**
     * Get method for the busy flag on the robot.
     * 
     * @return true if the robot is busy
     */
    public boolean getBusy(){
    	
    	return this.busy;
    }
    
    /**
     * Set method to change the busy flag on the robot.
     * 
     * @param bool the new value of busy 
     */
    public void setBusy(boolean bool){
    	
    	this.busy = bool;
    }
    
    /**
     * To String method for debugging.
     */
    public String toString(){
    	
    	return "Robot: " + this.getName() + " - busy:" + this.getBusy();
    }
}
