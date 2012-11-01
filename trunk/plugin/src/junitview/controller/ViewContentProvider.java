package junitview.controller;

import junitview.views.EduRideJunitView;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ViewContentProvider implements IStructuredContentProvider {
	/**
	 * 
	 */
	private EduRideJunitView viewContentProvider;
	/**
	 * @param eduRideJunitView
	 */
	public ViewContentProvider(EduRideJunitView eduRideJunitView) {
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