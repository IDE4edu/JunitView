package junitview.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestResult {
	
	private String mName;
	private String mDescription;
	
	public TestResult(Method testMethod){
		Annotation[] annotations = testMethod.getAnnotations();
		
		for (Annotation annotation: annotations){
			String mName = extractAnnotationValue(annotation, "Name", "value");
			if (mName != null){
				this.mName = mName;
				continue;
			}
			String mDescription = extractAnnotationValue(annotation, "Description", "value");
			if (mDescription != null) {
				this.mDescription = mDescription;
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
	
	public String getDescription(){
		return mDescription;
	}
}
