package warehouse.robot_interface;

<<<<<<< HEAD

=======
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
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
<<<<<<< HEAD
=======
	private String message;
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f

	/**
	 * Constructor
	 */
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
		comm = new Communication();
		message = comm.getMessage();

		Button.ESCAPE.addButtonListener(new ButtonListener() {

			@Override
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
		});

		reset();
	}

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

		hasJob.set(false);
		message = null;
	}

	@Override
	public void run() {
		while (interfaceRun.get()) {
			if (comm.hasJob()) {
				// Start a new job

				hasJob.set(true);
				jobRun.set(true);
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f

				PickupJob pj = new PickupJob(comm, jobRun);
				pj.run();

				// After finishing the job:
				reset();
			}
			
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
			}

			// Show mercy to the processor
			Rate r = new Rate(20);
			r.sleep();
		}
	}

<<<<<<< HEAD
=======
	public static void main(String[] args) {
		new RobotInterface().run();
	}
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
}
