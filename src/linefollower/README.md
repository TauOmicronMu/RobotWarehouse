Sensor calibration:
Two sensors in front of robot, facing downwards. DON'T PLACE THEM TOO CLOSE TO THE GROUND BECAUSE THIS MAY CAUSE WRONG READINGS !!!

How it works:
It uses behaviours. If the robot is straight on the line, Forward behaviour is on. If the listener (connected to sensors) detects that robot is going to the left/right too much, it triggers LineCorrect behaviour.
At junctions (detected by a different listener connected to sensors) it triggers JunctionBehaviour. You can choose what to do programatically (from code): turn left/right everytime, randomly assign a direction, or perform moves from a given array.
