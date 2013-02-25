package edu.berkeley.eduride.feedbackview.controller;


import org.eclipse.jface.viewers.ViewerSorter;

import edu.berkeley.eduride.feedbackview.views.FeedbackView;

public class ResultSorter extends ViewerSorter {

	/**
	 * 
	 */
	private final FeedbackView resultSorter;

	/**
	 * @param eduRideJunitView
	 */
	public ResultSorter(FeedbackView eduRideJunitView) {
		resultSorter = eduRideJunitView;
	}
}