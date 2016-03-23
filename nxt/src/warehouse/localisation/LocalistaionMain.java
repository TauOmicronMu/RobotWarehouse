package warehouse.localisation;

<<<<<<< HEAD

import java.util.Random;

import lejos.nxt.Button;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
=======
import java.util.Random;

import javax.swing.JFrame;

import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;
import lejos.util.Delay;
import rp.robotics.LocalisedRangeScanner;
import rp.robotics.MobileRobotWrapper;
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.localisation.SensorModel;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
<<<<<<< HEAD
import rp.systems.StoppableRunnable;


=======
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.simulation.MovableRobot;
import rp.robotics.simulation.SimulatedRobots;
import rp.robotics.testing.TestMaps;
import rp.robotics.visualisation.GridPositionDistributionVisualisation;
import rp.robotics.visualisation.KillMeNow;
import rp.robotics.visualisation.MapVisualisationComponent;
import rp.systems.StoppableRunnable;
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
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
<<<<<<< HEAD
//	private GridPositionDistributionVisualisation m_mapVis;

	// The pilot object used to move the robot around on the grid.
	private final GridPilotModified m_pilot;
//
//	// The range scanning sensor
//	private LocalisedRangeScanner m_ranger;
=======
	private GridPositionDistributionVisualisation m_mapVis;

	// The pilot object used to move the robot around on the grid.
	private final GridPilotModified m_pilot;

	// The range scanning sensor
	private LocalisedRangeScanner m_ranger;
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f

	private boolean m_running = true;

	// distances to the every object in the map
	private Distances dist;
<<<<<<< HEAD
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
=======
	
	
	LineFollowingL lineFollow;

	public LocalistaionMain(MovableRobot _robot, GridMap _gridMap, GridPose _start, LocalisedRangeScanner _ranger,
			Distances dist, LineFollowingL lineFollow) {

		this.lineFollow= lineFollow;
		m_map = _gridMap;
		m_pilot = new GridPilotModified(_robot.getPilot(), _gridMap, _start);
		m_ranger = _ranger;
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
		m_distribution = new GridPositionDistribution(m_map);
		this.dist = dist;
	}

	@Override
	public void stop() {
		m_running = false;
	}

<<<<<<< HEAD
	
=======
	/**
	 * Optionally run the visualisation of the robot and localisation process.
	 * This is not necessary to run the localisation and could be removed once
	 * on the real robot.
	 * 
	 * @param _sim
	 */
	public void visualise(MapBasedSimulation _sim) {

		JFrame frame = new JFrame("Map Viewer");
		frame.addWindowListener(new KillMeNow());

		// visualise the distribution on top of a line map
		m_mapVis = new GridPositionDistributionVisualisation(m_distribution, m_map);
		MapVisualisationComponent.populateVisualisation(m_mapVis, _sim);

		frame.add(m_mapVis);
		frame.pack();
		frame.setSize(m_mapVis.getMinimumSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f

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

<<<<<<< HEAD
		
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


=======
		// Move robot forward
		// NOTE: Not checking for this being a valid move, collisions etc.
//		if (m_ranger.getRange() >= 0.35f) {
//			m_pilot.moveForward();
//			
//			// Update estimate of position using the action model
//			m_distribution = _actionModel.updateAfterMove(m_distribution, heading);
//		}
		if (lineFollow.getRange() >= 0.35f) {
			lineFollow.moveAction(1);
			m_pilot.moveForward();
			
			// Update estimate of position using the action model
			m_distribution = _actionModel.updateAfterMove(m_distribution, heading);
		}
		
		m_distribution.normalise();

		// If visualising, update the shown distribution
		if (m_mapVis != null) {
			m_mapVis.setDistribution(m_distribution);
		}
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f

		// A delay so we can see what's going on
		Delay.msDelay(delay);

		// Update the estimate of position using the sensor model
<<<<<<< HEAD
		RangeReadings readings = new RangeReadings(1);
		readings.set(0, new RangeReading(0f, infraRed.getRange()));
		m_distribution = _sensorModel.updateAfterSensing(m_distribution, heading, readings);

		m_distribution.normalise();
=======
		RangeReadings readings  = new RangeReadings(1);
		readings.set(0, new RangeReading(0f, (float) lineFollow.getRange()));
		m_distribution = _sensorModel.updateAfterSensing(m_distribution, heading, readings);

		m_distribution.normalise();
		// If visualising, update the shown distribution
		if (m_mapVis != null) {
			m_mapVis.setDistribution(m_distribution);
		}
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f

		// A delay so we can see what's going on
		Delay.msDelay(delay);
	}

	private void sense(SensorModel _sensorModel) {
		Heading heading = m_pilot.getGridPose().getHeading();
		// Update the estimate of position using the sensor model
<<<<<<< HEAD
		RangeReadings readings = new RangeReadings(1);
		readings.set(0, new RangeReading(0f, infraRed.getRange()));
		m_distribution = _sensorModel.updateAfterSensing(m_distribution, heading, readings);

		m_distribution.normalise();
=======
		RangeReadings readings  = new RangeReadings(1);
		readings.set(0, new RangeReading(0f, (float) lineFollow.getRange()));
		m_distribution = _sensorModel.updateAfterSensing(m_distribution, heading, readings);

		m_distribution.normalise();
		// If visualising, update the shown distribution
		if (m_mapVis != null) {
			m_mapVis.setDistribution(m_distribution);
		}

		// A delay so we can see what's going on
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
		Delay.msDelay(100);
	}

	/**
	 * Move the robot around the map. This is just a dummy behaviour tied
	 * directly to the warehouse map.
	 */
<<<<<<< HEAD
	@Override
=======
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
	public void run() {

		// These models don't actually do anything...
		ActionModel actionModel = new ActionModelModified();
		SensorModel sensorModel = new SensorModelModified(dist);
		Random random = new Random();

		m_pilot.rotateNegative();
<<<<<<< HEAD
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
=======
		sense(sensorModel);

		m_pilot.rotateNegative();
		sense(sensorModel);

		m_pilot.rotateNegative();
		sense(sensorModel);

		m_pilot.rotateNegative();
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
		sense(sensorModel);

		while (m_running) {

			if (!knownCoordinates()) {
<<<<<<< HEAD
				if (infraRed.getRange() >= 0.f) {
					move(actionModel, sensorModel);
				}

=======
				if (m_ranger.getRange() >= 0.4f){
					move(actionModel, sensorModel);
				}
					
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
				else {
					int temp = random.nextInt(2);
					switch (temp) {
					case 0:
						m_pilot.rotateNegative();
<<<<<<< HEAD
						pilot.rotate(-90);
=======
						lineFollow.turnAction(-90);
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
						Delay.msDelay(1000);
						sense(sensorModel);
						break;
					case 1:
						m_pilot.rotatePositive();
<<<<<<< HEAD
						pilot.rotate(90);
=======
						lineFollow.turnAction(90);
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
						Delay.msDelay(1000);
						sense(sensorModel);
						break;
					}
				}
			}

		}

	}

	private boolean knownCoordinates() {
<<<<<<< HEAD

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
=======
		for (int x = 0; x < m_map.getXSize(); x++) {
			for (int y = 0; y < m_map.getYSize(); y++) {
				if (m_distribution.getProbability(x, y) >= 0.7f) {
					System.out.println("x: " + x + " // y: " + y);
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
					m_running = false;
					return true;
				}
			}
		}
		return false;
	}

<<<<<<< HEAD
	
=======
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		LineFollowingL lineFollow = new LineFollowingL();
		// Work on this map
		GridMap map = TestMaps.warehouseMap();

		// Create the simulation using the given map. This simulation can run
		// without a GUI.
		MapBasedSimulation sim = new MapBasedSimulation(map);

		// the starting position of the robot for the simulation. This is not
		// known in the action model or position distribution
		int startGridX = 2;
		int startGridY = 1;

		GridPose gridStart = new GridPose(startGridX, startGridY, Heading.PLUS_Y);

		// Create a robot with a range scanner but no bumper
		MobileRobotWrapper<MovableRobot> wrapper = sim.addRobot(SimulatedRobots.makeConfiguration(false, true),
				map.toPose(gridStart));
		LocalisedRangeScanner ranger = sim.getRanger(wrapper);

		Distances dist = new Distances(map);
		LocalistaionMain ml = new LocalistaionMain(wrapper.getRobot(), map, gridStart, ranger, dist, lineFollow);
		ml.visualise(sim);
		ml.run();

	}
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f

}