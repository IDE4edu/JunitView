package edu.berkeley.eduride.feedbackview.controller;

import java.util.HashMap;

import org.eclipse.jdt.junit.model.ITestRunSession;

import edu.berkeley.eduride.feedbackview.model.TestList;
import edu.berkeley.eduride.feedbackview.views.FeedbackView;

import studentview.model.Step;



public class FeedbackViewController {
	private FeedbackView view;
	private HashMap<Class<?>,TestList> dict = new HashMap<Class<?>,TestList>();
	
	public FeedbackViewController(FeedbackView view) {
		super();
		this.view = view;
	}
	

	/*
	 * Returns title for view, either name of class or an error string
	 * Returns error string for
	 *  - no target class available (no current editor, or the current editor isn't a java source
	 *  - target class has no test suite associated with it
	 *  - test suite doesn't compile.
	 */
	public String getViewTitle(Step step) {
		// ask Andrea + Jerry's stuff for the current target class 
		// return the name of the class or the error string
		if (step != null){
			return step.getSource();
		} else {
			return "";
		}
		//return c.getName();
		//return step.getName();
		
	}
	
	
	
	
	public void updateTests(TestList tl) {
		view.updateTests(tl);
	}
	
	
	
	/*
	 * return null if there is a problem (doesn't compile, etc) -- test area is grey in view --
	 * or a TestList
	 */
	
	
//	public TestList getTestList(Class<?> c) {
//		if (c == null) return null;
//		if (dict.containsKey(c)) {
//			return dict.get(c);
//		} else {
//			TestList t = new TestList(c);
//			dict.put(c, t);
//			return t;
//		}
//	}
	
	//TODO 
	
	// view will attach listeners for "updateTargetClass" 
	
	// and "updateSuccessStatus"  - 
	
}
