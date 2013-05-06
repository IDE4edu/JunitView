package edu.berkeley.eduride.feedbackview.model;



import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElementContainer;
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
		ITestElementContainer container = (ITestElementContainer) session.getChildren()[0];
		ITestElement[] testElements = container.getChildren();
		for (int i= 0; i < testElements.length; i++){
			ITestElement x = testElements[i];
			
			String name = ((ITestCaseElement)x).getTestMethodName();
			String result = x.getTestResult(true).toString();
			String observed;
			if(x.getFailureTrace() != null){
				observed = (String)x.getFailureTrace().getActual();
				tl.test_results.get(i).updateObserved(observed);
			}
			tl.test_results.get(i).updateSuccess(result);
		}
		EduRideFeedback.getDefault().asyncupdateTests(tl);
	}
}
