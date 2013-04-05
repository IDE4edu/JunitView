package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;

import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestRunSession;

public class TestList {
	
	public ArrayList<TestResult> test_results;

	public TestList(ITestRunSession session) {
		test_results = new ArrayList<TestResult>();
		ITestElementContainer container = (ITestElementContainer) session.getChildren()[0];
		ITestElement[] testElements = container.getChildren();
		for (int i= 0; i < testElements.length; i++){
			ITestElement x = testElements[i];
			
			String name = ((ITestCaseElement)x).getTestMethodName();
			String progress_state = x.getProgressState().toString();
			String result = x.getTestResult(true).toString();
			String failure_trace = "";
			if(x.getFailureTrace() != null){
				failure_trace = x.getFailureTrace().toString();
			}
			test_results.add(new TestResult(name, progress_state, result, failure_trace));
		}
		
		// ITestRunSession doesn't have squat
		// ITestRunElement has an array of ITestElements
		// ITestElement has shit we need!
		
		// TODO build test results into here based on constructor
		
	}

}
