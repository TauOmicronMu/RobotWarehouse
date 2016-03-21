package warehouse.localisation;
import rp.robotics.LocalisedRangeScanner;
import rp.robotics.MobileRobotWrapper;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.simulation.MovableRobot;
import rp.robotics.simulation.SimulatedRobots;
import rp.robotics.testing.TestMaps;

public class MainClass {
	
	public MainClass()
	{
		
	}

	public void startLocalisation()
	{
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
//		ml.visualise(sim);
//		ml.run();
	}
}
