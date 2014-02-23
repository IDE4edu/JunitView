package edu.berkeley.eduride.feedbackview;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;

import edu.berkeley.eduride.base_plugin.isafile.ISAFormatException;
import edu.berkeley.eduride.feedbackview.model.FeedbackLaunchConfigurationShortcut;
import edu.berkeley.eduride.feedbackview.model.IFeedbackModel;
import edu.berkeley.eduride.feedbackview.model.IJUnitFeedbackModel;
import edu.berkeley.eduride.feedbackview.model.JUnitFeedbackModel;

public class FeedbackModelProvider {

	/*
	 * this creates, stores, and returns the IFeedbackModels associated with
	 * java classes.  
	 * 
	 * Note, sometimes there is more than one IFeedbackModel
	 * for a particular class.  Here we use an additional string (Step name)
	 * to distinguish
	 * 
	 * (TODO change the string into a Step, now that the model is in Base)
	 */

	static HashMap<IJavaElement, IFeedbackModel> defaultFeedbackModels = new HashMap<IJavaElement, IFeedbackModel>();
	static HashMap<IJavaElement, HashMap<String, IFeedbackModel>> keyedFeedbackModels = new HashMap<IJavaElement, HashMap<String, IFeedbackModel>>();

	/*
	 * Generates a new feedback model for this java class and step key, based on
	 * the junit testclass that is passed. Step key is a string, and should be
	 * consistent with whoever calls getFeedbackModel(). It is only necessary in
	 * the rare cases that multiple activities use this same java class with
	 * different test classes, so that getFeedbackModel() will need to
	 * differentiate.
	 * 
	 * if stepkey is 'null' here for the first time that the javaclass is
	 * passed, then stepkey will be ignored in getFeedbackModel() -- that is,
	 * there will be only one junit class associated with this java class.
	 */
	public static void setup(ITypeRoot source, String stepkey,
			ITypeRoot testclass)  throws ISAFormatException {
		
		// builds the right IFeedbackModel for this javafile (a
		// JUnitFeedbackModel)
		// TODO -- wat, is this getting called twice for every model?
		IFeedbackModel model = new JUnitFeedbackModel(testclass, source);
		if (!((JUnitFeedbackModel)model).structureKnown()) {
			// hm, test class didn't compile or could make a launch configuration
			throw new ISAFormatException("Problem setting up feedback model from " + testclass.getElementName());
		}
		
		// store the default feedback model for this source file
		if (!defaultFeedbackModels.containsKey(source)) {
			defaultFeedbackModels.put(source, model);
		}
		// store the feedback model keyed on this stepkey (possibly null)
		HashMap<String, IFeedbackModel> inner;
		if (!keyedFeedbackModels.containsKey(source)) {
			inner = new HashMap<String, IFeedbackModel>();
			keyedFeedbackModels.put(source, inner);
		} else {
			inner = keyedFeedbackModels.get(source);
		}
		inner.put(stepkey, model);
	}

	
	// throws exceptions in order to give feedback to ISA authors... eventually
	public static void setup(IType source, String stepkey, IType testclass) throws ISAFormatException{
		if (source == null) {
			throw new ISAFormatException("Feedback Model exception: setup passed a null source type");
		}
		if (testclass == null) {
			throw new ISAFormatException("Feedback Model exception: setup passed a null testclass type");
		}
		ITypeRoot rSource = source.getTypeRoot();
		ITypeRoot rTestclass = testclass.getTypeRoot();
		if (rSource == null) {
			throw new ISAFormatException("Feedback Model exception: unable to determing type root of source file " + source.getFullyQualifiedName());
		}
		if (rTestclass == null) {
			throw new ISAFormatException("Feedback Model exception: unable to determing typeroot of testclass file " + testclass.getFullyQualifiedName());
		}
		setup(rSource, stepkey, rTestclass);
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
