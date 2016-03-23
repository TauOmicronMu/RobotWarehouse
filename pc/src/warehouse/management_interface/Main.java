package warehouse.management_interface;

/**
 * The main management interface class that can be launched from outside
 */

import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import warehouse.job.AssignedJob;
import warehouse.job.Job;

public class Main extends Application {
	private static List<Job> unassigned;
	private static List<AssignedJob> assigned;
	
	private static List<String> robotNames;
	
	/**
	 * Set parameters for management GUI
	 * @param unassigned Unassigned Jobs list
	 * @param assigned Assigned Jobs list
	 * @param numberOfRobots The number of robots
	 * @param robotNames An ArrayList containing the names of the robots
	 */
	public static void setParameters(List<Job> unassigned, List<AssignedJob> assigned, List<String> robotNames) {
		Main.unassigned = unassigned;
		Main.assigned = assigned;
	
		Main.robotNames = robotNames;
	}
	
	
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
			MapVBox map = new MapVBox(robotNames);

			// Create model for Unassigned and Assigned Jobs
			JobsModel model = new JobsModel(unassigned, assigned);

			// Create vertical box which holds unassigned jobs list and it's
			// buttons
			UnassignedJobsVBox unassignedJobs = new UnassignedJobsVBox("Unassigned Jobs", model);

			// Create vertical box which holds assigned jobs list and it's
			// buttons
			AssignedJobsVBox assignedJobs = new AssignedJobsVBox(model);

			// Add map and jobs vertical boxes to the horizontal box mapAndJobs
			mapAndJobs.getChildren().addAll(map, unassignedJobs, assignedJobs);

			// Create exit button and add handler to close window
			Button exit = new Button("Exit Management Interface");
			exit.setOnAction(e -> primaryStage.close());

			// Create a new horizontal box for menu buttons and add Exit to it
			HBox buttons = new HBox();

			buttons.getChildren().add(exit);

			buttons.setAlignment(Pos.BOTTOM_RIGHT);
			buttons.setSpacing(50);

			// Add mapAndJobx HBox and buttons HBox to the root VBox
			root.getChildren().add(mapAndJobs);
			root.getChildren().add(buttons);

			// Create a new scene (window) and add CSS styling to it
			Scene scene = new Scene(root, 1000, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setTitle("Management interface");

			// Set the scene to primaryStage
			primaryStage.setScene(scene);

			// Hide toolbar
			primaryStage.initStyle(StageStyle.TRANSPARENT);

			// And show the primary stage
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start Management GUI
	 */
	public static void startGUI() {
		launch();
	}
}
