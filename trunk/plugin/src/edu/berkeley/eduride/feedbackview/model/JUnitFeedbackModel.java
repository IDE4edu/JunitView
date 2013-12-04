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

	static FeedbackLaunchConfigurationShortcut flcs = new FeedbackLaunchConfigurationShortcut();
	
//	public static HashMap<ILaunchConfigurationWorkingCopy, JUnitFeedbackModel> store 
//		= new HashMap<ILaunchConfigurationWorkingCopy, JUnitFeedbackModel>();
	
	// a junit run configuration 
	private HashMap<String, TestResult> test_results = new HashMap<String, TestResult>();
	ASTparse parse = null;
	//ILaunchConfigurationWorkingCopy lc = null;
	private ITypeRoot testclass = null;
	TreeSelection treeselection = null;
	
	//TestList tl;
	
	
	// - make a blank TestList (feedback model), using an ASTParse of testclass,
	//    stored on launch config name as key
	
	public JUnitFeedbackModel(ITypeRoot testclass) {
		//setup the launch config
		this.testclass = testclass;
		Object[] segments = new Object[1];
		segments[0] = testclass;
		TreePath treepath = new TreePath(segments);
		TreeSelection treeselection = new TreeSelection(treepath);
		this.treeselection = treeselection;
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
	
	//why did we want to know if the feedbackmodel had a runConfig?
	//does that mean I need to store one?
	public boolean hasRunConfigurationWithName(String runConfigName) {
		return false;
	}
	
	//override other one
	@Override
	public void updateModel() {
		//idea is run JUnit and update thingie
		flcs.launch(treeselection, ILaunchManager.RUN_MODE);
		//this triggers the other listener?
	}
	
	public ITypeRoot getTestClass() {
		return testclass;
	}
	
	public void updateModel(ArrayList<ITestCaseElement> testCaseElements) {
		for(ITestCaseElement tce: testCaseElements) {
			String methodName = tce.getTestMethodName();
			boolean makeTestResult = true;
			if (tce.getTestResult(true).equals(Result.IGNORED)) {
				makeTestResult = false;
			}
//			if (makeTestResult) {
//				TestResult result = new TestResult(annotations, methodName, tce);
//				test_results.add(result);
//			}
		}
	}

}
