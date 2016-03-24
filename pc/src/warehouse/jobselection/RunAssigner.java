package warehouse.jobselection;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import warehouse.event.BeginAssigningEvent;
import warehouse.event.JobCancellationEvent;
import warehouse.event.JobCompleteEvent;
import warehouse.job.AssignedJob;
import warehouse.job.Job;
import warehouse.jobselection.event.SelectorHasCurrentJobEvent;
import warehouse.util.EventDispatcher;
import warehouse.util.Location;
import warehouse.util.Robot;
import warehouse.util.Subscriber;

/**
 * Created by Owen on 22/03/2016.
 */
public class RunAssigner  extends Thread{

    private List<Job> trainingJobs;
    private List<Job> actualJobs;
    private boolean hasCurrentJob;
	private Robot robot;
	private LinkedList<AssignedJob> finalList;
	private int firstCancelled;
	
    public RunAssigner(Robot robot, List<Job> trainingJobs, List<Job> actualJobs){

    	trainingJobs = new LinkedList<>(trainingJobs);
    	actualJobs = new LinkedList<>(actualJobs);
    	
        this.trainingJobs = trainingJobs;
        this.actualJobs = actualJobs;

        this.robot = robot;
        
        EventDispatcher.subscribe2(this);
        
        this.start();
    }

    @Override
    public void run() {

            JobAssignerSingle assigner = new JobAssignerSingle(this.robot, new LinkedList<>(this.trainingJobs));

            EventDispatcher.onEvent2(new BeginAssigningEvent(this.actualJobs, new LinkedList<Location>()));

            for (int i = 0; i < actualJobs.size(); i++) {

                while(!this.hasCurrentJob) {

                    try {

                        //System.out.println("\nTEST THREAD: Sleeping");
                        Thread.sleep(100);
                        //System.out.println("\nTEST THREAD: Woke up");
                    } catch (InterruptedException e) {
                        // Sleep was interrupted for some reason
                        e.printStackTrace();
                    }
                }
                
                    assert (this.hasCurrentJob == true);
                    assert (assigner.getCurrentJob() != null);

                    EventDispatcher.onEvent2(new JobCompleteEvent(assigner.getCurrentJob()));
                    
                this.hasCurrentJob = false;
            }
            
            try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				//Auto-generated catch block
				e.printStackTrace();
			}
            
            this.finalList = assigner.getFinalList();
            
            assigner.stopAssigning();
    }

    public List<AssignedJob> getFinalList(){
    	
    	return this.finalList;
    }
    
    @Subscriber
    public void onHasCurrentJobEvent(SelectorHasCurrentJobEvent e){
        
        this.hasCurrentJob = true;
    }
    
    public static void parseFile(String filePath, Consumer<String[]> consumer) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filePath));
        while(in.hasNextLine()){
            String line = in.nextLine();
            if(!line.isEmpty()) consumer.accept(line.trim().split(","));
        }
        
        in.close();
    }
    
    public static void print(Object o){

        System.out.println(o);
    }
}
