package warehouse.localisation;

import java.util.Random;

import lejos.nxt.Button;
import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import lejos.util.Delay;
import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.localisation.SensorModel;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.systems.StoppableRunnable;

// TODO: Auto-generated Javadoc
/**
 * The Class LocalistaionMain.
 *
 * @author jokLiu
 */

public class LocalistaionMain implements StoppableRunnable {

	
	// The map used as the basis of behaviour
	private final GridMap m_map;

	// Probability distribution over the position of a robot on the given
	// grid map. Note this assumes that the robot has a known heading.
	private GridPositionDistribution m_distribution;

	
	// The pilot object used to move the robot around on the grid.
	private final GridPilotModified m_pilot;

	/** The m_running. */
	private boolean m_running = true;

	// distances to the every object in the map
	private Distances dist;
	
	/** The line follower. */
	LineFollowTest lineFollower;
	

	/**
	 * Instantiates a new localistaion main.
	 *
	 * @param _gridMap the _grid map
	 * @param _start the _start
	 * @param dist the dist
	 * @param lineFollower the line follower
	 */
	public LocalistaionMain(GridMap _gridMap, GridPose _start, Distances dist, LineFollowTest lineFollower) {

		
		this.lineFollower = lineFollower;
		m_pilot = new GridPilotModified(_gridMap, _start);
		m_map = _gridMap;
		m_distribution = new GridPositionDistribution(m_map);
		this.dist = dist;
	}


	@Override
	public void stop() {
		m_running = false;
	}

	/**
	 * *
	 * Move the robot and update the distribution with the action and sensor
	 * models.
	 *
	 * @param _actionModel the _action model
	 * @param _sensorModel the _sensor model
	 */
	private void move(ActionModel _actionModel, SensorModel _sensorModel) {

		// How long to sleep between updates, just for clarity on the
		// visualisation!
		long delay = 100;

		Heading heading = m_pilot.getGridPose().getHeading();

		//if it is possible drive one step and update all the probabilities
		if (lineFollower.getRange() >= 22f) {
			lineFollower.moveAction(1);
			m_pilot.moveForward();
			Delay.msDelay(100);

			// Update estimate of position using the action model
			m_distribution = _actionModel.updateAfterMove(m_distribution, heading);
		}

		m_distribution.normalise();

		// A delay so we can see what's going on
		Delay.msDelay(delay);

//		// Update the estimate of position using the sensor model
//		RangeReadings readings = new RangeReadings(1);
//		readings.set(0, new RangeReading(0f,PLUS_X (float) lineFollower.getRange()));
//		m_distribution = _sensorModel.updateAfterSensing(m_distribution, heading, readings);
//
//		m_distribution.normalise();
//
//		// A delay so we can see what's going on
//		Delay.msDelay(delay);
	}

	/**
	 * Sense method in order to use only sensor for localising
	 *
	 * @param _sensorModel the _sensor model
	 */
	private void sense(SensorModel _sensorModel) {
		Heading heading = m_pilot.getGridPose().getHeading();
		
		// Update the estimate of position using the sensor model
		//create readings object in order to pass readings from actual robot
		RangeReadings readings = new RangeReadings(1);
		readings.set(0, new RangeReading(0f, (float) lineFollower.getRange()));
		m_distribution = _sensorModel.updateAfterSensing(m_distribution, heading, readings);

		m_distribution.normalise();
		Delay.msDelay(100);
	}

	/**
	 * Move the robot around the map. This is just a dummy behaviour tied
	 * directly to the warehouse map.
	 */
	@Override
	public void run() {

		// These models don't actually do anything...
		ActionModel actionModel = new ActionModelModified();
		SensorModel sensorModel = new SensorModelModified(dist);
		Random random = new Random();
		

		//running until the robot coordinates are found
		while (m_running) {

			if (!knownCoordinates()) {
				//if there is no wall in front drive straight
				//and update probabilities
				if (lineFollower.getRange() >= 22f) {
					move(actionModel, sensorModel);
				}
				//else rotate left
				else
				{
					m_pilot.rotatePositive();
					lineFollower.turnAction(-90);
					Delay.msDelay(100);
//					sense(sensorModel);
				}

//				else {
//					int temp = random.nextInt(2);
//					switch (temp) {
//					case 0:
//						m_pilot.rotateNegative();
//						lineFollower.turnAction(-90);
//						Delay.msDelay(100);
//						sense(sensorModel);
//						break;
//					case 1:
//						m_pilot.rotatePositive();
//						lineFollower.turnAction(90);
//						Delay.msDelay(100);
//						sense(sensorModel);
//						break;
//					}
//				}
			}

		}

	}
	
	/** The max. */
	private double max = 0;
	
	/**
	 * Checks if the robot founds its place
	 *
	 * @return true, if successful
	 */
	private boolean knownCoordinates() {

		
		//iterating through the whole map, checking if there is a probability which is greater than 0.7
		for (int x = 0; x < m_map.getXSize(); x++) {
			for (int y = 0; y < m_map.getYSize(); y++) {
				if(max < m_distribution.getProbability(x, y))
					max = m_distribution.getProbability(x, y);
				if (m_distribution.getProbability(x, y) >= 0.7f) {
					
					/*commented area is for the real robot to send an event 
					 * (coordiantes and heading of the robot)
					 * after the robot founds itself
					 */
					// Heading heading = m_pilot.getGridPose().getHeading();
					// Direction d = Direction.NORTH;
					// switch (heading) {
					// case PLUS_X:
					// d = Direction.EAST;
					// case PLUS_Y:
					// d = Direction.NORTH;
					// case MINUS_X:
					// d = Direction.WEST;
					// case MINUS_Y:
					// d = Direction.SOUTH;
					// }
					// EventDispatcher.onEvent2(new StartingCoordinatesEvent(x,
					// y, d));
					System.out.println("x: " + x + " // y: " + y);
					Button.waitForAnyPress();
					m_running = false;
					return true;
				}
			}
		}
		System.out.println(max);
		return false;
	}

}