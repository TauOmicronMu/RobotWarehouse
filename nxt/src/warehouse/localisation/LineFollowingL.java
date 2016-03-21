package warehouse.localisation;


import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

public class LineFollowingL  {

	private DifferentialPilot pilot;
	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	private boolean goLeft = false;
	private boolean goRight = false;
	
	//changes travel speed
	private final int travelSpeed = 10;

	private int delay;
	
	UltrasonicSensor distanceSensor;
	public LineFollowingL(){
		pilot = new DifferentialPilot(0.056, 0.12, Motor.B, Motor.C);
		delay = 50;
		pilot.setTravelSpeed(travelSpeed);
		
		LightSensor ls1 = new LightSensor(SensorPort.S2);
		ls1.setFloodlight(true);
		LSListener ls1Listener = new LSListener(this, ls1, RIGHT);
		ls1Listener.setBackgroundValue(ls1.getLightValue());
		ls1Listener.start();
		
		LightSensor ls2 = new LightSensor(SensorPort.S3);
		ls2.setFloodlight(true);
		LSListener ls2Listener = new LSListener(this, ls2, RIGHT);
		ls2Listener.setBackgroundValue(ls2.getLightValue());
		ls2Listener.start();
		
		distanceSensor = new UltrasonicSensor(SensorPort.S4);
		
	}
	
	public void moveAction(int distance){
		while (distance > 0) {
			if (goLeft) {
				Delay.msDelay(delay);
				if (goRight) {
					distance--;					
				} else {
					pilot.stop();
					while (goLeft) {
						pilot.rotate(-5);
					}
				}
			} else if (goRight) {
				Delay.msDelay(delay);
					pilot.stop();
					while (goRight) {
						pilot.rotate(5);
					}
			}
			pilot.forward();
		}
	}
	
	public double getRange()
	{
		return distanceSensor.getRange();
	}
	public void turnAction(double angle){
		//distance to travel before turning
		pilot.travel(0.06);
		
		pilot.stop();
		pilot.rotate(angle);
	}

	public void setGoLeft(boolean goLeft) {
		this.goLeft = goLeft;
	}

	public void setGoRight(boolean goRight) {
		this.goRight = goRight;
	}
}
