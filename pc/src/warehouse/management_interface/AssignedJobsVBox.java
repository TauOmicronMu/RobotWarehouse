package warehouse.management_interface;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import warehouse.job.AssignedJob;

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
		items = readyToBeDisplayedA(model.getAssignedList());
		jobList.setItems(items);

	}
	
	/**
	 * Prepare job for display
	 * @param jobList the list of jobs
	 * @return a list of displayable (if that's even a word) items
	 */
	public ObservableList<String> readyToBeDisplayedA(List<AssignedJob> jobList) {
		ArrayList<String> list = new ArrayList<String>();
		
		jobList.forEach(j -> list.add(j.id));
		
		ObservableList<String> items = FXCollections.observableArrayList(list);
		return items;
	}

}
