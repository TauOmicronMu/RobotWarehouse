package warehouse.robot_interface;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.util.Timer;
import rp.util.Rate;

/**
 * The main class of the Robot interface
 * @author txs
 */
public class RobotInterface extends Thread
{
	private boolean interfaceRun, jobRun;
	private Communication comm;
	private boolean enterPressed;
	private boolean escapePressed;
	private String itemName;
	private int itemNumber;

	/**
	 * Constructor
	 */
	public RobotInterface()
	{
		interfaceRun = true;
		jobRun = false;
		comm = new Communication();

		enterPressed = false;
		escapePressed = false;

		itemName = null;
		itemNumber = 0;

		// ADDING LISTENERS FOR BUTTONS
		
		Button.ENTER.addButtonListener(new ButtonListener()
		{

			@Override
			public void buttonReleased(Button b)
			{
				enterPressed = true;
			}

			@Override
			public void buttonPressed(Button b){}
		});

		Button.ESCAPE.addButtonListener(new ButtonListener()
		{

			@Override
			public void buttonReleased(Button b)
			{
				escapePressed = true;
			}

			@Override
			public void buttonPressed(Button b){}
		});

		Output.interfaceScreen();
	}

	@Override
	public void run()
	{
		while (interfaceRun)
		{
			if (comm.hasJob())
			{
				// Start a new job
				jobRun = true;
				pickupJob();

				// After finishing the job:
				Output.interfaceScreen();
				
			} else if (escapePressed)
			{
				// Turn the robot off
				comm.robotOff();
				interfaceRun = false;
			}

			if (comm.atDropOff())
			{
				Output.dropoffScreen();
			}

			// Show mercy to the processor
			Rate r = new Rate(20);
			r.sleep();
		}
	}

	/**
	 * When reaching a pickup point
	 */
	public void pickupJob()
	{
		// Creating a timer, in case people forget to "put" items
		WarningListener warningListener = new WarningListener();
		Timer timer = new Timer(7000, warningListener);

		// Getting information about the items
		itemName = comm.getItemName();
		itemNumber = comm.getItemNumber();

		Output.pickupScreen(itemName, itemNumber);
		Output.pickupReached();

		while (jobRun && itemNumber > 0)
		{
			timer.start();
			timer.setDelay(7000);
			warningListener.set(true);
			
			if (enterPressed)
			{
				timer.stop();
				warningListener.set(false);
				
				itemNumber--;
				enterPressed = false;

				Output.pickupScreen(itemName, itemNumber);
				Output.itemSound();
			}

			if (escapePressed)
			{
				timer.stop();
				warningListener.set(false);
				
				itemNumber++;
				cancelMenu();
				escapePressed = false;
			}

			Rate rate = new Rate(20);
			rate.sleep();
		}

		if (itemNumber == 0)
		{
			// Successfully finished
			Output.pickupFinishedSound();
			comm.jobDone();
		} else
		{
			// Canceled
			Output.pickupCanceledSound();
		}
	}

	/**
	 * The cancellation menu
	 */
	public void cancelMenu()
	{
		boolean running = true;
		int selected = 0;

		while (running)
		{
			Output.cancelScreen(selected);
			int button = Button.waitForAnyPress();

			switch (button)
			{
			case Button.ID_ENTER:
			{
				if (selected == 0)
				{
					comm.jobCancelled();
					jobRun = false;
				} else if (selected == 1)
				{
					comm.wrongPlace();
					jobRun = false;
				}
				running = false;
			}
				break;

			case Button.ID_ESCAPE:
				running = false;
				break;

			case Button.ID_LEFT:
				if (selected > 0)
				{
					Output.selectSound();
					selected--;
				}
				break;

			case Button.ID_RIGHT:
				if (selected < 2)
				{
					Output.selectSound();
					selected++;
				}
				break;

			default:
				break;
			}
		}
	}

}
