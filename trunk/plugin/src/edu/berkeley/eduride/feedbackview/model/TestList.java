package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jdt.junit.model.ITestElement.FailureTrace;

public class TestList {
	
	private ASTparse parse;
	private String name;
	public ArrayList<TestResult> test_results = new ArrayList<TestResult>();

	public TestList(IProject proj, String javaFileName, ArrayList<ITestElement> testElements, ArrayList<ITestCaseElement> testCaseElements) {
		parse = new ASTparse(proj,javaFileName);
		//parse.getSource();
		name = javaFileName.substring(0,javaFileName.length()-5);
		//ArrayList<MethodDeclaration> methods = parse.get_methods_by_annotation("@Test");
		for(ITestCaseElement tce: testCaseElements) {
			String methodName = tce.getTestMethodName();
			ArrayList<Annotation> annotations = parse.get_annotations(methodName);
			boolean makeTestResult = true;
			if (tce.getTestResult(true).equals(Result.IGNORED)) {
				makeTestResult = false;
			}
			if (makeTestResult) {
				TestResult result = new TestResult(annotations, methodName, tce);
				test_results.add(result);
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
//	public TestList(ITestRunSession session) {
//		test_results = new ArrayList<TestResult>();
//		ITestElementContainer container = (ITestElementContainer) session.getChildren()[0];
//		ITestElement[] testElements = container.getChildren();
//		for (int i= 0; i < testElements.length; i++){
//			ITestElement x = testElements[i];
//			
//			String name = ((ITestCaseElement)x).getTestMethodName();
//			String progress_state = x.getProgressState().toString();
//			String result = x.getTestResult(true).toString();
//			String failure_trace = "";
//			if(x.getFailureTrace() != null){
//				failure_trace = x.getFailureTrace().getTrace();
//			}
//			test_results.add(new TestResult(name, progress_state, result, failure_trace));
//		}
//		// TODO build test results into here based on constructor
//	}

}
