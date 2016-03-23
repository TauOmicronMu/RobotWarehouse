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
        s += "JobCancellation";
        s += ",";
        s += job.toPacketString();
        return s;
    }
}
