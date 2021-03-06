package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;

import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.junit.model.ITestCaseElement;

public interface IJUnitFeedbackModel extends IFeedbackModel {
	
	public void updateModel();
	
	public ITypeRoot getTestClass();
	public ITypeRoot getSourceClass();
	public boolean structureKnown();
	
	public void attachResults(ArrayList<ITestCaseElement> resultList);
	
}
