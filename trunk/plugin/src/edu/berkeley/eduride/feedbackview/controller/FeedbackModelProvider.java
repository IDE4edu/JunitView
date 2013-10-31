package edu.berkeley.eduride.feedbackview.controller;

import java.util.HashMap;

import org.eclipse.jdt.core.ElementChangedEvent;

import edu.berkeley.eduride.feedbackview.model.FeedbackModel;

public class FeedbackModelProvider {
	
	HashMap<String, FeedbackModel> feedbackModels = new HashMap<String, FeedbackModel>();
	
	public void updateModel(String handleIdentifier, ElementChangedEvent elementChangedEvent){
		//things happen
		if (feedbackModels.containsKey(handleIdentifier)){
			feedbackModels.get(handleIdentifier).updateModel(elementChangedEvent);
		} else {
			//determine if new one is necessary
			FeedbackModel model = new FeedbackModel(handleIdentifier, elementChangedEvent);
			feedbackModels.put(handleIdentifier, model);
		}
	}
}
