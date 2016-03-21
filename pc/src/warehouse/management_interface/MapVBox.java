package warehouse.management_interface;

/**
 * Graphic element that displays warehouse map
 */
import java.awt.Dimension;
import java.util.ArrayList;

import javafx.embed.swing.SwingNode;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lejos.robotics.RangeFinder;
import rp.robotics.MobileRobotWrapper;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.simulation.MovableRobot;
import rp.robotics.simulation.SimulatedRobots;
import rp.robotics.testing.TestMaps;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.robotics.visualisation.MapVisualisationComponent;

public class MapVBox extends VBox {
	private ArrayList<SimController> controllers;

	/**
	 * Constructor. Initialise everything
	 */
	public MapVBox() {
		super();

		setSpacing(10);

		Text mapTitle = new Text("Map");
		mapTitle.setFill(Color.WHITE);
		getChildren().add(mapTitle);

		// Add and display map
		warehouseMap();

		// Create buttons: 1) showPath - shows path for all robots TODO: add
		// handler and show for one button
		Button showPath = new Button("Show path for all");
		showPath.setOnAction(e -> System.out.println("This will show path"));

		getChildren().add(showPath);

	}

	/**
	 * Builds and displays the map visualisation. Taken from Nick's classes
	 */
	public void warehouseMap() {

		GridMap map = TestMaps.warehouseMap();

		// Create the simulation using the given map. This simulation can run
		// without a GUI.
		MapBasedSimulation sim = new MapBasedSimulation(map);

		// Add a robot of a given configuration to the simulation. The return
		// value is the object you can use to control the robot. //

		// Initialise list of controllers
		controllers = new ArrayList<SimController>();

		int robots = 1;
		for (int i = 0; i < robots; i++) {
			// Starting point on the grid
			GridPose gridStart = new GridPose(3 * i, 0, Heading.PLUS_Y);

			MobileRobotWrapper<MovableRobot> wrapper = sim.addRobot(SimulatedRobots.makeConfiguration(false, true),
					map.toPose(gridStart));

			RangeFinder ranger = sim.getRanger(wrapper);

			controllers.add(new SimController(wrapper.getRobot(), map, gridStart, ranger));

			new Thread(controllers.get(i)).start();
		}

		GridMapVisualisation viz = new GridMapVisualisation(map, sim.getMap());

		MapVisualisationComponent.populateVisualisation(viz, sim);

		// Wrap viz in SwingNode so it can be added
		SwingNode vizNode = new SwingNode();
		vizNode.setContent(viz);

		viz.setPreferredSize(new Dimension(500, 320));

		// Add the visualisation to display it
		this.getChildren().add(vizNode);

	}

	public SimController getController(int index) {
		return controllers.get(index);
	}

}
