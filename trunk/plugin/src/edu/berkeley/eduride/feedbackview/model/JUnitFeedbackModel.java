package edu.berkeley.eduride.feedbackview.model;

import java.util.HashMap;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;

public class JUnitFeedbackModel implements IJUnitFeedbackModel {

	
	public static HashMap<ILaunchConfigurationWorkingCopy, JUnitFeedbackModel> store 
		= new HashMap<ILaunchConfigurationWorkingCopy, JUnitFeedbackModel>();
	
	//ASTparse testFileParse;
	// a junit run configuration 
	TestList tl;
	
	// - make a blank TestList (feedback model), using an ASTParse of testclass,
	//    stored on launch config name as key
	
	public JUnitFeedbackModel(ILaunchConfigurationWorkingCopy lc) {
		
	}
	
	
	public boolean hasRunConfigurationWithName(String runConfigName) {
		return false;
	}
	
	
	@Override
	public void updateModel(ElementChangedEvent elementChangedEvent) {
		// TODO Auto-generated method stub

	}

}
