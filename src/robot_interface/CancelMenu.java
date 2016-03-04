package robot_interface;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class CancelMenu extends Thread
{

	private final String[] menu =
	{ "Cancel Job", "Wrong Place", "Back" };

	private Communication comm;
	private Boolean jobRun, running;
	private int selected;

	public CancelMenu(Communication comm, Boolean jobRun)
	{
		this.comm = comm;
		this.jobRun = jobRun;

		running = new Boolean(true);
		selected = 0;
	}

	@Override
	public void run()
	{
		while (running.get())
		{
			update(selected);
			int button = Button.waitForAnyPress();

			switch (button)
			{
			case Button.ID_ENTER:
			{
				if (selected == 0)
				{
					comm.jobCancelled();
					jobRun.set(false);
				} else if (selected == 1)
				{
					comm.wrongPlace();
					jobRun.set(false);
				}
				running.set(false);
			}
				break;

			case Button.ID_ESCAPE:
				running.set(false);

			case Button.ID_LEFT:
				if (selected > 0)
					selected--;

			case Button.ID_RIGHT:
				if (selected < 3)
					selected++;
			default:
				break;
			}
		}
	}

	private void update(int selected)
	{
		LCD.clear();

		for (int i = 0; i < 3; i++)
		{
			if (i == selected)
				System.out.println("-> " + menu[i]);
			else
				System.out.println("   " + menu[i]);
		}
	}
}
