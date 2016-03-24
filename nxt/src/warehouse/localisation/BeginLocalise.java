package warehouse.localisation;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import warehouse.util.Direction;


/**
 * The class which is called when the robot is lost
 * only given the heading before he lost
 * Starts robot localisation and sends an event when finishes
 * @author jokLiu
 */
public class BeginLocalise {
	
	/**
	 * 
	 *
	 * @param d 
	 */
	public void begin(Direction d)
	{
		// The actual map of the warehouse
		GridMap map = MapUtils.createMarkingWarehouseMap();
		
		//line following robot
		LineFollowTest lineFollower = new LineFollowTest();
	
		Heading heading = getHeading(d);
		//pose of the grid
		//initialy heading is set to PLUS_X direction
		GridPose gridStart = new GridPose(0,0, heading);
		
		//object of the distances in the map
		Distances dist = new Distances(map);
		
		//Actual localisation 
		LocalistaionMain ml = new LocalistaionMain(map, gridStart, dist,lineFollower);
		ml.run();
	}
	
	/**
	 * Gets the heading
	 * Team used own created class Direction
	 * so it is necessary to change it to Heading
	 * which is used for robot
	 *
	 * @param d the d
	 * @return the heading
	 */
	public Heading getHeading(Direction d)
	{
		if(d.equals(Direction.NORTH))
			return Heading.PLUS_Y;
		else if(d.equals(Direction.SOUTH))
			return Heading.MINUS_Y;
		else if(d.equals(Direction.EAST))
			return Heading.PLUS_X;
		else
			return Heading.MINUS_X;
		
	}

}
