package edu.berkeley.eduride.feedbackview.model;


public class TestResult {
	
	private String name;
	private String progress_state;
	private String result;
	private String failure_trace;
	
	public TestResult(String name, String progress_state, String result, String failure_trace){
		this.name = name;
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

	public String getMethodName() {
		return "booyah1";
	}
	
	public boolean hasDescription(){
		return false;
	}

	public String getDescription(){
		return "booyah2";
	}

	public boolean getSuccess(){
		return true;
	}

	public boolean hasExpected(){
		return false;
	}

	public String getExpected(){
		return "booyah3";
	}

	public boolean hasObserved(){
		return false;
	}

	public String getObserved(){
		return "booyah4";
	}

	public String getMethodCall(){
		return "booyah5";
	}

	public boolean hideWhenSuccessful(){
		return false;
	}

	public String getName() {
		return name;
	}
}

