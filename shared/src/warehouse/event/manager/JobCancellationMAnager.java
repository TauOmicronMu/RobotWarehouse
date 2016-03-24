package warehouse.event.manager;

import warehouse.event.JobCancellationEvent;
import warehouse.event.listener.JobCancellationListener;

/**
 * Created by sxt567 on 23/03/16.
 */
public class JobCancellationMAnager extends RobotEventManager<JobCancellationListener, JobCancellationEvent> {
    @Override
    public void each(JobCancellationListener jobCancellationListener, JobCancellationEvent jobCancellationEvent) {
        jobCancellationListener.onJobCancellation(jobCancellationEvent);
    }
}
