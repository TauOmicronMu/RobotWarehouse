package gui;

import javafx.scene.control.Button;

public class AssignedJobsVBox extends UnassignedJobsVBox {

	public AssignedJobsVBox(JobsModel model) {
		super("Assigned Jobs", model);
		
	}

	@Override
	public void addButtons() {
		// Create button to cancel jobs TODO: add handler
		Button cancel = new Button("Cancel job");
		cancel.setOnAction(c -> {
			model.cancel(model.findAssignedJob(jobList.getSelectionModel().getSelectedItem()));
		});
		getChildren().add(cancel);

	}

	@Override
	public void setItems() {
		items = readyToBeDisplayed(model.getAssignedList());
		jobList.setItems(items);

	}

}
