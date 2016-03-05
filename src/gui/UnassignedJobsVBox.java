package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class UnassignedJobsVBox extends VBox implements Observer {
	private ListView<String> jobList;
	private ObservableList<String> items;
	private JobsModel model;
	

	public UnassignedJobsVBox(String title, JobsModel model) {
		super();
		
		this.model = model;

		setSpacing(10);

		// Create the title for this section
		Text jobsTitle = new Text(title);
		jobsTitle.setFill(Color.WHITE);

		// Create the job list - for now, it's entries are hard coded TODO:
		// replace this
		jobList = new ListView<String>();
		jobList.setItems(items);

		// Add title, list to jobs VBox
		getChildren().addAll(jobsTitle, jobList);
		
		// Add buttons
		addButtons();

	}

	public void addButtons() {
		
//		// Create button to assign jobs TODO: add handler
//		Button assign = new Button("Assign job");
//		assign.setOnAction(a -> {
//			model.assign(model.findAssignedJob(jobList.getSelectionModel().getSelectedItem()).getKey(),
//					model.findAssignedJob(jobList.getSelectionModel().getSelectedItem()).getValue());
//		});
//
//		getChildren().add(assign);

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		setItems();
		
	}

	public void setItems() {
		items = readyToBeDisplayed(model.getUnassignedList());
		
	}

	public <A> ObservableList<String> readyToBeDisplayed(List<A> jobList) {
		ArrayList<String> list = new ArrayList<String>();
		
		jobList.forEach(j -> list.add(j.toString()));
		
		ObservableList<String> items = FXCollections.observableArrayList(list);
		return items;
	}

}
