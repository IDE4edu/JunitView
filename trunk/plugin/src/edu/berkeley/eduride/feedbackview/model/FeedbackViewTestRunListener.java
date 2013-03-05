package edu.berkeley.eduride.feedbackview.model;



import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import edu.berkeley.eduride.feedbackview.EduRideFeedback;

public class FeedbackViewTestRunListener extends TestRunListener {


	
	
	public FeedbackViewTestRunListener() {
		super();
	}
	
	public void sessionFinished(ITestRunSession session) {
		EduRideFeedback.asyncShowFeedbackView();
		TestList tl = new TestList(session);
		EduRideFeedback.getDefault().updateTests(tl);
	}
	
	
}
