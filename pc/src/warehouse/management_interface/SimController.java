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

public class SimController implements StoppableRunnable {

	private final GridMap map;
	private final GridPilot pilot;

	private boolean running = true;
	private final RangeFinder ranger;
	private final MovableRobot robot;

	public SimController(MovableRobot _robot, GridMap _map, GridPose _start, RangeFinder _ranger) {
		map = _map;
		pilot = new GridPilot(_robot.getPilot(), _map, _start);
		ranger = _ranger;
		robot = _robot;
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
	 * Below are methods that listen for events related to robots' actions They
	 * are self-explanatory so I won't document each one of them
	 */

	public void onMoveAheadEvent() {
		if (moveAheadClear()) {
			pilot.moveForward();
		}
	}

	public void onStoppedEvent() {
		// TODO
	}

	public void onTurnRightEvent() {
		pilot.rotateNegative();
	}

	public void onTurnLeftEvent() {
		pilot.rotatePositive();
	}

	@Override
	public void stop() {
		running = false;
	}

}
