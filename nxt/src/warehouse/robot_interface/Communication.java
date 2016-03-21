package warehouse.robot_interface;

import warehouse.event.JobCancellationEvent;
import warehouse.event.RobotOffEvent;
import warehouse.event.WrongPlaceEvent;
import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;
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

	static 
	{
        EventDispatcher.subscribe2(Communication.class);
    }
	
	public Communication()
	{
		EventDispatcher.subscribe2(this);
		pickup = null;
		message = null;
	}
	
	@Subscriber
	public void onString(DropOffEvent e)
	{
		message = e.getMessage();
	}
	
	@Subscriber
	public void onPickup(ItemPickup pickup)
	{
		this.pickup = pickup;
	}
	
	/**
	 * Gets the message
	 * 
	 * @return The message
	 */
	public String getMessage()
	{
		return message;
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
		EventDispatcher.onEvent2(new RobotOffEvent());
		pickup = null;
	}

	/**
	 * Tells the server that the current job was cancelled
	 */
	public void jobCancelled()
	{
		EventDispatcher.onEvent2(new JobCancellationEvent());
		pickup = null;
	}

	/**
	 * Tells the server that the robot is in the wrong place
	 */
	public void wrongPlace()
	{
		EventDispatcher.onEvent2(new WrongPlaceEvent());
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
		pickup = null;
	}

}
