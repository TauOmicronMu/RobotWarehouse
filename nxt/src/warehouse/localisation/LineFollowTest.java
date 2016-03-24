package warehouse.localisation;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

/**
 * Class that enables a NXT Robot to follow a line.
 * 
 * @author Team E1
 *
 */
public class LineFollowTest {

	private DifferentialPilot pilot;
	public int leftSensorValue;
	public int rightSensorValue;
	private UltrasonicSensor distanceSensor;
	//changes travel speed
	private final int numb = 1500;
	private final Float travelSpeed = 0.18f;

	private int delay=50;
	
	public LineFollowTest(){
		pilot = new DifferentialPilot(0.056, 0.12, Motor.B, Motor.C);
		delay = 50;
		pilot.setTravelSpeed(travelSpeed);
		LightSensor right = new LightSensor(SensorPort.S2);
		LightSensor left = new LightSensor(SensorPort.S3);
		distanceSensor = new UltrasonicSensor(SensorPort.S1);
		right.setFloodlight(true);
		left.setFloodlight(true);
		int high = 44;
		int low = 20;
		
		left.setHigh(high);
		right.setHigh(high);
		left.setLow(low);
		right.setLow(low);
		
		LSlListenerTest ls1Listener = new LSlListenerTest(this , right ,left);
		ls1Listener.start();
	}
	
	public void moveAction(int distance){
		while (distance > 0) {

			if(leftSensorValue < numb && rightSensorValue < numb){

				distance--;
				pilot.stop();
			}else{
				pilot.steer((leftSensorValue - rightSensorValue)/4);
				Delay.msDelay(delay);
			}
			Delay.msDelay(delay);
		}
		pilot.travel(0.02f);
	}
	
	public double getRange()
	{
		return distanceSensor.getDistance();
	}
	
	public void turnAction(double angle){
		//distance to travel before turning
		pilot.travel(0.03f);
		pilot.stop();
		pilot.rotate(angle);
	}

}

