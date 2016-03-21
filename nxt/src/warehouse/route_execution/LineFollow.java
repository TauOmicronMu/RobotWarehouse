package warehouse.route_execution;

import java.awt.Event;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import warehouse.event.JobCompleteEvent;
import warehouse.job.AssignedJob;
import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;
import warehouse.util.Subscriber;

/**
 * Class that enables a NXT Robot to follow a line.
 * 
 * @author Team E1
 *
 */
public class LineFollow {

	private DifferentialPilot pilot;
	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	private boolean goLeft = false;
	private boolean goRight = false;
	private int lengthOfMovement = 2000;
	
	//changes travel speed
	private final int travelSpeed = 1;

	private int delay;
	private boolean pickUp =true;
	private boolean dropOff =true;
	
	public LineFollow(){
		
		EventDispatcher.subscribe2(this);
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
	}
	
	 static {
	        EventDispatcher.subscribe2(LineFollow.class);
	 }
	
	public void moveAction(int distance, String robotname){
		while (distance > 0) {
			if (goLeft) {
				Delay.msDelay(delay);
				if (goRight) {
					distance--;
					pilot.stop();
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
			EventDispatcher.onEvent2(new MoveAheadEvent(robotname,travelSpeed));
		}
	}
	
	public void turnAction(double angle,String robotname){
		//distance to travel before turning
		pilot.travel(0.05);
		
		pilot.stop();
		pilot.rotate(angle);
		if(angle>0)
		{
			EventDispatcher.onEvent2(new TurnRightEvent(robotname));
		}
		else
		{
			EventDispatcher.onEvent2(new TurnLeftEvent(robotname));
		}
	}

	public void idleAction(int time,String robotname) {
		
		EventDispatcher.onEvent2(new RobotStoppedEvent(robotname));
		while(time > 0)
		{
			Delay.msDelay(lengthOfMovement);
			time--;
		}
	}
	
	public void dropoffAction(AssignedJob job) {
		EventDispatcher.onEvent2(new RobotStoppedEvent(job.robot.robotName));
		EventDispatcher.onEvent2(new DropOffEvent("Robot at drop-off location.", job));
		dropOff =true;
		while(dropOff)
		{
			Delay.msDelay(delay);
		}
	}
	
	@Subscriber
	public void onJobFinished(JobCompleteEvent e)
	{
		dropOff = false;
	}
	
	public void pickupAction(ItemPickup pickup, String robotname) {
		EventDispatcher.onEvent2(new RobotStoppedEvent(robotname));
		EventDispatcher.onEvent2(new PickupEvent(pickup));
		pickUp =true;
		while(pickUp)
		{
			Delay.msDelay(delay);
		}
	}
	
	@Subscriber
	public void onPickupFinished(PickupCompleteEvent e)
	{
		pickUp = false;
	}
	
	public void setGoLeft(boolean goLeft) {
		this.goLeft = goLeft;
	}

	public void setGoRight(boolean goRight) {
		this.goRight = goRight;
	}

}
