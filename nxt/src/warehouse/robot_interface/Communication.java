package warehouse.robot_interface;


import warehouse.event.DropOffReachedEvent;
import warehouse.event.JobCancellationEvent;
import warehouse.event.JobCompleteEvent;
import warehouse.event.PickupCompleteEvent;
import warehouse.event.PickupReachedEvent;
import warehouse.event.RobotLostEvent;
import warehouse.event.RobotOffEvent;
import warehouse.job.AssignedJob;
import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;
import warehouse.util.Robot;
import warehouse.util.Subscriber;


/**
 * The class assuring the connection between the server and the robot. This is a
 * private field in RobotInterface, and it's instantiated inside the
 * constructor.
 */
public class Communication
{

	ItemPickup pickup;
	String message;
	Robot robot;
	AssignedJob job;
	boolean dropOff;

	static 
	{
        EventDispatcher.subscribe2(Communication.class);
    }
	
	public Communication()
	{
		EventDispatcher.subscribe2(this);
		pickup = null;
		dropOff = false;
	}
	
	@Subscriber
	public void onDropOff(DropOffReachedEvent e)
	{
		dropOff = true;
		if(e.robot.isPresent()){
			robot = e.robot.get();
		}
		
		job = e.job;
		EventDispatcher.onEvent2(new JobCompleteEvent(job));
	}
	
	@Subscriber
	public void onPickup(PickupReachedEvent e)
	{
		pickup = e.pickup;
	}
	
	/**
	 * Gets the message
	 * 
	 * @return The message
	 */
	public boolean atDropOff()
	{
		boolean result = dropOff;
		dropOff = false;
		return result;
	}

	/**
	 * Tells whether the robot is at a pickup point
	 * 
	 * @return whether the robot is at a pickup point
	 */
	public boolean hasJob()
	{
		return pickup != null;
	}

	/**
	 * Tells the server that the robot was turned off
	 */
	public void robotOff()
	{
		EventDispatcher.onEvent2(new RobotOffEvent(robot));
		pickup = null;
	}

	/**
	 * Tells the server that the current job was cancelled
	 */
	public void jobCancelled()
	{
		EventDispatcher.onEvent2(new JobCancellationEvent(job));
		pickup = null;
	}

	/**
	 * Tells the server that the robot is in the wrong place
	 */
	public void wrongPlace()
	{
		EventDispatcher.onEvent2(new RobotLostEvent(robot));
		pickup = null;
	}

	/**
	 * Returns the name of the items that need to be picked
	 * 
	 * @return The name of the item
	 */
	public String getItemName()
	{
		return pickup.itemName;
	}

	/**
	 * Returns the number of items that need to be picked
	 * 
	 * @return The number of items
	 */
	public int getItemNumber()
	{
		return pickup.itemCount;
	}

	/**
	 * Tells the server that the job is done
	 */
	public void jobDone()
	{
		EventDispatcher.onEvent2(new PickupCompleteEvent(robot, pickup));
		pickup = null;
	}

}
