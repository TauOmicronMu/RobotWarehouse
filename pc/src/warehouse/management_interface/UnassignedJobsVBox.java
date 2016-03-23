package warehouse.management_interface;
/**
 * Graphic element that displays unassigned jobs and interacts with user
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class UnassignedJobsVBox extends VBox implements Observer {
	public ListView<String> jobList;
	public ObservableList<String> items;
	public JobsModel model;
	
	/**
	 * Constructor. Initialise everything and start observing the model
	 * @param title title of section
	 * @param model
	 */
	public UnassignedJobsVBox(String title, JobsModel model) {
		super();
		
		this.model = model;
		model.addObserver(this);

		setSpacing(10);

		// Create the title for this section
		Text jobsTitle = new Text(title);
		jobsTitle.setFill(Color.WHITE);

		// Create the job list - for now, it's entries are hard coded TODO:
		// replace this
		jobList = new ListView<String>();
		setItems();

		// Add title, list to jobs VBox
		getChildren().addAll(jobsTitle, jobList);
		
		// Add buttons
		addButtons();

	}
	
	/**
	 * Add buttons to this VBox
	 */
	public void addButtons() {
		addShowInfoButton(true);

	}
	
	/**
	 * Add a button that shows information about a job
	 * @param searchInUnassigned true if the job is unassigned, else false
	 */
	public void addShowInfoButton(boolean searchInUnassigned){
		
		Button info = new Button("Show info");
		info.setOnAction(i -> {
			// Try to display the job's info
			try {
				Alert infoAboutJob = new Alert(AlertType.INFORMATION);
				
				infoAboutJob.setTitle("Job Information");
				infoAboutJob.setHeaderText("Information about <JOB IDENTIFIER>");
				
				String contentText = new String();
				
				if(searchInUnassigned) 
					contentText = model.findUnassignedJob(jobList.getSelectionModel().getSelectedItem()).toString();
				else contentText = model.findAssignedJob(jobList.getSelectionModel().getSelectedItem()).toString();
				
				infoAboutJob.setContentText(contentText);
				infoAboutJob.show();
			}
			// Deal with no selection
			catch (NullPointerException npe) {
				showNoJobError();
			}

		});
		getChildren().add(info);
	}
	
	/**
	 * Shows an error when no job is selected
	 */
	public void showNoJobError(){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText("No job selected");
		alert.show();
	}

	/**
	 * Update when model changes
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		setItems();
		
	}
	
	/**
	 * Set items of job list
	 */
	public void setItems() {
		items = readyToBeDisplayed(model.getUnassignedList());
		jobList.setItems(items);
		
	}

	/**
	 * Prepare job for display
	 * @param jobList the list of jobs
	 * @return a list of displayable (if that's even a word) items
	 */
	public <A> ObservableList<String> readyToBeDisplayed(List<A> jobList) {
		ArrayList<String> list = new ArrayList<String>();
		
		jobList.forEach(j -> list.add(j.toString()));
		
		ObservableList<String> items = FXCollections.observableArrayList(list);
		return items;
	}

}
