package warehouse.route_execution;

import java.util.LinkedList;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import warehouse.action.Action;
import warehouse.action.MoveAction;
import warehouse.action.TurnAction;
import warehouse.job.AssignedJob;
import warehouse.util.EventDispatcher;
import warehouse.util.Route;

public class Test {
	
	public static void main(String[] args) { 
		DifferentialPilot pilot = new DifferentialPilot(0.056, 0.12, Motor.B, Motor.C);
		LinkedList<Action> actions = new LinkedList<Action>();
		actions.add(new MoveAction(2,null));
		actions.add(new TurnAction(90));
		actions.add(new MoveAction(1,null));
		actions.add(new TurnAction(-90));
		actions.add(new MoveAction(1,null));
		Route route = new Route(actions, null,null, null);
		AssignedJob Job = new AssignedJob(null,route,null); 
		RouteExecution rExecution = new RouteExecution();
		EventDispatcher.onEvent2(new AssignedJob(Job, route, null));

	}

}
