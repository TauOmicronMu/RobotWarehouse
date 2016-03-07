package linefollower;


import lejos.nxt.LightSensor;
import lejos.util.Delay;
import rp.systems.WheeledRobotSystem;

public class LineCorrectBehaviour extends AbstractBehaviour
{
    private CorrectListener listener;

    public LineCorrectBehaviour(WheeledRobotSystem robot, LightSensor leftS, LightSensor rightS,
	    CorrectListener listener)
    {
	super(robot);
	listener.start();
	this.listener = listener;
	

    }

    @Override
    public boolean takeControl()
    {
	return (listener.leftCorrect || listener.rightCorrect);
    }

    @Override
    public void action()
    {
	if (listener.leftCorrect)
	{
	    pilot.rotateLeft();
	    pilot.setRotateSpeed(pilot.getRotateMaxSpeed()* 0.4);
	    while (listener.leftCorrect) {Delay.msDelay(5);}
	    pilot.stop();
	    Delay.msDelay(100);
	}
	//if (listener.rightCorrect)
	else
	{
	    pilot.rotateRight();
	    pilot.setRotateSpeed(pilot.getRotateMaxSpeed()* 0.4);
	    while (listener.rightCorrect) {Delay.msDelay(5);}
	    pilot.stop();
	    Delay.msDelay(100);
	}

    }

}
