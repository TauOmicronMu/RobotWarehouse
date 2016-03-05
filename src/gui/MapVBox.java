package gui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MapVBox extends VBox {
	public MapVBox() {
		super();

		setSpacing(10);
		
		Text mapTitle = new Text("Map");
		mapTitle.setFill(Color.WHITE);

		// TODO: Display map instead of this text
		Text mapTODO = new Text("MAP WILL BE HERE");
		mapTODO.setFill(Color.WHITE);

		// Create buttons: 1) showPath - shows path for all robots TODO: add
		// handler and show for one button
		Button showPath = new Button("Show path for all");
		showPath.setOnAction(e -> System.out.println("This will show path"));
		

		// Add map and button to map VBox
		getChildren().addAll(mapTitle, mapTODO, showPath);

	}

}
