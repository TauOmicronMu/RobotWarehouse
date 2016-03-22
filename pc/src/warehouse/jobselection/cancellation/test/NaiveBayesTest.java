package warehouse.jobselection.cancellation.test;

import java.io.IOException;

/**
 * Created by Owen on 21/03/2016.
 */
public class NaiveBayesTest {

    public static void main(String[] args){

        String[] files1 = new String[5];
        files1[0] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\1\\locations.csv";
        files1[1] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\1\\items.csv";
        files1[2] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\1\\jobs.csv";
        files1[3] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\1\\cancellations.csv";
        files1[4] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\1\\drops.csv";

        String[] files2 = new String[5];
        files2[0] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\2\\locations.csv";
        files2[1] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\2\\items.csv";
        files2[2] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\2\\jobs.csv";
        files2[3] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\2\\cancellations.csv";
        files2[4] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\2\\drops.csv";

        String[] files3 = new String[5];
        files3[0] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\3\\locations.csv";
        files3[1] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\3\\items.csv";
        files3[2] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\3\\jobs.csv";
        files3[3] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\3\\cancellations.csv";
        files3[4] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\3\\drops.csv";

        String[] files4 = new String[5];
        files4[0] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\4\\locations.csv";
        files4[1] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\4\\items.csv";
        files4[2] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\4\\jobs.csv";
        files4[3] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\4\\cancellations.csv";
        files4[4] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\4\\drops.csv";

        String[] files5 = new String[5];
        files5[0] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\5\\locations.csv";
        files5[1] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\5\\items.csv";
        files5[2] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\5\\jobs.csv";
        files5[3] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\5\\cancellations.csv";
        files5[4] = "C:\\Users\\Owen\\Documents\\cssync\\robot_programming\\Team Project Files\\RobotWarehouse\\pc\\src\\warehouse\\jobselection\\cancellation\\test\\5\\drops.csv";

        try {
            CancellationMachineTester tester1 = new CancellationMachineTester(0.95, files1, files3, files4, files5);
            CancellationMachineTester tester2 = new CancellationMachineTester(0.95, files2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
