package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;

public class JUnitFeedbackModel implements IJUnitFeedbackModel {

	private HashMap<String, TestResult> test_results = new HashMap<String, TestResult>();
	ASTparse parse = null;
	private ITypeRoot testclass = null;

	// - make a blank TestList (feedback model), using an ASTParse of testclass,
	//    stored on launch config name as key
	
	public JUnitFeedbackModel(ITypeRoot testclass) {
		//setup the launch config
		this.testclass = testclass;
		
		//create the parse
		parse = new ASTparse(testclass);
		
		//use the parse to get the annotations and create an empty "TestResult"
		ArrayList<MethodDeclaration> methods = parse.get_methods_by_annotation("@Test");
		for(MethodDeclaration method: methods){
			ArrayList<Annotation> annotations = parse.get_annotations(method);
			String methodName = method.getName().getIdentifier();
			test_results.put(methodName, new TestResult(annotations, methodName));
		}
	}
	
	public ITypeRoot getTestClass() {
		return testclass;
	}
	
	public void updateModel(ArrayList<ITestCaseElement> testCaseElements) {
		for(ITestCaseElement tce: testCaseElements) {
			String methodName = tce.getTestMethodName();
			test_results.get(methodName).updateModel(tce);
			//TODO: need to deal with @Ignore
//			boolean makeTestResult = true;
//			if (tce.getTestResult(true).equals(Result.IGNORED)) {
//				makeTestResult = false;
//			}
//			if (makeTestResult) {
//				
//			}
		}
	}

	@Override
	public void updateModel(Object obj) {
	
	}

}
