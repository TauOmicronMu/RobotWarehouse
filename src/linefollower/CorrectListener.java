package linefollower;


import lejos.nxt.LightSensor;
import lejos.util.Delay;
import rp.systems.WheeledRobotSystem;

public class CorrectListener extends Thread
{
    public boolean leftCorrect;
    public boolean rightCorrect;
    public boolean junctionCorrect;
    
    private WheeledRobotSystem robot;
    private LightSensor sLeft;
    private LightSensor sRight;
    private int left;
    private int right;
    private int error;

    public CorrectListener(WheeledRobotSystem robot, LightSensor sLeft, LightSensor sRight, int left, int right, int error)
    {
	this.robot = robot;
	this.sLeft = sLeft;
	this.sRight = sRight;
	leftCorrect = false;
	rightCorrect = false;
	junctionCorrect = false;
	
	this.left = left;
	this.right = right;
	this.error = error;
    }
    
    @Override
    public void run()
    {
	while(true)
	{
	    System.out.println(sLeft.getNormalizedLightValue() + "-" + sRight.getNormalizedLightValue() + "<-" + 
		    left+ "-" + right);
	    if (sLeft.getNormalizedLightValue() < left + error)
		leftCorrect = true;
	    else leftCorrect = false;
	   
	    if (sRight.getNormalizedLightValue() < right + error)
		rightCorrect = true;
	    else rightCorrect = false;
	    
	    if(leftCorrect && rightCorrect)
		junctionCorrect = true;
	    else junctionCorrect = false;
		
	    
	    Delay.msDelay(5);
	}
    }

}
