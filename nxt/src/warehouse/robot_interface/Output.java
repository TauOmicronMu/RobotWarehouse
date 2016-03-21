package warehouse.robot_interface;

import warehouse.util.EventDispatcher;
import warehouse.util.ItemPickup;

public class Output extends Thread
{
	public void run()
	{
		try
		{
			Thread.sleep(5000);
			EventDispatcher.onEvent2("MESSAGE!!!");
			Thread.sleep(20000);
			EventDispatcher.onEvent2(new ItemPickup("Item", null, 5));
		} catch (InterruptedException e)
		{
			System.err.println("Catch");
			System.exit(1);
		}
		
	}
}
