package warehouse.localisation;


import java.util.Random;

import lejos.nxt.Button;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.localisation.SensorModel;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.systems.StoppableRunnable;


/**
 * 
 * @author jokLiu
 *
 */

public class LocalistaionMain implements StoppableRunnable {

	// The map used as the basis of behaviour
	private final GridMap m_map;

	// Probability distribution over the position of a robot on the given
	// grid map. Note this assumes that the robot has a known heading.
	private GridPositionDistribution m_distribution;

	// The visualisation showing position uncertainty
//	private GridPositionDistributionVisualisation m_mapVis;

	// The pilot object used to move the robot around on the grid.
	private final GridPilotModified m_pilot;
//
//	// The range scanning sensor
//	private LocalisedRangeScanner m_ranger;

	private boolean m_running = true;

	// distances to the every object in the map
	private Distances dist;
	OpticalDistanceSensor infraRed;
	Part2_LineFollow lineFollower;
	DifferentialPilot pilot;

	public LocalistaionMain( GridMap _gridMap, GridPose _start, 
			Distances dist, Part2_LineFollow lineFollower, OpticalDistanceSensor infraRed, DifferentialPilot pilot) {

		this.infraRed = infraRed;
		this.lineFollower = lineFollower;
		this.pilot = pilot;
		m_pilot = new GridPilotModified(_gridMap, _start);
		m_map = _gridMap;
		m_distribution = new GridPositionDistribution(m_map);
		this.dist = dist;
	}

	@Override
	public void stop() {
		m_running = false;
	}

	

	/***
	 * Move the robot and update the distribution with the action and sensor
	 * models
	 * 
	 * @param _actionModel
	 * @param _sensorModel
	 */
	private void move(ActionModel _actionModel, SensorModel _sensorModel) {

		// How long to sleep between updates, just for clarity on the
		// visualisation!
		long delay = 100;

		Heading heading = m_pilot.getGridPose().getHeading();

		
		if (infraRed.getRange() >= 0.35f) {
			lineFollower.start();
			try {
				lineFollower.join();
			} catch (InterruptedException e) {
				
			
			}
			m_pilot.moveForward();

			// Update estimate of position using the action model
			m_distribution = _actionModel.updateAfterMove(m_distribution, heading);
		}

		m_distribution.normalise();



		// A delay so we can see what's going on
		Delay.msDelay(delay);

		// Update the estimate of position using the sensor model
		RangeReadings readings = new RangeReadings(1);
		readings.set(0, new RangeReading(0f, infraRed.getRange()));
		m_distribution = _sensorModel.updateAfterSensing(m_distribution, heading, readings);

		m_distribution.normalise();

		// A delay so we can see what's going on
		Delay.msDelay(delay);
	}

	private void sense(SensorModel _sensorModel) {
		Heading heading = m_pilot.getGridPose().getHeading();
		// Update the estimate of position using the sensor model
		RangeReadings readings = new RangeReadings(1);
		readings.set(0, new RangeReading(0f, infraRed.getRange()));
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

		m_pilot.rotateNegative();
		pilot.rotate(-90);
		sense(sensorModel);

		m_pilot.rotateNegative();
		pilot.rotate(-90);
		sense(sensorModel);

		m_pilot.rotateNegative();
		pilot.rotate(-90);
		sense(sensorModel);

		m_pilot.rotateNegative();
		pilot.rotate(-90);
		sense(sensorModel);

		while (m_running) {

			if (!knownCoordinates()) {
				if (infraRed.getRange() >= 0.f) {
					move(actionModel, sensorModel);
				}

				else {
					int temp = random.nextInt(2);
					switch (temp) {
					case 0:
						m_pilot.rotateNegative();
						pilot.rotate(-90);
						Delay.msDelay(1000);
						sense(sensorModel);
						break;
					case 1:
						m_pilot.rotatePositive();
						pilot.rotate(90);
						Delay.msDelay(1000);
						sense(sensorModel);
						break;
					}
				}
			}

		}

	}

	private boolean knownCoordinates() {

		for (int x = 0; x < m_map.getXSize(); x++) {
			for (int y = 0; y < m_map.getYSize(); y++) {
				if (m_distribution.getProbability(x, y) >= 0.7f) {
//					Heading heading = m_pilot.getGridPose().getHeading();
//					Direction d = Direction.NORTH;
//					switch (heading) {
//					case PLUS_X:
//						d = Direction.EAST;
//					case PLUS_Y:
//						d = Direction.NORTH;
//					case MINUS_X:
//						d = Direction.WEST;
//					case MINUS_Y:
//						d = Direction.SOUTH;
//					}
//					EventDispatcher.onEvent2(new StartingCoordinatesEvent(x, y, d));
					 System.out.println("x: " + x + " // y: " + y);
					 Button.waitForAnyPress();
					m_running = false;
					return true;
				}
			}
		}
		return false;
	}

	

}