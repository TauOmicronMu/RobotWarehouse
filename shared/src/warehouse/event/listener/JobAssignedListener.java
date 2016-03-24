package warehouse.event.listener;

import warehouse.event.JobAssignedEvent;

/**
 * Created by sxt567 on 23/03/16.
 */
public interface JobAssignedListener {
    public void onJobAssigned(JobAssignedEvent e);
}
