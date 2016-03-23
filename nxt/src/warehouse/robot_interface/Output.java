package warehouse.robot_interface;


import warehouse.event.PickupReachedEvent;
import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;

public class Output
{
	public static void main(String[] args)
	{
		new RobotInterface().start();
		try
		{
			Thread.sleep(5000);
			EventDispatcher.onEvent2(new PickupReachedEvent(new ItemPickup("Item", null, 5), null));
		} catch (InterruptedException e)
		{
			System.err.println("Catch");
			System.exit(1);
		}
		
	}
}
