package warehouse.jobselection.cancellation.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Owen on 21/03/2016.
 */
public class NaiveBayesTest {

    public static void main(String[] args) throws URISyntaxException{

//    	URL url = NaiveBayesTest.class.getResource("actual\\cancellations.csv");
//    	
//    	System.out.println(url);
//    	
//    	System.exit(0);
//    	
//        String[] files1 = new String[5];
//        files1[0] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\out\\warehouse\\jobselection\\cancellation\\test\\1\\locations.csv";
//        files1[1] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\out\\warehouse\\jobselection\\cancellation\\test\\1\\items.csv";
//        files1[2] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\out\\warehouse\\jobselection\\cancellation\\test\\1\\jobs.csv";
//        files1[3] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\out\\warehouse\\jobselection\\cancellation\\test\\1\\cancellations.csv";
//        files1[4] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\out\\warehouse\\jobselection\\cancellation\\test\\1\\drops.csv";

    	URI[] files1 = new URI[5];
    	files1[0] = NaiveBayesTest.class.getResource("1\\locations.csv").toURI();
    	files1[1] = NaiveBayesTest.class.getResource("1\\items.csv").toURI();
    	files1[2] = NaiveBayesTest.class.getResource("1\\jobs.csv").toURI();
    	files1[3] = NaiveBayesTest.class.getResource("1\\cancellations.csv").toURI();
    	files1[4] = NaiveBayesTest.class.getResource("1\\drops.csv").toURI();
    	
        URI[] files2 = new URI[5];
        files2[0] = NaiveBayesTest.class.getResource("2\\locations.csv").toURI();
    	files2[1] = NaiveBayesTest.class.getResource("2\\items.csv").toURI();
    	files2[2] = NaiveBayesTest.class.getResource("2\\jobs.csv").toURI();
    	files2[3] = NaiveBayesTest.class.getResource("2\\cancellations.csv").toURI();
    	files2[4] = NaiveBayesTest.class.getResource("2\\drops.csv").toURI();
    	
        URI[] files3 = new URI[5];
        files3[0] = NaiveBayesTest.class.getResource("3\\locations.csv").toURI();
    	files3[1] = NaiveBayesTest.class.getResource("3\\items.csv").toURI();
    	files3[2] = NaiveBayesTest.class.getResource("3\\jobs.csv").toURI();
    	files3[3] = NaiveBayesTest.class.getResource("3\\cancellations.csv").toURI();
    	files3[4] = NaiveBayesTest.class.getResource("3\\drops.csv").toURI();
    	
        URI[] files4 = new URI[5];
        files4[0] = NaiveBayesTest.class.getResource("4\\locations.csv").toURI();
    	files4[1] = NaiveBayesTest.class.getResource("4\\items.csv").toURI();
    	files4[2] = NaiveBayesTest.class.getResource("4\\jobs.csv").toURI();
    	files4[3] = NaiveBayesTest.class.getResource("4\\cancellations.csv").toURI();
    	files4[4] = NaiveBayesTest.class.getResource("4\\drops.csv").toURI();
    	
        URI[] files5 = new URI[5];
        files5[0] = NaiveBayesTest.class.getResource("5\\locations.csv").toURI();
    	files5[1] = NaiveBayesTest.class.getResource("5\\items.csv").toURI();
    	files5[2] = NaiveBayesTest.class.getResource("5\\jobs.csv").toURI();
    	files5[3] = NaiveBayesTest.class.getResource("5\\cancellations.csv").toURI();
    	files5[4] = NaiveBayesTest.class.getResource("5\\drops.csv").toURI();
    	
        URI[] trainingSet = new URI[5];
        trainingSet[0] = NaiveBayesTest.class.getResource("actual\\locations.csv").toURI();
    	trainingSet[1] = NaiveBayesTest.class.getResource("actual\\items.csv").toURI();
    	trainingSet[2] = NaiveBayesTest.class.getResource("actual\\training_jobs.csv").toURI();
    	trainingSet[3] = NaiveBayesTest.class.getResource("actual\\cancellations.csv").toURI();
    	trainingSet[4] = NaiveBayesTest.class.getResource("actual\\drops.csv").toURI();
    	
        URI[] testSet = new URI[5];
        testSet[0] = NaiveBayesTest.class.getResource("actual\\locations.csv").toURI();
    	testSet[1] = NaiveBayesTest.class.getResource("actual\\items.csv").toURI();
    	testSet[2] = NaiveBayesTest.class.getResource("actual\\jobs.csv").toURI();
    	testSet[3] = NaiveBayesTest.class.getResource("actual\\marking_file.csv").toURI();
    	testSet[4] = NaiveBayesTest.class.getResource("actual\\drops.csv").toURI();
    	
        try {
        	
            new CancellationMachineTester(0.95, files1, files2, files3, files4, files5);
            
            new CancellationActualTester(trainingSet, testSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
