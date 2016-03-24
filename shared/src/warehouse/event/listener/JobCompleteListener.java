package warehouse.event.listener;

import warehouse.event.JobCompleteEvent;

/**
 * Created by sxt567 on 23/03/16.
 */
public interface JobCompleteListener {
    public void onJobComplete(JobCompleteEvent e);
}
