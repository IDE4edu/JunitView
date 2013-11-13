package edu.berkeley.eduride.feedbackview.controller;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;

import edu.berkeley.eduride.feedbackview.model.FeedbackLaunchConfigurationShortcut;
import edu.berkeley.eduride.feedbackview.model.IFeedbackModel;
import edu.berkeley.eduride.feedbackview.model.IJUnitFeedbackModel;
import edu.berkeley.eduride.feedbackview.model.JUnitFeedbackModel;

public class FeedbackModelProvider {

	// this creates, stores, and returns the IFeedbackModels associated with
	// java files and, possibly, steps within activity pages that use those java
	// files

	static HashMap<IJavaElement, IFeedbackModel> defaultFeedbackModels = 
			new HashMap<IJavaElement, IFeedbackModel>();
	static HashMap<IJavaElement, HashMap<String, IFeedbackModel>> keyedFeedbackModels =
			new HashMap<IJavaElement, HashMap<String, IFeedbackModel>>();
	static FeedbackLaunchConfigurationShortcut flcs = new FeedbackLaunchConfigurationShortcut();

	// if stepkey is null, then any step key will return the feedbackmodel for
	// this testclass
	public static void setup(ITypeRoot source, String stepkey,
			ITypeRoot testclass) {
		// builds the right IFeedbackModel for this javafile (a
		// JUnitFeedbackModel)
		ILaunchConfigurationWorkingCopy lc;
		try {
			lc = flcs.createLaunchConfiguration(testclass);
			//TODO we need to put more stuff into the model
			IFeedbackModel model = new JUnitFeedbackModel(lc);
			// store the default feedback model for this source file
			if (!defaultFeedbackModels.containsKey(source)) {
				defaultFeedbackModels.put(source, model);
			}
			// store the feedback model keyed on this stepkey (possibly null)
			HashMap<String, IFeedbackModel> inner;
			if (!keyedFeedbackModels.containsKey(source)) {
				inner = new HashMap<String, IFeedbackModel>();
			} else {
				inner = keyedFeedbackModels.get(source);
			}
			inner.put(stepkey, model);

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// gets a IJavaElement pointing to a class (ITypeRoot)
	public static IFeedbackModel getFeedbackModel(IJavaElement source,
			String stepkey) {
		if (!keyedFeedbackModels.containsKey(source)) {
			return null;
		} else if (keyedFeedbackModels.get(source).containsKey(stepkey)) {
			return keyedFeedbackModels.get(source).get(stepkey);
		} else {
			return defaultFeedbackModels.get(source);
		}
	}


}
