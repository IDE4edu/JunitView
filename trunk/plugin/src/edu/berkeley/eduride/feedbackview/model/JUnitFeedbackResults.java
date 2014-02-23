package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.junit.model.ITestCaseElement;

// TODO probably should do a JUnitFeedbackResult as well?

public class JUnitFeedbackResults {

	
	HashMap<String, TestResult> results = null;
	
	
	
	private HashMap<String, TestResult> getEmptyTestResults(HashMap<MethodDeclaration, ArrayList<Annotation>> testMethods) {
		HashMap<String, TestResult> testResults = new HashMap<String, TestResult>(); 
		for (Map.Entry<MethodDeclaration, ArrayList<Annotation>> entry : testMethods.entrySet()) {
			String methodName = entry.getKey().getName().getIdentifier();
			testResults.put(methodName, new TestResult(entry.getValue(), methodName));
		}
		return testResults;
	}
	

	
	
	public JUnitFeedbackResults(HashMap<MethodDeclaration, ArrayList<Annotation>> testMethods, ArrayList<ITestCaseElement> resultList) {
		results = getEmptyTestResults(testMethods);
		
		
		for (ITestCaseElement tce : resultList) {
			String methodName = tce.getTestMethodName();
			results.get(methodName).updateModel(tce);
			
			// TODO: need to deal with @Ignore
			// boolean makeTestResult = true;
			// if (tce.getTestResult(true).equals(Result.IGNORED)) {
			// makeTestResult = false;
			// }
			// if (makeTestResult) {
			//
			// }
		}
	}


	
	
	public ArrayList<TestResult> getResults() {
		// TODO Auto-generated method stub
		return new ArrayList<TestResult> (results.values());
	}
	

	
	
}
