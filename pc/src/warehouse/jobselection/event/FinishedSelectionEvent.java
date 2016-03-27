package warehouse.jobselection.event;

import warehouse.util.Robot;

/**
 * Created by Owen on 23/03/2016.
 */
public class FinishedSelectionEvent {
	
	public Robot robot;
	public boolean testing;
	
	public FinishedSelectionEvent(boolean testing){
		
		this.testing = testing;
	}
}
