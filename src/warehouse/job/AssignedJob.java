package warehouse.job;

import warehouse.Robot;
import warehouse.Route;

/**
 * Created by samtebbs on 22/02/2016.
 */
public class AssignedJob extends Job {

    private Route route;
    private Robot robot;

    public AssignedJob(Job job, Robot robot){
    	
    	super(job.getDropLocation(), job.getPickups());
    	
    	this.robot = robot;
    	
    	//TODO Get route?
    }
}
