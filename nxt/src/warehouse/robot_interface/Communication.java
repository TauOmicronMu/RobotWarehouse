package warehouse.robot_interface;

import warehouse.job.AssignedJob;
import warehouse.util.ItemPickup;
import warehouse.util.Robot;

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

	public Communication()
	{
		pickup = new ItemPickup("Muffin", null, 5);
		dropOff = false;
	}

	/**
	 * Gets the message
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
		pickup = null;
	}

	/**
	 * Tells the server that the current job was cancelled
	 */
	public void jobCancelled()
	{
		pickup = null;
	}

	/**
	 * Tells the server that the robot is in the wrong place
	 */
	public void wrongPlace()
	{
		pickup = null;
	}

	/**
	 * Returns the name of the items that need to be picked
	 * @return The name of the item
	 */
	public String getItemName()
	{
		return pickup.itemName;
	}

	/**
	 * Returns the number of items that need to be picked
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
