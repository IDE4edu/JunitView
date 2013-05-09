package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement.FailureTrace;
import org.eclipse.jdt.junit.model.ITestElement.Result;


public class TestResult {
	
	private boolean success = false;
	
	private String name;
	private String description;
	private String failmessage;
	private String successmessage;
	private String expected;
	private String observed;
	private boolean hideWhenSuccessful = false;
	
	public TestResult(ArrayList<Annotation> annotations, String methodname, ITestCaseElement tce) {
		name = methodname;
		for (Annotation annotation: annotations) {
			IAnnotationBinding binding = annotation.resolveAnnotationBinding();
			IMemberValuePairBinding[] valuePairs = binding.getDeclaredMemberValuePairs();
			//name = binding.getName();
			String annotationName = binding.getName();
			if(annotationName.equals("MethodCall")||annotationName.equals("Name")){
				for(IMemberValuePairBinding valuePair: valuePairs){
					if(valuePair.getName().equals("value")){
						name = (String)valuePair.getValue();
					}
				}
			}
			if(annotationName.equals("Expected")){
				for(IMemberValuePairBinding valuePair: valuePairs){
					if(valuePair.getName().equals("value")){
						expected = (String)valuePair.getValue();
					}
				}
			}
			if(annotationName.equals("Description")){
				for(IMemberValuePairBinding valuePair: valuePairs){
					if(valuePair.getName().equals("value")){
						description = (String)valuePair.getValue();
					}
				}
			}
			if(annotationName.equals("SuccessMessage")){
				for(IMemberValuePairBinding valuePair: valuePairs){
					if(valuePair.getName().equals("value")){
						successmessage = (String)valuePair.getValue();
					}
				}
			}
			if(annotationName.equals("hideWhenSuccessful")){
				hideWhenSuccessful = true;
			}
		}
		if (description == null) {
			description = name;
		}
		FailureTrace failuretrace = tce.getFailureTrace();
		if(failuretrace == null){
			success = true;
		} else {
			failmessage = getFailureMessage(failuretrace);
			if (!hasExpected()){
				// won't be here if there was an @Expected annotation, which takes precedence
				expected = failuretrace.getExpected();
			}
			observed = failuretrace.getActual();
			success = false;
		}
	}
	
	private String getFailureMessage(FailureTrace failuretrace){
		String trace = failuretrace.getTrace();
		int colon_index = trace.indexOf(":");
		int newline_index = trace.indexOf("\n");
		trace = trace.substring(colon_index+1, newline_index);
		return trace.trim();
	}
	
	public void updateSuccess(boolean success) {
		this.success = success;
	}
	
	public void updateObserved(String observed){
		this.observed = observed;
	}

	public String getDescription(){
		return description;
	}

	public boolean hasMessage(){
		if (success) {
			return successmessage != null;
		}
		return failmessage != null;
	}
	
	public String getMessage(){
		if (success) {
			return successmessage;
		}
		return failmessage;
	}
	
	public boolean getSuccess(){
		return success;
	}

	public boolean hasExpected(){
		return expected != null;
	}

	public String getExpected(){
		return expected;
	}

	public boolean hasObserved(){
		return observed != null;
	}

	public String getObserved(){
		return observed;
	}

	public boolean hideWhenSuccessful(){
		return hideWhenSuccessful;
	}

	public String getName() {
		return name;
	}
}

