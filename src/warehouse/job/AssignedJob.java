package warehouse.job;

import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Robot;
import warehouse.util.Route;

import java.util.LinkedList;

<<<<<<< 6151dbf7c50839293b15a8b598ee74b6a4781bd9
=======
/**
 * Created by samtebbs on 22/02/2016.
 * 
 * Modified by Owen on 25/02/2016:
 * 
 * - Added constructor to intialise fields
 * - Made fields private and added get method for robot
 * - Added overriding toString method for debugging
 * - Added javadocs
 */
>>>>>>> Added javadocs in the classes I made/modified
public class AssignedJob extends Job {

    private Route route;
    private Robot robot;

<<<<<<< 6151dbf7c50839293b15a8b598ee74b6a4781bd9
    public AssignedJob(Location dropLocation, LinkedList<ItemPickup> pickups, Route route, Robot robot) {
        super(dropLocation, pickups);
        this.route = route;
        this.robot = robot;
    }

    public AssignedJob(Job job, Route route, Robot robot) {
        this(job.dropLocation, job.pickups, route, robot);
=======
    /**
     * Create an assigned job object from an existing job object and a robot,
     * when that job is assigned to the robot from the job selector.
     * 
     * @param job the job which has been assigned to the robot
     * @param robot the robot which the job has been assigned to
     */
    public AssignedJob(Job job, Robot robot){
    	
    	super(job.getDropLocation(), job.getPickups());
    	
    	this.robot = robot;
    	
    	//TODO Get route?
>>>>>>> Added javadocs in the classes I made/modified
    }
    
    /**
     * Get method for the robot doing the assigned job.
     * 
     * @return the robot that the job has been assigned to
     */
    public Robot getRobot(){
    	
    	return this.robot;
    }
    
    /**
     * To String method for debugging.
     */
    @Override
    public String toString(){
    	
    	return "Job of pickups :" + this.getPickups().toString() + 
    			" to location " + this.getDropLocation().toString() +
    			 " assigned to " + this.robot.getName();
    }
}
