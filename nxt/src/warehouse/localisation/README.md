Localisation in the simulation:
	To get the simulation you need to clone the github repository:
		https://github.com/jokLiu/LocalisationSimulation.git

	-Works using both action and sensor models
	-finds it's position in about up to 6 steps of moving
	-Simulation is in the warehouse.localisation.simulation package 
	-To run simulation we need to run LocalisationMain with the main method
	-Also there is a test class called Test where is the coordinates of the robots position tested

Localisation in real world:
	-Works only by using action model
	-Finds its position almost always( do not find when misses the line) given a direction
	-Innitialy heading is set to PLUS_X but might be changed in the MainClass
	-It is crucial to put the robot in the right direction
	-If wanted to test the localisation on the map you have to run MainClass with the main method (However for the warehouse other methods is going to be used)
	
