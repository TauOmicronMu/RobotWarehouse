package warehouse.robot_interface;
import lejos.util.TimerListener;

/**
 * Class implementing the timer listener
 * @author txs
 */
public class WarningListener implements TimerListener
{
	private boolean working;
	
	public WarningListener()
	{
		working = true;
	}
	
	@Override
	public void timedOut()
	{
		if (working)
			Output.warning();
	}
	
	public void set(boolean working)
	{
		this.working = working;
	}
}
