package warehouse.event;

import warehouse.job.AssignedJob;

/**
 * Event to send to subscribers when a job is completed.
 * 
 * @author Owen
 *
 */
public class JobCompleteEvent extends Event {

	public final AssignedJob job;

	public JobCompleteEvent(AssignedJob job){
		super(job.robot);
		this.job = job;
	}

	public String toPacketString() {
		String s = "";
		s += "JobComplete";
		s += ",";
		s += robot.toPacketString();
		s += ",";
		s += job.toPacketString();
		return s;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JobCompleteEvent that = (JobCompleteEvent) o;

		return job != null ? job.equals(that.job) : that.job == null;

	}

	@Override
	public int hashCode() {
		return job != null ? job.hashCode() : 0;
	}
}
