package warehouse.event;

import warehouse.job.AssignedJob;

public class JobCancellationEvent extends Event {

    public final AssignedJob job;

    public JobCancellationEvent(AssignedJob job) {
        super(job.robot);
        this.job = job;
    }

    public String toPacketString() {
        String s = "";
        s += "JobCancellation,";
        s += super.toPacketString();
        s += ",";
        s += job.toPacketString();
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobCancellationEvent that = (JobCancellationEvent) o;

        return job != null ? job.equals(that.job) : that.job == null;

    }

    @Override
    public int hashCode() {
        return job != null ? job.hashCode() : 0;
    }
}
