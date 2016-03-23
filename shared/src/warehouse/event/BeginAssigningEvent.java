package warehouse.event;

import warehouse.job.Job;
import warehouse.util.Location;

import java.util.List;

/**
 * Event to be passed when the robot is ready to be assigned jobs
 * 
 * @author Owen
 *
 */
public class BeginAssigningEvent extends Event {
	
	public List<Job> jobs;

    @Deprecated
	public List<Location> dropLocations;

	/**
	 * Create a new event
	 */
    @Deprecated
	public BeginAssigningEvent(List<Job> jobs, List<Location> dropLocations){
		super(null);
		this.jobs = jobs;
		this.dropLocations = dropLocations;
	}

    public BeginAssigningEvent(List<Job> jobs) {
        super(null);
        this.jobs = jobs;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BeginAssigningEvent that = (BeginAssigningEvent) o;

		if (!jobs.equals(that.jobs)) return false;
		return dropLocations.equals(that.dropLocations);

	}

	@Override
	public int hashCode() {
		int result = jobs.hashCode();
		result = 31 * result + dropLocations.hashCode();
		return result;
	}

	public String toPacketString() {
		String s = "";
		s += "BeginAssigning";
		s += ",";
		s += jobs.size();
		s += ",";
		for(Job j : jobs) {
		    s += j.toPacketString();
		}
		return s;
	}
}
