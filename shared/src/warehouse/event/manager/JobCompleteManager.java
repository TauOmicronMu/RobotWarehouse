package warehouse.event.manager;

import warehouse.event.JobCompleteEvent;
import warehouse.event.listener.JobCompleteListener;

/**
 * Created by sxt567 on 23/03/16.
 */
public class JobCompleteManager extends RobotEventManager<JobCompleteListener, JobCompleteEvent> {
    @Override
    public void each(JobCompleteListener jobCompleteListener, JobCompleteEvent jobCompleteEvent) {
        jobCompleteListener.onJobComplete(jobCompleteEvent);
    }
}
