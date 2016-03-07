// NOT READY YET

/**
 * The class assuring the connection between the server and the robot. This is a
 * private field in RobotInterface, and it's instantiated inside the
 * constructor.
 */
public class Communication
{

	boolean hasJob;

	public Communication()
	{
		// TODO

		hasJob = true;
	}

	/**
	 * Tells whether the robot is at a pickup point
	 * 
	 * @return whether the robot is at a pickup point
	 */
	public boolean hasJob()
	{
		// TODO
		
		return hasJob;
	}

	/**
	 * Tells the server that the robot was turned off
	 */
	public void robotOff()
	{
		// TODO
	}

	/**
	 * Tells the server that the current job was cancelled
	 */
	public void jobCancelled()
	{
		// TODO

		hasJob = false;
	}

	/**
	 * Tells the server that the robot is in the wrong place
	 */
	public void wrongPlace()
	{
		// TODO

		hasJob = false;
	}

	/**
	 * Returns the name of the items that need to be picked
	 * 
	 * @return The name of the item
	 */
	public String getItemName()
	{
		// TODO
		
		return "Item *01*";
	}

	/**
	 * Returns the number of items that need to be picked
	 * 
	 * @return The number of items
	 */
	public int getItemNumber()
	{
		// TODO
		
		return 5;
	}

	/**
	 * Tells the server that the job is done
	 */
	public void jobDone()
	{
		// TODO

		hasJob = false;
	}

}
