package warehouse.job;

import org.testng.annotations.Test;
import warehouse.util.ItemPickup;
import warehouse.util.Location;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by samtebbs on 28/03/2016.
 */
public class JobInputTest {

    @Test
    public void testLaunch() throws Exception {
        Location dropLocation = new Location(9, 7), aaLocation = new Location(1, 2), abLocation = new Location(1, 3);
        double aaReward = 11.43, aaWeight = 2.06, abReward = 10.11, abWeight = 4.62;
        Job job1 = new Job(dropLocation, Arrays.asList(new ItemPickup("aa", aaLocation, 3, aaReward, aaWeight), new ItemPickup("ab", abLocation, 2, abReward, abWeight)), "0");
        Job job2 = new Job(dropLocation, Arrays.asList(new ItemPickup("aa", aaLocation, 6, aaReward, aaWeight)), "1");
        job2.cancelledInTrainingSet = true;
        List<Job> expected = Arrays.asList(job1, job2);
        List<Job> result = JobInput.launch();
        System.out.println(String.format("Expected: %s%nResult %s%n", expected, result));
        assert result.equals(expected);
    }
}