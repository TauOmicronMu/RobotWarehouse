package warehouse.robot_interface;


import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;
import rp.util.Rate;

/**
 * The thread starting when a pick up job is ready, and the robot is in the pick
 * up location
 * 
 * @author txs
 *
 */
public class PickupJob
{

	private Communication comm;
	private Boolean jobRun;
	private String itemName;
	private int itemNumber;
	private boolean enterPressed, escapePressed;

	/**
	 * Constructor
	 * 
	 * @param comm
	 * @param jobRun
	 */
	public PickupJob(Communication comm, Boolean jobRun)
	{
		this.comm = comm;
		this.jobRun = jobRun;
		itemName = comm.getItemName();
		itemNumber = comm.getItemNumber();
		enterPressed = false;
		escapePressed = false;

		Button.ENTER.addButtonListener(new ButtonListener()
		{

			@Override
			public void buttonReleased(Button b)
			{
				enterPressed = true;
			}

			@Override
			public void buttonPressed(Button b)
			{
			}
		});
		
		Button.ESCAPE.addButtonListener(new ButtonListener()
		{

			@Override
			public void buttonReleased(Button b)
			{
				escapePressed = true;
			}

			@Override
			public void buttonPressed(Button b)
			{
			}
		});
	}

	public void run()
	{
		updateScreen();

		while (jobRun && itemNumber > 0)
		{
			if (enterPressed)
			{
				itemNumber--;
				enterPressed = false;

				updateScreen();
				
				Sound.playTone(600, 30);
				Delay.msDelay(30);
			}

			if (escapePressed)
			{
				itemNumber++;
				CancelMenu cancelMenu = new CancelMenu(comm, jobRun);
				cancelMenu.run();
				escapePressed = false;
			}

			Rate rate = new Rate(20);
			rate.sleep();
		}

		if (itemNumber == 0)
		{
			Sound.playTone(440, 30);
			Delay.msDelay(30);
			comm.jobDone();
		} else
		{
			Sound.playTone(200, 30);
			Delay.msDelay(30);
		}
	}

	/**
	 * Updating the screen with the remaining number of items
	 */
	private void updateScreen()
	{
		LCD.clearDisplay();
		LCD.drawString("Item: " + itemName, 0, 0);
		LCD.drawString("Remaining: " + itemNumber, 0, 1);
	}

}
