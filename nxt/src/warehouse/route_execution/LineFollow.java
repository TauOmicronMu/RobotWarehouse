package warehouse.route_execution;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import warehouse.event.DropOffReachedEvent;
import warehouse.event.JobCompleteEvent;
import warehouse.event.PickupCompleteEvent;
import warehouse.event.PickupReachedEvent;
import warehouse.job.AssignedJob;
import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;
import warehouse.util.Robot;
import warehouse.util.Subscriber;

/**
 * Class that makes the robot perform various types of actions
 * 
 * @author Gabriel Iuriciuc
 *
 */

public class LineFollow {

	private DifferentialPilot pilot;
	private int lengthOfMovement = 2000;
	public int leftSensorValue;
	public int rightSensorValue;
	//changes travel speed
	private final Float travelSpeed = 0.18f;


	private int delay=50;
	private boolean pickUp =true;
	private boolean dropOff =true;
	
	public LineFollow(){
		
		EventDispatcher.subscribe2(this);
		pilot = new DifferentialPilot(0.056, 0.12, Motor.B, Motor.C);
		delay = 50;
		pilot.setTravelSpeed(travelSpeed);
		LightSensor right = new LightSensor(SensorPort.S2);
		LightSensor left = new LightSensor(SensorPort.S3);
		right.setFloodlight(true);
		left.setFloodlight(true);
		
		//setting the maximum and minimum values that the sensor can read for better behavior
		int high = 44; 
		int low = 20;
		
		left.setHigh(high);
		right.setHigh(high);
		left.setLow(low);
		right.setLow(low);
		
		LSListener ls1Listener = new LSListener(this , right ,left);
		ls1Listener.start();
	}
	
	 /**
	  * Makes the robot move for a given number of junctions with the help of a line follower
	  *
	  */
	 public void moveAction(int distance){
			while (distance > 0) {
				if(leftSensorValue < 1500 && rightSensorValue < 1500){
					distance--;
					pilot.stop();
				}else{
					pilot.steer((leftSensorValue - rightSensorValue)/4);
					Delay.msDelay(delay);
				}
				Delay.msDelay(delay);
			}
		}
	
	 
	 /**
	  * Makes the robot perform a turning action given a certain angle
	  *
	  */
	public void turnAction(double angle){
		pilot.travel(0.03); //distance to travel before turning
		pilot.stop();
		pilot.rotate(angle);
	}

	/**
	 * Makes the robot wait for a certain amount of time
	 *
	 */
	public void idleAction(int time) {
		while(time > 0)
		{
			Delay.msDelay(lengthOfMovement);//waits as long as a movement from 1 junction to another would take
			time--;
		}
	}
	
	/**
	 * Lets the interface know that the robot is at a drop off location with a certain job completed. Also it is making sure that the action is actually completed.
	 *
	 */
	public void dropoffAction(AssignedJob job) {
		EventDispatcher.onEvent2(new DropOffReachedEvent("Robot at drop-off location.", job));
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
	
	
	/**
	 * Lets the interface know that the robot is ready to pickup an item. Also it is making sure that the action is actually completed.
	 *
	 */
	public void pickupAction(ItemPickup pickup, Robot robot) {
		EventDispatcher.onEvent2(new PickupReachedEvent(pickup,robot));
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
	
}
