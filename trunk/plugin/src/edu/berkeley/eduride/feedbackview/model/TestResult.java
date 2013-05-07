package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.junit.model.ITestElement.Result;


public class TestResult {
	
	private String result;
	private boolean success = false;
	
	private String mMethodName, mMethodCall, mDescription;
	private String expected;
	private String observed;
	private boolean hideWhenSuccessful = false;
	
	public TestResult(ArrayList<Annotation> annotations, String name) {
		mMethodName = name;
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
						mDescription = (String)valuePair.getValue();
					}
				}
			}
			if(annotationName.equals("hideWhenSuccessful")){
				hideWhenSuccessful = true;
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
	
	
	public void updateSuccess(boolean success) {
		this.success = success;
	}
	
	public void updateObserved(String observed){
		this.observed = observed;
	}
	
	public String get_result(){
		return result;
	}
	
	public String getMethodName() {
		return mMethodName;
	}
	
	public boolean hasDescription(){
		return mDescription != null;
	}

	public String getDescription(){
		return mDescription;
	}

	public boolean getSuccess(){
//		if (result == null && hideWhenSuccessful)
//			return true;
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

	public String getMethodCall(){
		return mMethodCall;
	}

	public boolean hideWhenSuccessful(){
		return hideWhenSuccessful;
	}

	public String getName() {
		return getMethodCall();
	}
}

