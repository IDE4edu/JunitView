package edu.berkeley.eduride.feedbackview.model;



import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestRunSession;


import edu.berkeley.eduride.feedbackview.EduRideFeedback;

public class FeedbackViewTestRunListener extends TestRunListener {




	public FeedbackViewTestRunListener() {
		super();
	}

	public void sessionStarted(ITestRunSession session) {
		EduRideFeedback.asyncShowFeedbackView();
		
		
		// IJavaElement.isStructureKnown will tell if this is a class file or a source fi
		//new ASTparse("curriculum libraries", "SquareTest.java").getSource();
	}
	
	public void sessionFinished(ITestRunSession session) {
		//ILaunchConfiguration configuraton = NavigatorActivator.getLastLaunchConfiguration();
		IJavaProject proj = session.getLaunchedProject();
		String javaSourceName = session.getTestRunName() + ".java";   // sigh - junit doesn't give us the package, so we have to fudge
		TestList tl = new TestList(proj.getProject(), javaSourceName);
		EduRideFeedback.getDefault().asyncupdateTests(tl);
	}
}
