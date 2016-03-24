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
 *
 */
public class LineFollowTest {

	/** The pilot. */
	private DifferentialPilot pilot;
	
	/** The left sensor value. */
	public int leftSensorValue;
	
	/** The right sensor value. */
	public int rightSensorValue;
	
	/** The distance sensor. */
	private UltrasonicSensor distanceSensor;
	
	/** The constant of sensor measurments */
	private final int numb = 1500;
	
	/** The travel speed. */
	private final Float travelSpeed = 0.18f;

	/** The delay. */
	private int delay=50;
	
	/**
	 * Instantiates a new line follow test.
	 */
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
	
	/**
	 * Move action
	 *
	 * @param distance the distance- how many steps in the map to move
	 */
	public void moveAction(int distance){
		while (distance > 0) {

			//if both sensors are on the black tape then decrease distance by one
			//robot moved one step
			if(leftSensorValue < numb && rightSensorValue < numb){

				distance--;
				pilot.stop();
			}else{
				//else use a feedback control and correct robots direction
				pilot.steer((leftSensorValue - rightSensorValue)/4);
				Delay.msDelay(delay);
			}
			Delay.msDelay(delay);
		}
		pilot.travel(0.02f);
	}
	
	/**
	 * Gets the range from the range scanner
	 *
	 * @return the range
	 */
	public double getRange()
	{
		return distanceSensor.getDistance();
	}
	
	/**
	 * Turn action turn the robot at a particular angle
	 *
	 * @param angle the angle
	 */
	public void turnAction(double angle){
		//distance to travel before turning
		pilot.travel(0.03f);
		pilot.stop();
		pilot.rotate(angle);
	}

}

