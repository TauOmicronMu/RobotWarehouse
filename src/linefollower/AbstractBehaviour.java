package linefollower;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import rp.systems.WheeledRobotSystem;

public abstract class AbstractBehaviour implements Behavior
{

    protected final WheeledRobotSystem robot;
    protected final DifferentialPilot pilot;
    
    public AbstractBehaviour(WheeledRobotSystem robot)
    {
	super();
	this.robot = robot;
	pilot = robot.getPilot();
    }
    
    protected boolean isSuppressed;
    
    @Override
    public void suppress()
    {
	isSuppressed = true;
    }

}
