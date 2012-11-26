package junitview.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class TestList {

	ArrayList<TestResult> mTestResults = new ArrayList<TestResult>();
	ArrayList<Method> mTestMethods = new ArrayList<Method>();

	public TestList(Class<?> JUnitTestClass) {
		Method[] methods = JUnitTestClass.getMethods();

		// find all the methods with @Test Annotation
		for (Method method: methods){
			if (isTestMethod(method)) mTestMethods.add(method);
		}

		// convert these to TestResults
		// TODO add error checking. For now, we're assuming they all have Names and descriptions
		for (Method method: mTestMethods){
			mTestResults.add(new TestResult(JUnitTestClass, method));
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
		for (TestResult test_result : mTestResults){
			test_result.run();
		}
	}

	public Object getTestList(){
		return mTestResults;
	}
}
