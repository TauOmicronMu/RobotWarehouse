package warehouse.event.manager;

import warehouse.event.JobAssignedEvent;
import warehouse.event.listener.JobAssignedListener;

/**
 * Created by sxt567 on 23/03/16.
 */
public class JobAssignedManager extends RobotEventManager<JobAssignedListener, JobAssignedEvent> {
    @Override
    public void each(JobAssignedListener jobAssignedListener, JobAssignedEvent jobAssignedEvent) {
        jobAssignedListener.onJobAssigned(jobAssignedEvent);
    }
}
