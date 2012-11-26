package junitview.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;

import junitview.tests.SquareTest;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

public class TestList {
	
	ArrayList<TestResult> mTestResults = new ArrayList<TestResult>();
	ArrayList<Method> mTestMethods = new ArrayList<Method>();
	private Class<?> testClass;
	
	public TestList(Class<?> JUnitTestClass){
		this.testClass = JUnitTestClass;
		Method[] methods = JUnitTestClass.getMethods();
		
		// find all the methods with @Test Annotation
		for (Method method: methods){
			if (isTestMethod(method)) mTestMethods.add(method);
		}
		
		// convert these to TestResults
		// TODO add error checking. For now, we're assuming they all have Names and descriptions
		for (Method method: mTestMethods){
			mTestResults.add(new TestResult(method));
		}
	}
	
	private boolean isTestMethod(Method m){
		Annotation[] annotations = m.getAnnotations();
		for (Annotation annotation: annotations){
			if (annotation.annotationType().getSimpleName().equals("Test")){
				return true;
			}
		}
		return false;
	}
	
	public void runTests(){
		// somehow run stuff
		JUnitCore core = new JUnitCore();  
		MyListener mListerner = new MyListener();
		core.addListener(mListerner);
		for (TestResult test_result : mTestResults){
			Request request = Request.method(testClass, test_result.getName());
			core.run(request);
			if (!mListerner.succeeded.isEmpty()){
				test_result.success = true;
			} else {
				test_result.success = false;
			}
		}
	}
	
	public Object getTestList(){
		return mTestResults;
	}
}
