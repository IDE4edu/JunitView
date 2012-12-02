package junitview.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestResult {
	
	// TODO THIS ASSUMES THERE IS ONLY ONE ASSERT STATEMENT PER @TEST ANNOTATION

	private String mMethodName, mMethodCall, mDescription;
	public boolean success;
	private String expected;
	private String observed;
	private boolean hideWhenSuccessful = false;
	private String failMessage = null;
	private java.lang.Throwable exception = null;
	
	private Class<?> JUnitTestClass;
	private JUnitCore listJUnitCore;
	private Request myRequest;
	
	private final String assertArrayEqualsRegex = "arrays first differed at element \\[[0-9]+\\]; expected:<(.*)> but was:<(.*)>";
	private final String assertEqualsRegex = "expected:<(.*)> but was:<(.*)>";
	private final String assertSameRegex = "expected same:<(.*)> was not:<(.*)>";
	private final String[] regexArray = { assertArrayEqualsRegex, assertEqualsRegex, assertSameRegex };

	public TestResult(Class<?> JUnitTestClass, Method testMethod, JUnitCore listjunitcore) {
		// TODO add error checking. For now, we're assuming they all
		// have Names and descriptions
		
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
		
		this.listJUnitCore = listjunitcore;
		this.myRequest = Request.method(JUnitTestClass, mMethodName);
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


	
	public void run(){
		// run the single test
		Result res = listJUnitCore.run(myRequest);
		
		if (res.wasSuccessful()) {
			this.success = true;
			this.observed = this.expected;
		} else if(res.getFailureCount() != 1) {
			System.err.println("TestResult " + mMethodName + " had more than 1 error");
			System.exit(1);
		} else {
			// There should only be ONE failure
			Failure f = res.getFailures().get(0);
			
			this.exception = f.getException();
			
			this.failMessage = f.getMessage();
			if (failMessage == null){
				// this is assertTrue, assertFalse, assertNull, assertNotNull, fail
				// without a message. So return.
				return;
			}

			for (String regex : regexArray){
				Pattern p = Pattern.compile(regex,Pattern.DOTALL);
				Matcher m = p.matcher(failMessage);

				if (m.find()){
					this.expected = m.group(1);
					this.observed = m.group(2);
					// if it finds a match, stop looking.
					continue;
				}
			}
		}
	}

	
	
	
	////  getters
	
	
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
