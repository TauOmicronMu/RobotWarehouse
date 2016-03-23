package warehouse.robot_interface;

<<<<<<< HEAD

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
		
=======
import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;

/*
 * TEST CLASS - DEPRECATED
 */
@Deprecated
public class Output extends Thread {
	public void run() {
		try {
			Thread.sleep(5000);
			EventDispatcher.onEvent2("MESSAGE!!!");
			Thread.sleep(20000);
			EventDispatcher.onEvent2(new ItemPickup("Item", null, 5));
		}
		catch (InterruptedException e) {
			System.err.println("Catch");
			System.exit(1);
		}
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
	}
}
