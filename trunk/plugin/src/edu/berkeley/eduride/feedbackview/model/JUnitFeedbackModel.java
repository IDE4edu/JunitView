package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;

import edu.berkeley.eduride.base_plugin.util.Console;
import edu.berkeley.eduride.feedbackview.EduRideFeedback;
import edu.berkeley.eduride.feedbackview.controller.FeedbackController;

public class JUnitFeedbackModel implements IJUnitFeedbackModel {

	// used to generate launch configuration working copy for each model
	static FeedbackLaunchConfigurationShortcut launchProvider = new FeedbackLaunchConfigurationShortcut();
	
	/////// saved once for each model -- we don't expect these to change.  
	private final String title;
	private final ITypeRoot testClass;
	private final ITypeRoot srcClass;
	private final ASTparse parse;
	// a null launchConfig means we've failed to set this up right...
	private final ILaunchConfigurationWorkingCopy launchConfig;

	
	private final HashMap<MethodDeclaration, ArrayList<Annotation>> testMethods;
	
	
	public ITypeRoot getTestClass() {
		return testClass;
	}
	
	public ITypeRoot getSourceClass() {
		return srcClass;
	}
	
	public ILaunchConfigurationWorkingCopy getLaunchConfig() {
		return launchConfig;
	}

	public String getTitle() {
		return title;
	}
	
	public JUnitFeedbackModel(ITypeRoot testClass, ITypeRoot srcClass) {
		// set up the final fields except title, launchConfig
		this.testClass = testClass;
		this.srcClass = srcClass;
		testMethods = new HashMap<MethodDeclaration, ArrayList<Annotation>>();
		parse = new ASTparse(testClass);
		
		if (parse.structureKnown()) {
			// set up launch config.
			ILaunchConfigurationWorkingCopy tempConfig = null;
			try {
				tempConfig = launchProvider.createLaunchConfiguration(testClass
						.findPrimaryType());
			} catch (CoreException e) {
				Console.err(e);
			}
			if (tempConfig == null) {
				// arg, we must give up
				launchConfig = null;
			} else {
				launchConfig = tempConfig;
			}
			

			//use the parse to get the annotations and create an empty hashSet
			ArrayList<MethodDeclaration> methods =  parse.get_methods_by_annotation("@Test");
			for (MethodDeclaration method: methods) {
				ArrayList<Annotation> annotations = parse.get_annotations(method);
				testMethods.put(method, annotations);
			}
			
			// set title
			String tempTitle = parse.getTitle();
			if (tempTitle == null) {
				title = "Tests in " + testClass.getElementName();
			} else {
				title = tempTitle;
			}
			
			
		} else {
			launchConfig = null;
			title = testClass.getElementName();
		}
		

		
		if (launchConfig != null) {
			modelStore.put(launchConfig.getName(), this);
		}
	}
	
	
	// return true if everything got setup ok, and we can update, etc.
	public boolean structureKnown() {
		return (launchConfig != null);
	}
	

	

	////////////////////////////

		
	private NullProgressMonitor pm;
	
	// Starting point of update, called from controller
	public void updateModel() {
		if (structureKnown()) {
			
			pm = new NullProgressMonitor();
		
			try {
				launchConfig.launch(ILaunchManager.RUN_MODE, pm);
			} catch (CoreException e) {
				Console.err("Uh oh, I couldn't launch an update for this model, ma");
				Console.err(e);
			}
			// fillModel() will get called, if things worked.
		} else {
			//huh -- so test case is bad, uh...
		}
		
	}
	
	// called by static method attachResultsToModel() below
	public void attachResults(ArrayList<ITestCaseElement> resultList) {
		JUnitFeedbackResults results = new JUnitFeedbackResults(testMethods, resultList);
		EduRideFeedback.getController().resultsAttachedCallback(this, results);
		
	}


	
	////////
	// just for debugging
	public String toString() {
		String ret = "";
		ret += "JUnitFeedbackModel for testclass: " + testClass.getElementName() + "\n";
		int i = 1;
		for (MethodDeclaration method : testMethods.keySet()) {
			ret += "  TestMethod " + i + ": " + method.getName() + "\n";
		}
		return ret;
	}
	
	
	
	///////////////////////////
	/// class methods
	
	// so we can find this based on the name, for the JUnit session listeners to call
	private static HashMap<String, JUnitFeedbackModel> modelStore = new HashMap<String, JUnitFeedbackModel>();
	
	// we'll track it, yo, but Java leaves us little way to detrack it.  
	public static void trackThisModel(JUnitFeedbackModel model) {
		if (model.structureKnown()) {
			String lcName = model.getLaunchConfig().getName();
			modelStore.put(lcName, model);
		}
	}
	
	// called by JUnit session finished listener (see JUnitRunListener)
	public static void attachResultsToModel(String launchConfigName, final ArrayList<ITestCaseElement> testCaseElements) {
		// TODO, find the relevant model from modelStore, and call its updateModel() method
		JUnitFeedbackModel model = modelStore.get(launchConfigName);
		
		if (model != null) {
			model.attachResults(testCaseElements);
		}
	}


	

}
