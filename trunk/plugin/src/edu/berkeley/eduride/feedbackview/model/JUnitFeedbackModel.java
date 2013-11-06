package edu.berkeley.eduride.feedbackview.model;

import org.eclipse.jdt.core.ElementChangedEvent;

public class JUnitFeedbackModel implements IJUnitFeedbackModel {

	
	ASTparse testFileParse;
	// a junit run configuration 
	TestList tl;
	
	
	public JUnitFeedbackModel(IResource testclass, IJavaElement theclass) {
		
	}
	
	
	public boolean hasRunConfigurationWithName(String runConfigName) {
		
	}
	
	
	@Override
	public void updateModel(ElementChangedEvent elementChangedEvent) {
		// TODO Auto-generated method stub

	}

}
