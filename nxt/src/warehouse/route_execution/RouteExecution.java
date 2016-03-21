package warehouse.route_execution;

import warehouse.action.DropoffAction;
import warehouse.action.IdleAction;
import warehouse.action.MoveAction;
import warehouse.action.PickupAction;
import warehouse.action.TurnAction;
import warehouse.event.JobAssignedEvent;
import warehouse.util.EventDispatcher;
import warehouse.util.Route;
import warehouse.util.Subscriber;

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
	
	@Subscriber
	public void onJobAssignment(JobAssignedEvent jobEvent)
	{
		route = jobEvent.getAssignedJob().route;
		for(warehouse.action.Action a: route.actions){
			if(a instanceof MoveAction){
				lineFollower.moveAction(((MoveAction) a).distance, jobEvent.getAssignedJob().robot.robotName);
			}else if(a instanceof TurnAction){
				lineFollower.turnAction(((TurnAction) a).angle,jobEvent.getAssignedJob().robot.robotName);
			}else if(a instanceof PickupAction){
				lineFollower.pickupAction(((PickupAction) a).pickup,jobEvent.getAssignedJob().robot.robotName);
			}else if(a instanceof DropoffAction){
				lineFollower.dropoffAction(jobEvent.getAssignedJob());
			}else{
				lineFollower.idleAction(((IdleAction)a).time,jobEvent.getAssignedJob().robot.robotName);
			}
		}
	}
}
