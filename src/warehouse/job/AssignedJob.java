package warehouse.job;

import warehouse.Robot;
import warehouse.Route;

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
public class AssignedJob extends Job {

    private Route route;
    private Robot robot;

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
