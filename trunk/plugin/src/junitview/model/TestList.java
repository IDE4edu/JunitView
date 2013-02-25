package junitview.model;

import java.util.ArrayList;

import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestRunSession;

public class TestList {
	
	public ArrayList<TestResult> test_results;

	public TestList(ITestRunSession session) {
		test_results = new ArrayList<TestResult>();
		ITestElement[] testElements = session.getChildren();
		for (int i= 0; i < testElements.length; i++){
			ITestElement x = testElements[i];
			test_results.add(new TestResult(x.getProgressState().toString(), x.getTestResult(false).toString(), x.getFailureTrace().toString()));
		}
		
		// ITestRunSession doesn't have squat
		// ITestRunElement has an array of ITestElements
		// ITestElement has shit we need!
		
		// TODO build test results into here based on constructor
		
	}

}
