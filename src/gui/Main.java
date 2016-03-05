package gui;

import java.util.LinkedList;
import java.util.List;

/**
 * Class that represents the main window. JavaFX graphics work on a tree-like structure.
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import warehouse.action.Action;
import warehouse.action.TurnAction;
import warehouse.job.AssignedJob;
import warehouse.job.Job;
import warehouse.util.ItemPickup;
import warehouse.util.Location;
import warehouse.util.Robot;
import warehouse.util.Route;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			// Create root node as a vertical box and set spacing and padding
			VBox root = new VBox();
			root.setSpacing(60);
			root.setPadding(new Insets(15, 12, 15, 12));

			// Create horizontal box which holds the map VBox and job VBox
			HBox mapAndJobs = new HBox();
			mapAndJobs.setSpacing(60);
			mapAndJobs.setAlignment(Pos.CENTER);

			// Create vertical box which holds the map and it's buttons
			MapVBox map = new MapVBox();
			
			// Create mock job lists for model ----- TEST - remove this code when integrating
			
			LinkedList<ItemPickup> pickups = new LinkedList<ItemPickup>();
			pickups.add(new ItemPickup("cake", new Location(1,2), 3));
			
			Job job1 = new Job(new Location(1,2), pickups);
			Job job2 = new Job(new Location(3,5), pickups);
			Job job3 = new Job(new Location(5,6), pickups);
			
			List<Job> unassigned = new LinkedList<Job>();
			unassigned.add(job1);
			unassigned.add(job2);
			unassigned.add(job3);
			
			List<AssignedJob> assigned = new LinkedList<AssignedJob>();
			
			Robot robot = new Robot("Andrei", new Location(1,2));
			
			TurnAction action = new TurnAction(3.0);
			
			LinkedList<Action> actions = new LinkedList<Action>();
			actions.add(action);
			
			Route route = new Route(actions, new Location(1,2), new Location(3,5));
			
			AssignedJob job4 = new AssignedJob(new Location(3,5), pickups, route, robot);
			assigned.add(job4);
			
			// Create model for Unassigned and Assigned Jobs
			JobsModel model = new JobsModel(unassigned, assigned);

			// Create vertical box which holds unassigned jobs list and it's buttons
			UnassignedJobsVBox unassignedJobs = new UnassignedJobsVBox("Unassigned Jobs", model);
			
			// Create vertical box which holds assigned jobs list and it's buttons
			AssignedJobsVBox assignedJobs = new AssignedJobsVBox(model);
			

			// Add map and jobs vertical boxes to the horizontal box mapAndJobs
			mapAndJobs.getChildren().addAll(map, unassignedJobs, assignedJobs);

			// Create exit button and add handler to close window
			Button exit = new Button("Exit");
			exit.setOnAction(e -> primaryStage.close());

			// Create a new horizontal box for menu buttons and add Exit to it
			HBox buttons = new HBox();
			buttons.getChildren().add(exit);
			buttons.setAlignment(Pos.BOTTOM_CENTER);

			// Add mapAndJobx HBox and buttons HBox to the root VBox
			root.getChildren().add(mapAndJobs);
			root.getChildren().add(buttons);

			// Create a new scene (window) and add CSS styling to it
			Scene scene = new Scene(root, 1000, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setTitle("Management interface");

			// Set the scene to primaryStage
			primaryStage.setScene(scene);

			// And show the primary stage
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
