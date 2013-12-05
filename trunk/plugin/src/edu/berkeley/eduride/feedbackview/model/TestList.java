package edu.berkeley.eduride.feedbackview.model;

// UNUSED, RIGHT?

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

// it would be great to cache a lot of this, hm.

public class TestList  {

	
	static HashMap<IProject, HashMap<String, ASTparse>> ASTparseStore = new HashMap<IProject, HashMap<String, ASTparse>>();

	// make and track a new ASTparse as necessary
	static ASTparse getASTparse(IProject proj, String javaFileName) {
		ASTparse parse;
		HashMap<String, ASTparse> inProjStore;
		inProjStore= ASTparseStore.get(proj);
		if (inProjStore == null) {
			// never seen this project before, so we need to do it all
			inProjStore = new HashMap<String, ASTparse>();
			parse = new ASTparse(proj, javaFileName);
			inProjStore.put(javaFileName, parse);
			ASTparseStore.put(proj, inProjStore);
		} else {
			parse = inProjStore.get(javaFileName);
			if (parse == null) {
				// we've seen this project, but never this file
				parse = new ASTparse(proj, javaFileName);
				inProjStore.put(javaFileName, parse);
			}
		}
		return parse;
	}
	
	
	private ASTparse parse;
	private String name;
	public ArrayList<TestResult> test_results = new ArrayList<TestResult>();

	public TestList(IProject proj, String testRunName, String javaFileName, ArrayList<ITestElement> testElements, ArrayList<ITestCaseElement> testCaseElements) {
		parse = getASTparse(proj,javaFileName);
		name = testRunName;
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
