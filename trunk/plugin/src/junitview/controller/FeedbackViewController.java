package junitview.controller;

import junitview.views.EduRideJunitView;
import junitview.model.TestList;
import junitview.tests.*;

public class FeedbackViewController {

	EduRideJunitView view;
	
	
	public FeedbackViewController(EduRideJunitView view) {
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
	public String getViewTitle() {
		return "SquareTest";
	}
	
	
	
	/*
	 * return null if there is a problem (doesn't compile, etc) -- test area is grey in view --
	 * or a TestList
	 */
	public TestList getTestList() {

		TestList t = new TestList(SquareTest.class);
		return t;
	}
	
	
	
	//TODO 
	
	// view will attach listeners for "updateTargetClass" 
	
	// and "updateSuccessStatus"  - 
	
}
