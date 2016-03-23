package warehouse.event;

import warehouse.job.AssignedJob;

/**
 * Created by samtebbs on 22/03/2016.
 */
public class DropOffReachedEvent extends Event {
    @Deprecated
    public String str;

    public final AssignedJob job;

    @Deprecated
    public DropOffReachedEvent(String str, AssignedJob job) {
        super(job.robot);
        this.str = str;
        this.job = job;
    }

    public DropOffReachedEvent(AssignedJob job) {
        super(job.robot);
        this.job = job;
    }

    public String toPacketString() {
        String s = "";
        s += "DropOffReached";
        s += ",";
        s += job.toPacketString();
        return s;
    }
}
