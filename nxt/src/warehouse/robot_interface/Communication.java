package warehouse.robot_interface;

<<<<<<< HEAD
<<<<<<< HEAD

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
=======
import warehouse.event.JobCancellationEvent;
import warehouse.event.JobCompleteEvent;
import warehouse.event.RobotOffEvent;
import warehouse.event.WrongPlaceEvent;
import warehouse.job.Job;
import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
import warehouse.util.Subscriber;

=======
import warehouse.job.AssignedJob;
import warehouse.util.ItemPickup;
import warehouse.util.Robot;
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf

/**
 * The class assuring the connection between the server and the robot. This is a
 * private field in RobotInterface, and it's instantiated inside the
 * constructor.
 */
<<<<<<< HEAD
<<<<<<< HEAD
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
=======
public class Communication {
=======
public class Communication
{
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf

	ItemPickup pickup;
	String message;
	Robot robot;
	AssignedJob job;
	boolean dropOff;

<<<<<<< HEAD
	static {
        EventDispatcher.subscribe2(Communication.class);
    }
	
	public Communication() {
		EventDispatcher.subscribe2(this);
		pickup = null;
		message = null;
	}
	
	@Subscriber
	public void onDropOff(DropOffEvent e) {
		message = e.getMessage();
		EventDispatcher.onEvent2(new JobCompleteEvent(e.getJob()));
	}
	
	@Subscriber
	public void onPickup(PickupEvent e) {
		pickup = e.getPickup();
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
=======
	public Communication()
	{
		pickup = new ItemPickup("Muffin", null, 5);
		dropOff = false;
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
	}

	/**
	 * Gets the message
	 * @return The message
	 */
<<<<<<< HEAD
<<<<<<< HEAD
	public boolean atDropOff()
	{
		boolean result = dropOff;
		dropOff = false;
		return result;
=======
	public String getMessage()
	{
		return message;
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
=======
	public boolean atDropOff()
	{
		boolean result = dropOff;
		dropOff = false;
		return result;
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
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
<<<<<<< HEAD
<<<<<<< HEAD
	public void robotOff()
	{
		EventDispatcher.onEvent2(new RobotOffEvent(robot));
=======
	public void robotOff() {
		EventDispatcher.onEvent2(new RobotOffEvent());
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
=======
	public void robotOff()
	{
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
		pickup = null;
	}

	/**
	 * Tells the server that the current job was cancelled
	 */
<<<<<<< HEAD
<<<<<<< HEAD
	public void jobCancelled()
	{
		EventDispatcher.onEvent2(new JobCancellationEvent(job));
=======
	public void jobCancelled() {
		EventDispatcher.onEvent2(new JobCancellationEvent());
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
=======
	public void jobCancelled()
	{
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
		pickup = null;
	}

	/**
	 * Tells the server that the robot is in the wrong place
	 */
<<<<<<< HEAD
<<<<<<< HEAD
	public void wrongPlace()
	{
		EventDispatcher.onEvent2(new RobotLostEvent(robot));
=======
	public void wrongPlace() {
		EventDispatcher.onEvent2(new WrongPlaceEvent());
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
=======
	public void wrongPlace()
	{
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
		pickup = null;
	}

	/**
	 * Returns the name of the items that need to be picked
	 * @return The name of the item
	 */
<<<<<<< HEAD
<<<<<<< HEAD
	public String getItemName()
	{
=======
	public String getItemName() {
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
=======
	public String getItemName()
	{
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
		return pickup.itemName;
	}

	/**
	 * Returns the number of items that need to be picked
	 * @return The number of items
	 */
<<<<<<< HEAD
<<<<<<< HEAD
	public int getItemNumber()
	{
=======
	public int getItemNumber() {
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
=======
	public int getItemNumber()
	{
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
		return pickup.itemCount;
	}

	/**
	 * Tells the server that the job is done
	 */
<<<<<<< HEAD
<<<<<<< HEAD
	public void jobDone()
	{
		EventDispatcher.onEvent2(new PickupCompleteEvent(robot, pickup));
		pickup = null;
	}

=======
	public void jobDone() {
=======
	public void jobDone()
	{
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
		pickup = null;
	}
<<<<<<< HEAD
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
=======

>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
}
