package warehouse.jobselection.event;

import warehouse.util.Robot;

/**
 * Created by Owen on 22/03/2016.
 */
public class AddedToSelectedListEvent {

	public Robot robot;
	public boolean testing;
	
	public AddedToSelectedListEvent(boolean testing){
		
		this.testing = testing;
	};
}
