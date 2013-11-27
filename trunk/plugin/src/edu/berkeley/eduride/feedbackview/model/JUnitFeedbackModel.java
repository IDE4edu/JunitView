package edu.berkeley.eduride.feedbackview.model;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;

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
		
	}
	
	
	public boolean hasRunConfigurationWithName(String runConfigName) {
		return false;
	}
	
	
	@Override
	public void updateModel(ElementChangedEvent elementChangedEvent) {
		// TODO Auto-generated method stub

	}

}
