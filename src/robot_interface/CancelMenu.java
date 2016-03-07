
import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * The cancellation menu
 * 
 * @author txs
 *
 */
public class CancelMenu
{

	private final String[] menu =
	{ "Cancel Job", "Wrong Place", "Back" };

	private Communication comm;
	private Boolean jobRun;
	private int selected;
	private boolean running;

	/**
	* Constructor
	*/
	public CancelMenu(Communication comm, Boolean jobRun)
	{
		this.comm = comm;
		this.jobRun = jobRun;

		running = true;
		selected = 0;
	}

	public void run()
	{
		while (running)
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
				running = false;
			}
				break;

			case Button.ID_ESCAPE:
				running = false;
				break;

			case Button.ID_LEFT:
				if (selected > 0)
					selected--;
				break;

			case Button.ID_RIGHT:
				if (selected < 2)
					selected++;
				break;
				
			default:
				break;
			}
		}
	}

	private void update(int selected)
	{
		LCD.clearDisplay();

		for (int i = 0; i < 3; i++)
		{
			if (i == selected)
				LCD.drawString("-> " + menu[i], 0, i + 1);
			else
				LCD.drawString("   " + menu[i], 0, i + 1);
		}
	}
}
