package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class JUnitFeedbackModel implements IJUnitFeedbackModel {

	static FeedbackLaunchConfigurationShortcut flcs = new FeedbackLaunchConfigurationShortcut();
	
//	public static HashMap<ILaunchConfigurationWorkingCopy, JUnitFeedbackModel> store 
//		= new HashMap<ILaunchConfigurationWorkingCopy, JUnitFeedbackModel>();
	
	// a junit run configuration 
	private HashMap<String, TestResult> test_results = new HashMap<String, TestResult>();
	ASTparse parse = null;
	ILaunchConfigurationWorkingCopy lc = null;
	//TestList tl;
	
	
	// - make a blank TestList (feedback model), using an ASTParse of testclass,
	//    stored on launch config name as key
	
	public JUnitFeedbackModel(ITypeRoot testclass) {
		parse = new ASTparse(testclass);
		//testclass.
		try {
			lc = flcs.createLaunchConfiguration(testclass);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<MethodDeclaration> methods = parse.get_methods_by_annotation("@Test");
		for(MethodDeclaration method: methods){
			String methodName = method.getName().getIdentifier();
			ArrayList<Annotation> annotations = parse.get_annotations(method);
			test_results.put(methodName, new TestResult(annotations, methodName));
		}
	}
	
	
	public boolean hasRunConfigurationWithName(String runConfigName) {
		return false;
	}
	
	//override other one
	@Override
	public void updateModel() {
		// TODO Auto-generated method stub
		//idea is run JUnit and update thingie
	}

}
