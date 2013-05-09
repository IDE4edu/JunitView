package edu.berkeley.eduride.feedbackview.model;



import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElement.FailureTrace;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestRunSession;

import edu.berkeley.eduride.feedbackview.EduRideFeedback;

// TODO write this so it checks the particular session when inside testCaseFinished -- that is, can know
//  that you are in the same session by checking testCaseElement.getTestRunSession(). little tricky, as you'll
//  need to store sessions in sessionStarted, and find them in testCaseFinished and they have hopefully
//  be stored already!

// TODO figure out how to update TestList and TestResult rather that make a new one everytime.  Maybe 
//   each TestClass has only one TestList ever?  See TestListStore, although probably a better way to do it
//   that that


public class FeedbackViewTestRunListener extends TestRunListener {

	ArrayList<ITestCaseElement> testCaseElements = new ArrayList<ITestCaseElement>();


	public FeedbackViewTestRunListener() {
		super();
	}

	public void sessionStarted(ITestRunSession session) {
		EduRideFeedback.asyncShowFeedbackView();
	}
	
	
	public void testCaseStarted (ITestCaseElement testCaseElement) {
		
		
	}
	
	
	public void testCaseFinished (ITestCaseElement testCaseElement) {
		testCaseElements.add(testCaseElement);
	}
	
	
	public void sessionFinished(ITestRunSession session) {
		//ILaunchConfiguration configuraton = NavigatorActivator.getLastLaunchConfiguration();
		IJavaProject proj = session.getLaunchedProject();
		String javaSourceName = session.getTestRunName() + ".java";   // sigh - junit doesn't give us the package, so we have to fudge
		ITestElementContainer container = (ITestElementContainer) session.getChildren()[0];
		ArrayList<ITestElement> testElements = new ArrayList<ITestElement>();
		testElements.addAll(Arrays.asList(container.getChildren()));

		TestList tl = new TestList(proj.getProject(), javaSourceName, testElements, testCaseElements);

		EduRideFeedback.getDefault().asyncupdateTests(tl);
	}
}
