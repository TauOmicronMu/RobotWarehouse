package warehouse.route_execution;

import warehouse.action.DropoffAction;
import warehouse.action.IdleAction;
import warehouse.action.MoveAction;
import warehouse.action.PickupAction;
import warehouse.action.TurnAction;
import warehouse.event.ActionCompleteEvent;
import warehouse.event.JobAssignedEvent;
import warehouse.util.EventDispatcher;
import warehouse.util.Robot;
import warehouse.util.Route;
import warehouse.util.Subscriber;

/**
 * Class that receives the route and sees what type of actions a robot needs to execute
 * @author Gabriel
 *
 */
public class RouteExecution {

	private Route route;
	private LineFollow lineFollower;

	  static {
	        EventDispatcher.subscribe2(RouteExecution.class);
	    }

	    public RouteExecution() {
	        EventDispatcher.subscribe2(this);
	        lineFollower = new LineFollow();
	    }
	
/**
  * Waits for a new job to be assigned and calls the corresponding methods depending on the type of action the robot needs to execute
  *
  */
	    
	@Subscriber
	public void onJobAssignment(JobAssignedEvent jobEvent)
	{
		route = jobEvent.assignedJob.route;
		Robot robot = jobEvent.assignedJob.robot;
		for(warehouse.action.Action a: route.actions){
			if(a instanceof MoveAction){
				lineFollower.moveAction(((MoveAction) a).distance);
				EventDispatcher.onEvent2(new ActionCompleteEvent(robot,a));
			} 
                        else if(a instanceof TurnAction){
				lineFollower.turnAction(((TurnAction) a).angle);
				EventDispatcher.onEvent2(new ActionCompleteEvent(robot,a));
			} 
                        else if(a instanceof PickupAction){
				lineFollower.pickupAction(((PickupAction) a).pickup,robot);
				EventDispatcher.onEvent2(new ActionCompleteEvent(robot,a));
			} 
                        else if(a instanceof DropoffAction){
				lineFollower.dropoffAction(jobEvent.assignedJob);
				EventDispatcher.onEvent2(new ActionCompleteEvent(robot,a));
			} 
                        else {
				lineFollower.idleAction(((IdleAction)a).time);
				EventDispatcher.onEvent2(new ActionCompleteEvent(robot,a));
			}
		}
	}
}
