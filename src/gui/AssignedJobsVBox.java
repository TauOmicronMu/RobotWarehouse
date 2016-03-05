package gui;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class AssignedJobsVBox extends UnassignedJobsVBox {

	private JobsModel model;
	private ListView<String> jobList;
	private ObservableList<String> items;

	public AssignedJobsVBox(JobsModel model) {
		super("Assigned Jobs", model);

		this.model = model;

	}

	@Override
	public void addButtons() {
		// Create button to cancel jobs TODO: add handler
		Button cancel = new Button("Cancel job");
		cancel.setOnAction(c -> {
			model.cancel(model.findUnassignedJob(jobList.getSelectionModel().getSelectedItem()));
		});
		getChildren().add(cancel);

	}

	@Override
	public void setItems() {
		items = readyToBeDisplayed(model.getAssignedList());

	}

}
