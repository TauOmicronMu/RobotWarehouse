package warehouse.localisation;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.navigation.DifferentialPilot;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.navigation.GridPose;

public class MainClass {

	public void beginLocalise() {
		// Work on this map
		GridMap map = MapUtils.createMarkingWarehouseMap();

		DifferentialPilot pilot = new DifferentialPilot(0.056, 0.12, Motor.B, Motor.C);
		LightSensor left = new LightSensor(SensorPort.S1);
		LightSensor right = new LightSensor(SensorPort.S2);
		OpticalDistanceSensor infraRed = new OpticalDistanceSensor(SensorPort.S3);
		Part2_LineFollow lineFollower = new Part2_LineFollow(pilot, left, right);
		
		

		GridPose gridStart = new GridPose();


		Distances dist = new Distances(map);
		LocalistaionMain ml = new LocalistaionMain(map, gridStart, dist,lineFollower,
				infraRed, pilot);
	}
}
