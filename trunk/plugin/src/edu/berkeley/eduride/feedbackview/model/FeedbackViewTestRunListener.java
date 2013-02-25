package edu.berkeley.eduride.feedbackview.model;



import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestRunSession;

import edu.berkeley.eduride.feedbackview.EduRideFeedback;

public class FeedbackViewTestRunListener extends TestRunListener {


	
	
	public FeedbackViewTestRunListener() {
		super();
	}
	
	public void sessionFinished(ITestRunSession session) {
		TestList tl = new TestList(session);
		EduRideFeedback.getDefault().getController().updateTests(tl);
	}
	
	
}
