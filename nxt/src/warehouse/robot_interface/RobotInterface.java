package warehouse.robot_interface;

<<<<<<< HEAD

=======
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
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
<<<<<<< HEAD
<<<<<<< HEAD
=======
	private String message;
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
=======
	private boolean enterPressed;
	private boolean escapePressed;
	private String itemName;
	private int itemNumber;
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf

	/**
	 * Constructor
	 */
<<<<<<< HEAD
<<<<<<< HEAD
	public RobotInterface()
	{
		interfaceRun = true;
		hasJob = false;
		jobRun = false;
		comm = new Communication();

		Button.ESCAPE.addButtonListener(new ButtonListener()
		{

			@Override
			public void buttonReleased(Button b)
			{
				if (!hasJob)
				{
					comm.robotOff();
					interfaceRun = false;
=======
	public RobotInterface() {

		interfaceRun = new Boolean(true);
		hasJob = new Boolean(false);
		jobRun = new Boolean(false);
=======
	public RobotInterface()
	{
		interfaceRun = true;
		jobRun = false;
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
		comm = new Communication();

		enterPressed = false;
		escapePressed = false;

		itemName = null;
		itemNumber = 0;

		// ADDING LISTENERS FOR BUTTONS
		
		Button.ENTER.addButtonListener(new ButtonListener()
		{

			@Override
<<<<<<< HEAD
			public void buttonReleased(Button b) {
				if (!hasJob.get()) {
					comm.robotOff();
					interfaceRun.set(false);
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
				}
			}

			@Override
<<<<<<< HEAD
			public void buttonPressed(Button b)
			{
=======
			public void buttonPressed(Button b) {
				//TODO : ???
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
			}
=======
			public void buttonReleased(Button b)
			{
				enterPressed = true;
			}

			@Override
			public void buttonPressed(Button b){}
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
		});

		Button.ESCAPE.addButtonListener(new ButtonListener()
		{

<<<<<<< HEAD
	/**
	 * When finishing a job
	 */
<<<<<<< HEAD
	public void reset()
	{
		LCD.clearDisplay();
		LCD.drawString("Moving around!", 0, 0);
		hasJob = false;
	}

	@Override
	public void run()
	{
		while (interfaceRun)
		{
			if (comm.hasJob())
			{
				// Start a new job

				hasJob = true;
				jobRun = true;
=======
	public void reset() {
		LCD.clearDisplay();
		LCD.drawString("Moving around!", 0, 0);
=======
			@Override
			public void buttonReleased(Button b)
			{
				escapePressed = true;
			}
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf

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
<<<<<<< HEAD

				hasJob.set(true);
				jobRun.set(true);
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f

				PickupJob pj = new PickupJob(comm, jobRun);
				pj.run();
=======
				jobRun = true;
				pickupJob();
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf

				// After finishing the job:
				Output.interfaceScreen();
				
			} else if (escapePressed)
			{
				// Turn the robot off
				comm.robotOff();
				interfaceRun = false;
			}
<<<<<<< HEAD
			
<<<<<<< HEAD
			if (comm.atDropOff())
			{
				LCD.clearDisplay();
				LCD.drawString("Drop Off point reached!", 1, 1);
				try
				{
					Thread.sleep(5000);
				} catch (InterruptedException e)
				{
					System.err.println("Catch2");
					System.exit(1);
				}
				reset();
=======
			if (message != null) {
				LCD.clearDisplay();
				LCD.drawString(message, 1, 1);
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
=======

			if (comm.atDropOff())
			{
				Output.dropoffScreen();
>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
			}

			// Show mercy to the processor
			Rate r = new Rate(20);
			r.sleep();
		}
	}

<<<<<<< HEAD
<<<<<<< HEAD
=======
	public static void main(String[] args) {
		new RobotInterface().run();
	}
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
=======
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

>>>>>>> 5ebeee19c5ce5c9f350e45806ad048593fd0d0bf
}
