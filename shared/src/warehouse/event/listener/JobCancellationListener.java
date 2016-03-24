package warehouse.event.listener;

import warehouse.event.JobCancellationEvent;

/**
 * Created by sxt567 on 23/03/16.
 */
public interface JobCancellationListener {
    public void onJobCancellation(JobCancellationEvent e);
}
