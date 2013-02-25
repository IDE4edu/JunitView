package junitview.model;

import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestElement.FailureTrace;
import org.eclipse.jdt.junit.model.ITestElement.ProgressState;
import org.eclipse.jdt.junit.model.ITestElement.Result;

public class TestResult {
	
	private String progress_state;
	
	private String result;
	
	private String failure_trace;
	
	public TestResult(String progress_state, String result, String failure_trace){
		this.progress_state = progress_state;
		this.result = result;
		this.failure_trace = failure_trace;
	}
	
	public String get_progress_state(){
		return progress_state;
	}
	
	public String get_result(){
		return result;
	}
	
	public String get_failure_trace(){
		return failure_trace;
	}
}
