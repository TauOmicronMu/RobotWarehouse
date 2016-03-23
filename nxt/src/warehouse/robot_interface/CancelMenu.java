package warehouse.robot_interface;

<<<<<<< HEAD

import lejos.nxt.Button;
import lejos.nxt.LCD;
=======
import lejos.nxt.Button;
import lejos.nxt.LCD;
import warehouse.robot_interface.Communication;
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f

/**
 * The cancellation menu
 * 
 * @author txs
 *
 */
<<<<<<< HEAD
public class CancelMenu
{

	private final String[] menu =
	{ "Cancel Job", "Wrong Place", "Back" };
=======
public class CancelMenu {

	private final String[] menu = { "Cancel Job", "Wrong Place", "Back" };
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f

	private Communication comm;
	private Boolean jobRun;
	private int selected;
	private boolean running;

	/**
<<<<<<< HEAD
	 * Constructor
	 * @param comm
	 * @param jobRun
	 */
	public CancelMenu(Communication comm, Boolean jobRun)
	{
=======
	 * Creates a new instance of the CancelMenu class.
	 * @param comm
	 * @param jobRun
	 */
	public CancelMenu(Communication comm, Boolean jobRun) {
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
		this.comm = comm;
		this.jobRun = jobRun;

		running = true;
		selected = 0;
	}

<<<<<<< HEAD
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
					selected--;
				break;

			case Button.ID_RIGHT:
				if (selected < 2)
					selected++;
				break;
				
			default:
				break;
=======
	public void run() {
		while (running) {
			update(selected);
			int button = Button.waitForAnyPress();

			switch (button) {
			    case Button.ID_ENTER:
				    if (selected == 0) {
				    	comm.jobCancelled();
				    	jobRun = false;
					}
					else if (selected == 1) {
						comm.wrongPlace();
						jobRun = false;
					}

					running = false;
					break;

				case Button.ID_ESCAPE:
					running = false;
					break;

				case Button.ID_LEFT:
					if (selected > 0) { selected-- }
				    break;

				case Button.ID_RIGHT:
					if (selected < 2) { selected++ }
				    break;
				
			    default:
			    	break;
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
			}
		}
	}

	/**
	 * Re-write the menu on the screen
	 * @param selected
	 */
<<<<<<< HEAD
	private void update(int selected)
	{
		LCD.clearDisplay();

		for (int i = 0; i < 3; i++)
		{
			if (i == selected)
				LCD.drawString("-> " + menu[i], 0, i + 1);
			else
				LCD.drawString("   " + menu[i], 0, i + 1);
=======
	private void update(int selected) {
		LCD.clearDisplay();

		for (int i = 0; i < 3; i++) {
			if (i == selected) {
				LCD.drawString("-> " + menu[i], 0, i + 1)
			}
			else {
				LCD.drawString("   " + menu[i], 0, i + 1)
			}
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
		}
	}
}
