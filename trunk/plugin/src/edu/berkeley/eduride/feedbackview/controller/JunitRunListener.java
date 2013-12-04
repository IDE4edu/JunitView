package edu.berkeley.eduride.feedbackview.controller;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
import edu.berkeley.eduride.feedbackview.model.TestList;


public class JunitRunListener extends TestRunListener {


	public JunitRunListener() {
		super();
	}

	// session storage
	
	static Map<ITestRunSession, ArrayList<ITestCaseElement>> sessions = new HashMap<ITestRunSession, ArrayList<ITestCaseElement>>();
	//ArrayList<ITestCaseElement> testCaseElements = new ArrayList<ITestCaseElement>();

	static private ArrayList<ITestCaseElement> trackSession(ITestRunSession session) {
		System.out.println("session " + session.toString() + " tracked  -- hashcode " + session.hashCode());
		if (sessions.containsKey(session)) {
			// well, this is kind of a problem, hm...  Shouldn't happen, should it?
			// if there are no test case elements, then its ok, but still...
			System.err.println("Um, trying to add session " + session + " to active list but it's already there?  Wha?");
			// I guess do nothing?
			return null;
		} else {
			ArrayList<ITestCaseElement> testCaseElements = new ArrayList<ITestCaseElement>();
			sessions.put(session, testCaseElements);
			return testCaseElements;
		}
	}
	
	static private void addCase(ITestCaseElement testCaseElement) {
		ITestRunSession session = testCaseElement.getTestRunSession();
		if (session == null) {
			// wha?
			System.err.println("Hey!  ITestRunSession for ITestCaseElement " + testCaseElement + " is null.  This don't score, I must ignore.");
			return;
		}
		ArrayList<ITestCaseElement> testCaseElements = sessions.get(session);
		if (testCaseElements != null) {
			testCaseElements.add(testCaseElement);
		} else {
			// uh oh - its session doesn't exist?  lets track it now, I guess?
			testCaseElements = trackSession(session);
			testCaseElements.add(testCaseElement);
		}
	}
	
	
	static private ArrayList<ITestCaseElement> getCases(ITestRunSession session) {
		ArrayList<ITestCaseElement> testCaseElements = sessions.get(session);
		if (testCaseElements != null) {
			return testCaseElements;
		} else {
			// um, hm.
			System.err.println("Something wicked this way happened. Session was tracked but then value was null?  Say it ain't so! ");
			// return an empty arraylist I guess
			return new ArrayList<ITestCaseElement>();
		}
	}

	
	static private void untrackSession(ITestRunSession session) {
		System.out.println("session " + session.toString() + " untracked -- hashcode " + session.hashCode());
		sessions.remove(session);
	}
	
	
	///////////////////
	// listener methods
	
	public void sessionStarted(ITestRunSession session) {
		// add session
		trackSession(session);
	}
	
	
	public void testCaseStarted (ITestCaseElement testCaseElement) {	
	}
	
	
	public void testCaseFinished (ITestCaseElement testCaseElement) {
		addCase(testCaseElement);
	}
	
	
	public void sessionFinished(ITestRunSession session) {
		ArrayList<ITestCaseElement> testCaseElements = getCases(session);
		
		IJavaProject proj = session.getLaunchedProject();
		String testRunName = session.getTestRunName();
		//ILaunchConfiguration configuraton = NavigatorActivator.getLastLaunchConfiguration();
		
		// used?
		ITestElementContainer container = (ITestElementContainer) session.getChildren()[0];
		ArrayList<ITestElement> testElements = new ArrayList<ITestElement>();
		testElements.addAll(Arrays.asList(container.getChildren()));
		
		
		 // sigh - junit doesn't give us the package, so we have to fudge
		String javaSourceName = testRunName + ".java";  
		
		// TODO -- use JUnitFeedbackModel
		TestList tl = new TestList(proj.getProject(), testRunName, javaSourceName, testElements, testCaseElements);

		EduRideFeedback.getDefault().asyncupdateTests(tl);
		untrackSession(session);
	}
}
