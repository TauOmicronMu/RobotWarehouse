package warehouse.robot_interface;

<<<<<<< HEAD
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
=======
import lejos.nxt.LCD;
import lejos.nxt.Sound;
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf

/**
 * The class taking care of the output that needs to be printed and the sounds.
 * @author txs
 */
public class Output
{
	// Testing
	public static void main(String[] args)
	{
		new RobotInterface().start();
	}

	// DISPLAYING THINGS
	
	/**
	 * Draws a border on the screen
	 */
	private static void drawBorder()
	{
		int width = LCD.SCREEN_WIDTH;
		int height = LCD.SCREEN_HEIGHT;

		for (int i = 1; i < width - 1; i++)
		{
			LCD.setPixel(i, 1, 1);
			LCD.setPixel(i, 2, 1);
			LCD.setPixel(i, 3, 1);
			LCD.setPixel(i, height - 1, 1);
			LCD.setPixel(i, height - 2, 1);
			LCD.setPixel(i, height - 3, 1);
		}

		for (int i = 1; i < height - 1; i++)
		{
			LCD.setPixel(1, i, 1);
			LCD.setPixel(2, i, 1);
			LCD.setPixel(3, i, 1);
			LCD.setPixel(width - 1, i, 1);
			LCD.setPixel(width - 2, i, 1);
			LCD.setPixel(width - 3, i, 1);
		}
	}

	/**
	 * When the robot is moving
	 */
	public static void interfaceScreen()
	{
		LCD.clearDisplay();
		drawBorder();
		LCD.drawString("Moving around!", 2, 3);
	}

	/**
	 * When the robot is waiting for items
	 * @param itemName
	 * @param itemNumber
	 */
	public static void pickupScreen(String itemName, int itemNumber)
	{
		LCD.clearDisplay();
		drawBorder();

		for (int i = 1; i < LCD.SCREEN_WIDTH; i++)
			LCD.setPixel(i, LCD.SCREEN_HEIGHT / 2, 1);

		LCD.drawString("Item: " + itemName, 2, 2);
		LCD.drawString("Remaining: " + itemNumber, 2, 5);
	}

	/**
	 * The cancellation menu
	 * @param selected
	 */
	public static void cancelScreen(int selected)
	{
		final String[] menu =
		{ "Cancel Job", "Wrong Place", "Back" };

		LCD.clearDisplay();
		drawBorder();

		for (int i = 0; i < 3; i++)
		{
			if (i == selected)
				LCD.drawString("-> " + menu[i], 1, i + 2);
			else
				LCD.drawString("   " + menu[i], 1, i + 2);
		}
	}

	/**
	 * When reaching a drop off poing
	 */
	public static void dropoffScreen()
	{
		LCD.clearDisplay();
		drawBorder();
		LCD.drawString("Drop Off!", 2, 3);

		Sound.playNote(Sound.FLUTE, 392, 200);
		Sound.playNote(Sound.FLUTE, 392, 200);
		Sound.playNote(Sound.FLUTE, 262, 400);

		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			System.err.println("Catch2");
			System.exit(1);
		}
<<<<<<< HEAD
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
=======
		interfaceScreen();
	}

	// SOUNDS
	
	/**
	 * When picking items
	 */
	public static void itemSound()
	{
		Sound.setVolume(20);
		Sound.playNote(Sound.FLUTE, 440, 200);
	}

	/**
	 * When choosing the cancellation reason
	 */
	public static void selectSound()
	{
		Sound.setVolume(20);
		Sound.playNote(Sound.FLUTE, 392, 200);
	}

	/**
	 * After successfully finishing a pickup
	 */
	public static void pickupFinishedSound()
	{
		Sound.setVolume(20);
		Sound.playNote(Sound.FLUTE, 494, 150);
		Sound.playNote(Sound.FLUTE, 423, 150);
		Sound.playNote(Sound.FLUTE, 440, 200);
	}

	/**
	 * After a job has been cancelled
	 */
	public static void pickupCanceledSound()
	{
		Sound.setVolume(20);
		Sound.playNote(Sound.FLUTE, 440, 150);
		Sound.playNote(Sound.FLUTE, 415, 150);
		Sound.playNote(Sound.FLUTE, 440, 200);
	}

	/**
	 * When reaching a pickup point
	 */
	public static void pickupReached()
	{
		Sound.setVolume(20);
		Sound.playNote(Sound.FLUTE, 262, 500);
		Sound.playNote(Sound.FLUTE, 262, 500);
	}

	/**
	 * When the user forgets to "put" items
	 */
	public static void warning()
	{
		Sound.setVolume(40);
		Sound.playNote(Sound.FLUTE, 587, 150);
		Sound.playNote(Sound.FLUTE, 587, 150);
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
	}

}
