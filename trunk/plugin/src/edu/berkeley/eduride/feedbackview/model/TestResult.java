package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;


public class TestResult {
	
	private String result;
	
	private String mMethodName, mMethodCall, mDescription;
	private String expected;
	private String observed;
	private boolean hideWhenSuccessful = false;
	
	public TestResult(ArrayList<Annotation> annotations) {
		//mMethodName = 
		for (Annotation annotation: annotations) {
			IAnnotationBinding binding = annotation.resolveAnnotationBinding();
			IMemberValuePairBinding[] valuePairs = binding.getDeclaredMemberValuePairs();
			//mMethodName = binding.getName();
			String annotationName = binding.getName();
			if(annotationName.equals("MethodCall")){
				for(IMemberValuePairBinding valuePair: valuePairs){
					if(valuePair.getName().equals("value")){
						mMethodCall = (String)valuePair.getValue();
					}
				}
			}
//			String mMethodCall = extractAnnotationValue(annotation, "MethodCall", "value");
//			if (mMethodCall != null){
//				this.mMethodCall = mMethodCall;
//				continue;
//			}
//			String expected = extractAnnotationValue(annotation, "Expected", "value");
//			if (expected != null){
//				this.expected = expected;
//				continue;
//			}
//			String mDescription = extractAnnotationValue(annotation, "Description", "value");
//			if (mDescription != null) {
//				this.mDescription = mDescription;
//				continue;
//			}
//			String hideWhenSuccessful = annotation.annotationType().getSimpleName();
//			if (hideWhenSuccessful!= null && hideWhenSuccessful.equals("hideWhenSuccessful")) {
//				this.hideWhenSuccessful = true;
//				continue;
//			}
		}
	}
	
	public void updateTest(String name, String result){
		this.name = name;
		this.result = result;
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
		return result.equals("OK");
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

