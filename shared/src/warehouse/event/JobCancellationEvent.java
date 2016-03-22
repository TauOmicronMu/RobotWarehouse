package warehouse.event;

import warehouse.job.*;

public class JobCancellationEvent extends Event {

    public final AssignedJob job;

    public JobCancellationEvent(AssignedJob job) {
        super(job.robot);
        this.job = job;
    }
}
