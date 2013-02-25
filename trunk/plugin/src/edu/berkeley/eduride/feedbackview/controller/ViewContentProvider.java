package edu.berkeley.eduride.feedbackview.controller;


import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.berkeley.eduride.feedbackview.views.FeedbackView;

public class ViewContentProvider implements IStructuredContentProvider {
	/**
	 * 
	 */
	private FeedbackView viewContentProvider;
	/**
	 * @param eduRideJunitView
	 */
	public ViewContentProvider(FeedbackView eduRideJunitView) {
		viewContentProvider = eduRideJunitView;
	}
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		
	}
	public void dispose() {
	}
	public Object[] getElements(Object parent) {
		return new String[] { "Assignment 1", "Two", "Three" };
	}
}