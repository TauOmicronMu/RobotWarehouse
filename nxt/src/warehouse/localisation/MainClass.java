package warehouse.localisation;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.navigation.GridPose;

public class MainClass {

	public static void main(String[] args) {
		// The actual map of the warehouse
		GridMap map = MapUtils.createMarkingWarehouseMap();
		
		//line following robot
		LineFollowTest lineFollower = new LineFollowTest();
		
		//pose of the grid
		//initialy heading is set to PLUS_X direction
		GridPose gridStart = new GridPose();
		
		//object of the distances in the map
		Distances dist = new Distances(map);
		
		//Actual localisation 
		LocalistaionMain ml = new LocalistaionMain(map, gridStart, dist,lineFollower);
		ml.run();
	}
}
