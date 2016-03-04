package robot_interface;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class PickupJob extends Thread
{

	private Communication comm;
	private Boolean jobRun;
	private String itemName;
	private int itemNumber;

	public PickupJob(Communication comm, Boolean jobRun)
	{
		this.comm = comm;
		this.jobRun = jobRun;
		itemName = comm.getItemName();
		itemNumber = comm.getItemNumber();
	}

	@Override
	public void run()
	{
		while (jobRun.get() && itemNumber > 0)
		{
			updateScreen();
			int button = Button.waitForAnyPress();
			if (button == Button.ID_ENTER)
			{
				itemNumber--;
			}
		}

		if (itemNumber == 0)
		{
			System.out.println("DONE!");
			comm.jobDone();
		} else
			System.out.println("CANCELED!");
	}

	private void updateScreen()
	{
		LCD.clear();
		System.out.println("Item: " + itemName);
		System.out.println("Remaining: " + itemNumber);
	}

}
