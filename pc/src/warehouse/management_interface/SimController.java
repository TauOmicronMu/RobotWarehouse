package warehouse.management_interface;

/**
 * Controller for the simulation.
 *  Moves/stops based on events that come from the server
 *  Inspired by Nick's RandomGridWalk
 * 
 * @author aml
 *
 */

import lejos.robotics.RangeFinder;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.GridPilot;
import rp.robotics.navigation.GridPose;
import rp.robotics.simulation.MovableRobot;
import rp.systems.StoppableRunnable;
import warehouse.action.Action;
import warehouse.action.DropoffAction;
import warehouse.action.MoveAction;
import warehouse.action.PickupAction;
import warehouse.action.TurnAction;
import warehouse.event.ActionCompleteEvent;
import warehouse.util.EventDispatcher;
import warehouse.util.Subscriber;

public class SimController implements StoppableRunnable {

	private final GridMap map;
	private final GridPilot pilot;

	private boolean running = true;
	private final RangeFinder ranger;
	private final MovableRobot robot;
	private String myName;

	public SimController(MovableRobot _robot, GridMap _map, GridPose _start, RangeFinder _ranger, String myName) {
		map = _map;
		pilot = new GridPilot(_robot.getPilot(), _map, _start);
		ranger = _ranger;
		robot = _robot;
		EventDispatcher.subscribe2(this);
		this.myName = myName;
	}

	/**
	 * Is there enough space in front of the robot to move into?
	 * 
	 * @return
	 */
	private boolean enoughSpace() {
		return ranger.getRange() > map.getCellSize() + robot.getRobotLength() / 2f;
	}

	/**
	 * Is the grid junction ahead really a junction and clear of obstructions?
	 * 
	 * @return
	 */
	private boolean moveAheadClear() {
		GridPose current = pilot.getGridPose();
		GridPose moved = current.clone();
		moved.moveUpdate();
		return map.isValidTransition(current.getPosition(), moved.getPosition()) && enoughSpace();
	}

	@Override
	public void run() {

		while (running) {
			// Nothing to do here. Just looping and listening for events using
			// methods below
			
		}
	}
	
	/**
	 * Listen to action events and perform the action if 
	 * the robot name is the same as mine
	 * @param e the event
	 */
	@Subscriber
	public void onActionCompleteEvent(ActionCompleteEvent e) {
		if (e.robot.get().robotName.equals(myName)) {
			Action action = e.action;
			
			if (action instanceof MoveAction) 
				moveAhead(((MoveAction) action).speed);
			
			else if (action instanceof TurnAction) {
				if (((TurnAction) action).angle == 90)
					turnLeft();
				else turnRight();
			}
			
			else if (action instanceof DropoffAction || action instanceof PickupAction)
				stopRobot();
				
			
		}
		
	}

	/**
	 * Below are methods that are called by the event listener,
	 * in order to make robots perform actions
	 */

	
	public void moveAhead(double speed) {
		if (moveAheadClear()) {
			pilot.setTravelSpeed((float) speed);
			pilot.moveForward();
		}
	}

	
	public void stopRobot() {
		pilot.setTravelSpeed(0f);
	}

	
	public void turnRight() {

		pilot.rotateNegative();
	}

	
	public void turnLeft() {

		pilot.rotatePositive();
	}

	
	public void stop() {
		running = false;
	}

}
