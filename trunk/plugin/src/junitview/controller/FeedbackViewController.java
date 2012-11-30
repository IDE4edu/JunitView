package junitview.controller;

import java.util.HashMap;

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
		// ask Andrea + Jerry's stuff for the current target class 
		// return the name of the class or the error string
		return "SquareTest";
	}
	
	
	
	/*
	 * return null if there is a problem (doesn't compile, etc) -- test area is grey in view --
	 * or a TestList
	 */
	public TestList getTestList() {
		HashMap<Class<?>,TestList> dict = new HashMap<Class<?>,TestList>();
		Class<?> c = SquareTest.class; // to be replaced later
		if (dict.containsKey(c)) {
			return dict.get(c);
		} else {
			TestList t = new TestList(c);
			dict.put(c, t);
			return t;
		}
	}
	
	
	
	//TODO 
	
	// view will attach listeners for "updateTargetClass" 
	
	// and "updateSuccessStatus"  - 
	
}
