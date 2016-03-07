package linefollower;


import lejos.util.Delay;
import rp.systems.WheeledRobotSystem;

public class ForwardBehaviour extends AbstractBehaviour
{
    
    
    public ForwardBehaviour(WheeledRobotSystem robot)
    {
	super(robot);
    }

    @Override
    public boolean takeControl()
    {
	return true;
    }

    @Override
    public void action()
    {
	pilot.forward();
	pilot.setTravelSpeed(pilot.getMaxTravelSpeed() * 0.4);
	
	while(!isSuppressed && pilot.isMoving())
	{
	    Delay.msDelay(20);
	}
	
	pilot.stop();
	isSuppressed = false;
	
    }

}
