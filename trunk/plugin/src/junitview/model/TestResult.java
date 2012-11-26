package junitview.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.notification.Failure;

public class TestResult {
	
	// TODO THIS ASSUMES THERE IS ONLY ONE ASSERT STATEMENT PER @TEST ANNOTATION

	private String mMethodName, mMethodCall, mDescription;
	public boolean success;
	private String expected;
	private String observed;
	private boolean hideWhenSuccessful = false;
	private String failMessage = null;
	private Class<?> JUnitTestClass;

	public TestResult(Class<?> JUnitTestClass, Method testMethod){
		this.JUnitTestClass = JUnitTestClass;
		
		Annotation[] annotations = testMethod.getAnnotations();

		this.mMethodName = testMethod.getName();

		for (Annotation annotation: annotations){
			// TODO this loop is wonky, sez nate
			String mMethodCall = extractAnnotationValue(annotation, "MethodCall", "value");
			if (mMethodCall != null){
				this.mMethodCall = mMethodCall;
				continue;
			}
			String expected = extractAnnotationValue(annotation, "Expected", "value");
			if (expected != null){
				this.expected = expected;
				continue;
			}
			String mDescription = extractAnnotationValue(annotation, "Description", "value");
			if (mDescription != null) {
				this.mDescription = mDescription;
				continue;
			}
			String hideWhenSuccessful = annotation.annotationType().getSimpleName();
			if (hideWhenSuccessful!= null && hideWhenSuccessful.equals("hideWhenSuccessful")) {
				this.hideWhenSuccessful = true;
				continue;
			}
		}
	}

	public void run(){
		
		JUnitCore core = new JUnitCore();  
		MyListener mListener = new MyListener();
		core.addListener(mListener);
		Request request = Request.method(this.JUnitTestClass, mMethodName);
		core.run(request);
		if (!mListener.succeeded.isEmpty()){
			this.success = true;
			this.observed = this.expected;
		} else {
			// There should only be ONE failure
			Failure f = mListener.failed.get(0);
			this.failMessage = f.getMessage();
			
			String regex = " failed expected:<(.*)> but was:<(.*)>";

			Pattern p = Pattern.compile(regex,Pattern.DOTALL);
			Matcher m = p.matcher(failMessage);

			if (m.find()){
				this.expected = m.group(1);
			} else{
				this.observed = null;
			}
			if (m.find()){
				this.observed = m.group(1);
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

	// returns methodCall if it exists, or methodName if not
	public String getName(){
		if (getMethodCall() != null) {
			return getMethodCall();
		} else {
			return getMethodName();
		}
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
}
