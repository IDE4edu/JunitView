package junitview.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;

public class TestList {
	
	HashSet mTestResults;
	ArrayList<Method> mtestMethods = new ArrayList<Method>();
	
	public TestList(Class<?> JUnitTestClass){
		Method[] methods = JUnitTestClass.getMethods();
		for (Method method: methods){
			if (isTestMethod(method)) mtestMethods.add(method);
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
	
	String getName(){return "";};
	void setName(){};
}
