package linefollower;


import java.util.Random;

import lejos.nxt.LightSensor;
import lejos.util.Delay;
import rp.systems.WheeledRobotSystem;

public class JunctionBehaviour extends AbstractBehaviour
{

    private CorrectListener listener;
    private boolean right;
    private int count;

    public JunctionBehaviour(WheeledRobotSystem robot, LightSensor leftS, LightSensor rightS, CorrectListener listener,
	    boolean right)
    {
	super(robot);
	this.listener = listener;
	this.right = right;
    }

    public JunctionBehaviour(WheeledRobotSystem robot, LightSensor leftS, LightSensor rightS, CorrectListener listener)
    {
	super(robot);
	this.listener = listener;

    }

    @Override
    public boolean takeControl()
    {
	return listener.junctionCorrect;
    }

    @Override
    public void action()
    {
	pilot.stop();
	pilot.travel(0.07);
	pilot.stop();

	// Left
	// pilot.rotate(90);

	// Random right-forward-left
//	Random random = new Random();
//	int r = random.nextInt(2);
//	if (r == 1)
//	    pilot.rotate(90);
//	   
//	else if (r == 2)
//	    pilot.rotate(-90);
//	Delay.msDelay(100);

	// Right or left by user choice
	// if(right) pilot.rotate(-90);
	// else pilot.rotate(90);

	// from array
	if (count < JunctionDetect.moves.length)
	    if (JunctionDetect.moves[count] == 0)
		pilot.rotate(90);
	    else if (JunctionDetect.moves[count] == 1)
		pilot.rotate(-90);
	count++;
    }

}

/*
 * useless while(listener.junctionCorrect) pilot.steer(200);
 * while(!listener.leftCorrect) pilot.steer(200); while(listener.leftCorrect)
 * pilot.steer(200);
 */
