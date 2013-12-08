package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;

import org.eclipse.jdt.core.ElementChangedEvent;

public interface IFeedbackModel {

	//public IFeedbackModel(String handleIdentifier, ElementChangedEvent elementChangedEvent); 

	public void updateModel() ;
	
	public ArrayList getViewInputAsArrayList();
	
}
