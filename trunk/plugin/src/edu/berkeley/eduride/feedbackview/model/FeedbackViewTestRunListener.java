package edu.berkeley.eduride.feedbackview.model;



import navigatorView.NavigatorActivator;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.internal.junit.model.*;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import edu.berkeley.eduride.feedbackview.EduRideFeedback;

public class FeedbackViewTestRunListener extends TestRunListener {


	
	
	public FeedbackViewTestRunListener() {
		super();
	}
	
	public void sessionStarted(ITestRunSession session) {
		EduRideFeedback.asyncShowFeedbackView();
		System.out.println("sessionname: "+ session.getTestRunName());
	}
	
	public void sessionFinished(ITestRunSession session) {
		ILaunchConfiguration configuraton = NavigatorActivator.getLastLaunchConfiguration();
		TestList tl = new TestList(session);
		EduRideFeedback.getDefault().asyncupdateTests(tl);
	}
}
