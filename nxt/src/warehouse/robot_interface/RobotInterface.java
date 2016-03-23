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

	/**
	 * Constructor
	 */
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

				PickupJob pj = new PickupJob(comm, jobRun);
				pj.run();

				// After finishing the job:
				reset();
			}
			
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
			}

			// Show mercy to the processor
			Rate r = new Rate(20);
			r.sleep();
		}
	}

}
