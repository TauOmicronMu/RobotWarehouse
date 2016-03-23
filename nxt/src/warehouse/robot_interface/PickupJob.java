package warehouse.robot_interface;

<<<<<<< HEAD

=======
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;
import rp.util.Rate;

/**
 * The thread starting when a pick up job is ready, and the robot is in the pick
 * up location
 * 
 * @author txs
 *
 */
<<<<<<< HEAD
public class PickupJob
{
=======
public class PickupJob {
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f

	private Communication comm;
	private Boolean jobRun;
	private String itemName;
	private int itemNumber;
	private boolean enterPressed, escapePressed;

	/**
	 * Constructor
	 * 
	 * @param comm
	 * @param jobRun
	 */
<<<<<<< HEAD
	public PickupJob(Communication comm, Boolean jobRun)
	{
=======
	public PickupJob(Communication comm, Boolean jobRun) {
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
		this.comm = comm;
		this.jobRun = jobRun;
		itemName = comm.getItemName();
		itemNumber = comm.getItemNumber();
		enterPressed = false;
		escapePressed = false;

<<<<<<< HEAD
		Button.ENTER.addButtonListener(new ButtonListener()
		{

			@Override
			public void buttonReleased(Button b)
			{
=======
		Button.ENTER.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
				enterPressed = true;
			}

			@Override
<<<<<<< HEAD
			public void buttonPressed(Button b)
			{
			}
		});
		
		Button.ESCAPE.addButtonListener(new ButtonListener()
		{

			@Override
			public void buttonReleased(Button b)
			{
=======
			public void buttonPressed(Button b) {
			    //TODO : ???
			}
		});
		
		Button.ESCAPE.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
				escapePressed = true;
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
	}

<<<<<<< HEAD
	public void run()
	{
		updateScreen();

		while (jobRun && itemNumber > 0)
		{
			if (enterPressed)
			{
=======
	public void run() {
		updateScreen();

		while (jobRun && itemNumber > 0) {
			if (enterPressed) {
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
				itemNumber--;
				enterPressed = false;

				updateScreen();
				
				Sound.playTone(600, 30);
				Delay.msDelay(30);
			}

<<<<<<< HEAD
			if (escapePressed)
			{
				itemNumber++;
				CancelMenu cancelMenu = new CancelMenu(comm, jobRun);
				cancelMenu.run();
=======
			if (escapePressed) {
				itemNumber++;

				CancelMenu cancelMenu = new CancelMenu(comm, jobRun);
				cancelMenu.run();

>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
				escapePressed = false;
			}

			Rate rate = new Rate(20);
			rate.sleep();
		}

<<<<<<< HEAD
		if (itemNumber == 0)
		{
			Sound.playTone(440, 30);
			Delay.msDelay(30);
			comm.jobDone();
		} else
		{
=======
		if (itemNumber == 0) {
			Sound.playTone(440, 30);
			Delay.msDelay(30);
			comm.jobDone();
		}
		else {
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
			Sound.playTone(200, 30);
			Delay.msDelay(30);
		}
	}

	/**
	 * Updating the screen with the remaining number of items
	 */
<<<<<<< HEAD
	private void updateScreen()
	{
=======
	private void updateScreen() {
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
		LCD.clearDisplay();
		LCD.drawString("Item: " + itemName, 0, 0);
		LCD.drawString("Remaining: " + itemNumber, 0, 1);
	}
<<<<<<< HEAD

=======
>>>>>>> c38831639d5da111cb4562e6a5ec6cee90de0c6f
}
