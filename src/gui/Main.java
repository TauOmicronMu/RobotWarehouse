package gui;

/**
 * Class that represents the main window. JavaFX graphics work on a tree-like structure.
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
			VBox map = new VBox();
			map.setSpacing(10);
			
			// Create vertical box which holds the job list and it's buttons
			VBox jobs = new VBox();
			jobs.setSpacing(10);

			// TODO: Display map instead of this text
			Text mapTODO = new Text("MAP WILL BE HERE");
			mapTODO.setFill(Color.WHITE);

			// Create buttons: 1) showPath - shows path for all robots TODO: add handler and show for one button
			Button showPath = new Button("Show path for all");

			// Add map and button to map VBox
			map.getChildren().addAll(mapTODO, showPath);
			
			// Create the title for Job List section
			Text jobsTitle = new Text("LIST OF JOBS");
			jobsTitle.setFill(Color.WHITE);
			
			// Create the job list - for now, it's entries are hard coded TODO: replace this
			ListView<String> jobList = new ListView<String>();
			ObservableList<String> items = FXCollections.observableArrayList (
			    "Job 1 - TAKEN BY ROBOT 3", "Job 2", "Job 3", "Job 4");
			jobList.setItems(items);
			
			// Create button to cancel jobs TODO: add handler
			Button cancel = new Button("Cancel job");
	
			// Add title, list, button to jobs VBox
			jobs.getChildren().addAll(jobsTitle, jobList, cancel);
			
			// Add map and jobs vertical boxes to the horizontal box mapAndJobs
			mapAndJobs.getChildren().add(map);
			mapAndJobs.getChildren().add(jobs);
			
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
			Scene scene = new Scene(root, 500, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			// Set it's outer toolbar (the default one with Title, Exit, Minimize etc. buttons as transparent) - Looks better
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			
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
