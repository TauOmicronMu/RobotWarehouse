package localisation;

import javax.swing.JFrame;

import lejos.util.Delay;
import rp.robotics.LocalisedRangeScanner;
import rp.robotics.MobileRobotWrapper;
import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.localisation.SensorModel;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.GridPilot;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.simulation.MovableRobot;
import rp.robotics.simulation.SimulatedRobots;
import rp.robotics.testing.TestMaps;
import rp.robotics.visualisation.GridPositionDistributionVisualisation;
import rp.robotics.visualisation.KillMeNow;
import rp.robotics.visualisation.MapVisualisationComponent;
import rp.systems.StoppableRunnable;

public class LocalistaionMain implements StoppableRunnable {

	// The map used as the basis of behaviour
	private final GridMap m_map;

	// Probability distribution over the position of a robot on the given
	// grid map. Note this assumes that the robot has a known heading.
	private GridPositionDistribution m_distribution;

	// The visualisation showing position uncertainty
	private GridPositionDistributionVisualisation m_mapVis;

	// The pilot object used to move the robot around on the grid.
	private final GridPilot m_pilot;

	// The range scanning sensor
	private LocalisedRangeScanner m_ranger;

	private boolean m_running = true;

	private Distances dist;

	public LocalistaionMain(MovableRobot _robot, GridMap _gridMap, GridPose _start, LocalisedRangeScanner _ranger,
			Distances dist) {

		m_map = _gridMap;
		m_pilot = new GridPilot(_robot.getPilot(), _gridMap, _start);
		m_ranger = _ranger;
		m_distribution = new GridPositionDistribution(m_map);
		this.dist = dist;
	}

	@Override
	public void stop() {
		m_running = false;
	}

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
		long delay = 1000;

		Heading heading = m_pilot.getGridPose().getHeading();

		// Move robot forward
		// NOTE: Not checking for this being a valid move, collisions etc.
		if (m_ranger.getRange() >= 0.35f) {
			m_pilot.moveForward();

			// Update estimate of position using the action model
			m_distribution = _actionModel.updateAfterMove(m_distribution, heading);
		}

		m_distribution.normalise();

		// If visualising, update the shown distribution
		if (m_mapVis != null) {
			m_mapVis.setDistribution(m_distribution);
		}

		// A delay so we can see what's going on
		Delay.msDelay(delay);

		// Update the estimate of position using the sensor model
		m_distribution = _sensorModel.updateAfterSensing(m_distribution, heading, m_ranger.getRangeValues());

		m_distribution.normalise();
		// If visualising, update the shown distribution
		if (m_mapVis != null) {
			m_mapVis.setDistribution(m_distribution);
		}

		// A delay so we can see what's going on
		Delay.msDelay(delay);
	}

	private void sense(SensorModel _sensorModel) {
		Heading heading = m_pilot.getGridPose().getHeading();
		// Update the estimate of position using the sensor model
		m_distribution = _sensorModel.updateAfterSensing(m_distribution, heading, m_ranger.getRangeValues());

		m_distribution.normalise();
		// If visualising, update the shown distribution
		if (m_mapVis != null) {
			m_mapVis.setDistribution(m_distribution);
		}

		// A delay so we can see what's going on
		Delay.msDelay(1000);
	}

	/**
	 * Move the robot around the map. This is just a dummy behaviour tied
	 * directly to the warehouse map.
	 */
	public void run() {

		// These models don't actually do anything...
		ActionModel actionModel = new ActionModelModified();
		SensorModel sensorModel = new SensorModelModified(dist);

		int horizontal = 4;
		int vertical = 5;

		int moves;

		//

		while (m_running) {

			
			moves = vertical;
			for (int i = 0; i < moves; i++) {
				move(actionModel, sensorModel);
			}

			// Note we don't update after rotations, but you probably should as
			// at least the sensor readings will change
			m_pilot.rotateNegative();

			moves = horizontal;
			for (int i = 0; i < moves; i++) {
				move(actionModel, sensorModel);
			}

			m_pilot.rotateNegative();

			moves = vertical + 1;
			for (int i = 0; i < moves; i++) {
				move(actionModel, sensorModel);
			}

			m_pilot.rotateNegative();

			moves = horizontal;
			for (int i = 0; i < moves; i++) {
				move(actionModel, sensorModel);
			}

			m_pilot.rotateNegative();

		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

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
		LocalistaionMain ml = new LocalistaionMain(wrapper.getRobot(), map, gridStart, ranger, dist);
		ml.visualise(sim);
		ml.run();

	}

}