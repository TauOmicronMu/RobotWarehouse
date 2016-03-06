package warehouse.assign.tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

	public static void main(String[] args){
		
		System.out.println("Running Job Selection Tests:");
		
		Result result = JUnitCore.runClasses(JobSelectionTests.class);
		
		for(Failure failure: result.getFailures()){
			
			System.out.print("Failure: ");
			System.out.println(failure.toString());
		}
		
		System.out.println(result.wasSuccessful() ? "Tests Passed." : "Tests Failed.");
	}
}
