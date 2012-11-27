package junitview.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.runner.JUnitCore;

public class TestList {

	ArrayList<TestResult> mTestResults = new ArrayList<TestResult>();
	JUnitCore myjunitcore = new JUnitCore();  


	public TestList(Class<?> JUnitTestClass) {
		Method[] methods = JUnitTestClass.getMethods();

		// find all the methods with @Test Annotation
		for (Method method : methods) {
			if (isTestMethod(method)) {
				mTestResults.add(new TestResult(JUnitTestClass, method, myjunitcore));
			}
		}
	}

	private boolean isTestMethod(Method m) {
		Annotation[] annotations = m.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().getSimpleName().equals("Test")) {
				return true;
			}
		}
		return false;
	}


	public void runTests() {
		for (TestResult test_result : mTestResults) {
			test_result.run();
		}
	}

	public ArrayList<TestResult> getTestList() {
		return mTestResults;
	}
}
