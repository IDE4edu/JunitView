package junitview.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestResult {
	
	private String mName;
	private String mMethodCall;
	private String mDescription;
	public boolean success;
	private String expected;
	private String observed;
	private boolean hideWhenSuccessful = false;
	
	public TestResult(Method testMethod){
		Annotation[] annotations = testMethod.getAnnotations();
		
		this.mName = testMethod.getName();
		
		for (Annotation annotation: annotations){
			String mMethodCall = extractAnnotationValue(annotation, "MethodCall", "value");
			if (mMethodCall != null){
				this.mMethodCall = mMethodCall;
				continue;
			}
			String mDescription = extractAnnotationValue(annotation, "Description", "value");
			if (mDescription != null) {
				this.mDescription = mDescription;
				continue;
			}
			String hideWhenSuccessful = extractAnnotationValue(annotation, "hideWhenSuccessful", "value");
			if (hideWhenSuccessful!= null && hideWhenSuccessful.trim().toLowerCase().equals("true")) {
				this.hideWhenSuccessful = true;
				continue;
			}
		}
	}
	
	private String extractAnnotationValue(Annotation annotation, String annotationName, String attributeName) {
		if (annotation.annotationType().getSimpleName().equals(annotationName)){
			String rtn = null;
			try {
				rtn = (String) annotation.annotationType().getMethod(attributeName).invoke(annotation);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			return rtn;
		} else {
			return null;
		}
	}
	
	public String getName(){
		return mName;
	}
	
	public boolean hasDescription(){
		return mDescription != null;
	}
	
	public String getDescription(){
		return mDescription;
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
	
	public String getMethodCall(){
		return mMethodCall;
	}
	
	public boolean hideWhenSuccessful(){
		// if the annotation for hideWhenSuccessful exists, return true, otherwise false
		return false;
	}
}
