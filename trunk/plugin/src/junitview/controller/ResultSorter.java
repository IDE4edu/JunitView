package junitview.controller;

import junitview.views.EduRideJunitView;

import org.eclipse.jface.viewers.ViewerSorter;

public class ResultSorter extends ViewerSorter {

	/**
	 * 
	 */
	private final EduRideJunitView resultSorter;

	/**
	 * @param eduRideJunitView
	 */
	public ResultSorter(EduRideJunitView eduRideJunitView) {
		resultSorter = eduRideJunitView;
	}
}