package warehouse.management_interface;

import javafx.scene.control.Button;

public class AssignedJobsVBox extends UnassignedJobsVBox {

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public AssignedJobsVBox(JobsModel model) {
		super("Assigned Jobs", model);

	}

	/**
	 * Add buttons for this VBox
	 */
	@Override
	public void addButtons() {
		// Create button to cancel jobs
		Button cancel = new Button("Cancel job");
		cancel.setOnAction(c -> {
			try {
				model.cancel(model.findAssignedJob(jobList.getSelectionModel().getSelectedItem()));
			}
			// Deal with no selection
			catch (NullPointerException npe) {
				showNoJobError();
			}

		});
		getChildren().add(cancel);

		addShowInfoButton(false);

	}

	/**
	 * Set the items (assigned jobs) of the job list
	 */
	@Override
	public void setItems() {
		items = readyToBeDisplayed(model.getAssignedList());
		jobList.setItems(items);

	}

}
