package warehouse.robot_interface;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.LCDOutputStream;
import lejos.util.Delay;
import rp.util.Rate;

/**
 * The main class of the Robot interface
 * 
 * @author txs
 */
public class RobotInterface extends Thread
{
	private Boolean interfaceRun, jobRun, hasJob;
	private Communication comm;
	private String message;

	/**
	 * Constructor
	 */
	public RobotInterface()
	{
		interfaceRun = new Boolean(true);
		hasJob = new Boolean(false);
		jobRun = new Boolean(false);
		comm = new Communication();
		message = comm.getMessage();

		Button.ESCAPE.addButtonListener(new ButtonListener()
		{

			@Override
			public void buttonReleased(Button b)
			{
				if (!hasJob.get())
				{
					comm.robotOff();
					interfaceRun.set(false);
				}
			}

			@Override
			public void buttonPressed(Button b)
			{
			}
		});

		reset();
	}

	/**
	 * When finishing a job
	 */
	public void reset()
	{
		LCD.clearDisplay();
		LCD.drawString("Moving around!", 0, 0);
		hasJob.set(false);
		message = null;
	}

	@Override
	public void run()
	{
		while (interfaceRun.get())
		{
			if (comm.hasJob())
			{
				// Start a new job

				hasJob.set(true);
				jobRun.set(true);

				PickupJob pj = new PickupJob(comm, jobRun);
				pj.run();

				// After finishing the job:
				reset();
			}
			
			if (message != null)
			{
				LCD.clearDisplay();
				LCD.drawString(message, 1, 1);
			}

			// Show mercy to the processor
			Rate r = new Rate(20);
			r.sleep();
		}
	}

	public static void main(String[] args)
	{
		new RobotInterface().run();
	}

}
