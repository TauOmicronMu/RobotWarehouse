package warehouse.route_execution_test;


import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

/**
 * Class performs tests if the robot actually executes the given actions.
 * 
 * @author Gabriel Iuriciuc
 *
 */
public class LineFollowTest {

	private DifferentialPilot pilot;
	public int leftSensorValue;
	public int rightSensorValue;
	//changes travel speed
	private final Float travelSpeed = 0.18f;

	private int delay=50;
	
	public LineFollowTest(){
		pilot = new DifferentialPilot(0.056, 0.12, Motor.B, Motor.C);
		delay = 50;
		pilot.setTravelSpeed(travelSpeed);
		LightSensor right = new LightSensor(SensorPort.S2);
		LightSensor left = new LightSensor(SensorPort.S3);
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
//			System.out.println("left"+ leftSensorValue);
//			System.out.println("right" +rightSensorValue);
			if(leftSensorValue < 1530 && rightSensorValue < 1530){
//				System.out.println("Junction");
				distance--;
				pilot.stop();
			}else{
				pilot.steer((leftSensorValue - rightSensorValue)/4);
				Delay.msDelay(delay);
			}
			Delay.msDelay(delay);
		}
	}
	
	public void turnAction(double angle){
		//distance to travel before turning
//		System.out.println("turning");
		pilot.travel(0.03);
		pilot.stop();
		pilot.rotate(angle);
	}

}

