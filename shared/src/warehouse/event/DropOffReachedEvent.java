package warehouse.event;

import warehouse.job.AssignedJob;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class DropOffReachedEvent extends Event {
    public final String str;
    public final AssignedJob job;

    public DropOffReachedEvent(String str, AssignedJob job) {
        super(job.robot);
        this.str = str;
        this.job = job;
    }

}
