package warehouse.linefollower;


import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole.Monitor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import rp.config.RobotConfigs;
import rp.systems.RobotProgrammingDemo;
import rp.systems.WheeledRobotSystem;

public class JunctionDetect extends RobotProgrammingDemo implements Runnable 
{
    private WheeledRobotSystem robot;

    public static int left_calibrated_black;
    public static int right_calibrated_black;
    public static final int ERROR = 100;
    public static final int[] moves = {0, 1, 2, 0, 1, 2};

    private boolean enter_pressed;
    
    private LightSensor leftS;
    private LightSensor rightS;

    public JunctionDetect()
    {
	enter_pressed = false;

	robot = new WheeledRobotSystem(RobotConfigs.EXPRESS_BOT);
	
	leftS = new LightSensor(SensorPort.S2);
	rightS = new LightSensor(SensorPort.S3);

	// Listener for pressing enter
	Button.ENTER.addButtonListener(new ButtonListener()
	{

	    @Override
	    public void buttonReleased(Button _b)
	    {
		enter_pressed = true;
	    }

	    @Override
	    public void buttonPressed(Button _b)
	    {

	    }
	});

    }
    
    public boolean getRun()
    {
	return m_run;
    }

    public void run()
    {
	// Calibrating the robot
	System.out.println("Please put the left sensor on a black surface");
	while (!enter_pressed)
	{
	    Delay.msDelay(50);
	}
	Delay.msDelay(2000);
	int s = 0;
	for (int i = 0; i < 10; i++)
	{
	    s += leftS.getNormalizedLightValue();
	}
	left_calibrated_black = s / 10;
	enter_pressed = false;

	System.out.println("Please put the right sensor on a black surface");
	while (!enter_pressed)
	{
	    Delay.msDelay(50);
	}
	Delay.msDelay(2000);
	s = 0;
	for (int i = 0; i < 10; i++)
	{
	    s += rightS.getNormalizedLightValue();
	}
	right_calibrated_black = s / 10;
	enter_pressed = false;
	Delay.msDelay(2000);

	// Creating the behaviors, and the arbitrator
	
	CorrectListener listener = new CorrectListener(robot, leftS, rightS, left_calibrated_black, right_calibrated_black, ERROR);
	
	Behavior forwardB = new ForwardBehaviour(robot);
	Behavior correctB = new LineCorrectBehaviour(robot, leftS, rightS, listener);
	Behavior junctionB = new JunctionBehaviour(robot, leftS, rightS, listener, true);
	
	Arbitrator arby = new Arbitrator(new Behavior[] { forwardB, correctB, junctionB });
	arby.start();
	
	while(m_run) { Delay.msDelay(300);}
	System.exit(0);
	

    }

    public static void main(String[] args)
    {
	new JunctionDetect().run();
    }

}
