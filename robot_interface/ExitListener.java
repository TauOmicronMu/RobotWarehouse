package robot_interface;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class ExitListener implements ButtonListener
{

	private Boolean interfaceRun, hasJob, jobRun;
	private Communication comm;

	/**
	 * Constructor
	 * 
	 * @param comm
	 * @param hasJob
	 * @param interfaceRun
	 * @param jobRun
	 */
	public ExitListener(Communication comm, Boolean hasJob, Boolean interfaceRun, Boolean jobRun)
	{
		super();
		this.comm = comm;
		this.hasJob = hasJob;
		this.interfaceRun = interfaceRun;
		this.jobRun = jobRun;
	}

	@Override
	public void buttonPressed(Button b)
	{
		// Nothing to do here
	}

	@Override
	public void buttonReleased(Button b)
	{
		if (hasJob.get())
		{
			// Starts a cancellation menu
			CancelMenu cancelMenu = new CancelMenu(comm, jobRun);
			cancelMenu.start();
			try
			{
				cancelMenu.join();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		} else
		{
			// Turn the robot off
			interfaceRun.set(false);

			// Tell the server about it
			comm.robotOff();
		}
	}

}
