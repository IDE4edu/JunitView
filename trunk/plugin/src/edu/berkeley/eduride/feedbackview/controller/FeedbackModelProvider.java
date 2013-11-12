package edu.berkeley.eduride.feedbackview.controller;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;

import edu.berkeley.eduride.feedbackview.model.IFeedbackModel;
import edu.berkeley.eduride.feedbackview.model.IJUnitFeedbackModel;
import edu.berkeley.eduride.feedbackview.model.JUnitFeedbackModel;

public class FeedbackModelProvider {
	
	// this creates, stores, and returns the IFeedbackModels associated with 
	// activity pages
	
	static HashMap<IJavaElement, IFeedbackModel> feedbackModels = new HashMap<IJavaElement, IFeedbackModel>();
	
	
	public static void setup(IResource javafile, IFile isafile) {
		// builds the right IFeedbackModel for this javafile (a JUnitFeedbackModel)
		//   , based on its ISA info
		//isafile.
		//^ how does that work
		
		// - make a junit runconfig
		// - ASTParse the test class
		// - make a (at first blank) TestList
		IFeedbackModel model = new JUnitFeedbackModel(javafile);
	}
	
	
	
	// gets a IJavaElement pointing to a class
	public static IFeedbackModel getFeedbackModel(IJavaElement je_class) {
		IFeedbackModel model = feedbackModels.get(je_class);
		return model;
	}
	
	
	// might have IFeedbackModels for non-java activities someday, hey?

	
	
	
	public static IJUnitFeedbackModel getJUnitFeedbackModel(String runConfigName) {
		
	}
	
	
}
