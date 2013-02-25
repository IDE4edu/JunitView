package junitview.model;

import junitview.EduRideFeedback;


import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestRunSession;

public class JunitviewTestRunListener extends TestRunListener {


	
	
	public JunitviewTestRunListener() {
		super();
	}
	
	public void sessionFinished(ITestRunSession session) {
		TestList tl = new TestList(session);
		EduRideFeedback.getDefault().getController().updateTests(tl);
	}
	
	
}
