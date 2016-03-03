package robot_interface;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;

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
		interfaceRun = new Boolean(true);
		hasJob = new Boolean(false);
		jobRun = new Boolean(false);
		
		comm = new Communication();

		Button.ESCAPE.addButtonListener(new ExitListener(comm, hasJob, interfaceRun, jobRun));

		reset();
	}

	/**
	 * When finishing a job
	 */
	public void reset()
	{
		LCD.clear();
		System.out.println("Waiting to get to the next pick-up location!");
		hasJob.set(false);;
	}

	@Override
	public void run()
	{
		while (interfaceRun.get())
		{
			if (comm.hasJob())
			{
				// Start a new job

				hasJob.set(true);;
				jobRun.set(true);;

				PickupJob pj = new PickupJob(comm, jobRun);
				pj.start();

				try
				{
					pj.join();
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				// After finishing the job:
				reset();

			}

			// Show mercy to the processor
			Delay.msDelay(50);
		}
	}

	public static void main(String[] args)
	{
		new RobotInterface().run();
	}

}
