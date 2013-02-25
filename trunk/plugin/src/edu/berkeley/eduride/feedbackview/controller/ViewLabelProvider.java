package edu.berkeley.eduride.feedbackview.controller;


import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import edu.berkeley.eduride.feedbackview.views.FeedbackView;

public class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
	/**
	 * 
	 */
	private final FeedbackView viewLabelProvider;
	/**
	 * @param eduRideJunitView
	 */
	public ViewLabelProvider(FeedbackView eduRideJunitView) {
		viewLabelProvider = eduRideJunitView;
	}
	public String getColumnText(Object obj, int index) {
		return getText(obj);
	}
	public Image getColumnImage(Object obj, int index) {
		return getImage(obj);
	}
	public Image getImage(Object obj) {
		return PlatformUI.getWorkbench().
				getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}
}