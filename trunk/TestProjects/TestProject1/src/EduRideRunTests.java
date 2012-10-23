import java.util.ArrayList;

import org.junit.internal.TextListener;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

//@RunWith(MyRunner.class)  
public class EduRideRunTests {  

	//	public static void main(String[] args) {  
	//		ArrayList<Result> results = getResults(SquareTest.class); // this returns an ArrayList of Results
	//		// display the results
	//	}
	//
	//	        public static ArrayList<Result> getResults(Class passedTestClass){
	//            JUnitCore core = new JUnitCore();  
	//            core.addListener(new TextListener(System.out));   
	//            core.run(passedTestClass); // This runs all the tests in the class, which we don't want
	//            ArrayList<Test> tests = findTests(passedTestClass); // find the individual test methods... with @test
	//            for (tests : test) { // for each of the tests that you find
	//            	result = checkOn(test); // run the test
	//            	// add result to an array list
	//            	// return array list of results
	//            }

	public static void main(String[] args) {  
		getResults(SquareTest.class); // this returns an ArrayList of Results
	}
	public static void getResults(Class<SquareTest> passedTestClass){
		JUnitCore core = new JUnitCore();  
		MyListener mListerner = new MyListener();
		core.addListener(mListerner);   
		core.run(SquareTest.class);
		for (Description d: mListerner.succeeded){
			System.out.println(d.getDisplayName() + " succeeded!");
		}
		for (Description d: mListerner.failed){
			System.out.println(d.getDisplayName() + " failed!");
		}
	}
}