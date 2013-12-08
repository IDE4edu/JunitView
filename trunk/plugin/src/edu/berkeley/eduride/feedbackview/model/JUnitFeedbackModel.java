package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;
import java.util.HashMap;

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

import edu.berkeley.eduride.feedbackview.EduRideFeedback;
import edu.berkeley.eduride.feedbackview.controller.FeedbackController;

public class JUnitFeedbackModel implements IJUnitFeedbackModel {

	// used to generate launch configuration working copy for each model
	static FeedbackLaunchConfigurationShortcut launchProvider = new FeedbackLaunchConfigurationShortcut();
	
	/////// saved once for each model -- we don't expect these to change.  
	private final ITypeRoot testclass;
	private final ASTparse parse;
	// a null launchConfig means we've failed to set this up right...
	private final ILaunchConfigurationWorkingCopy launchConfig;

	
	
	public ITypeRoot getTestClass() {
		return testclass;
	}
	
	public ILaunchConfigurationWorkingCopy getLaunchConfig() {
		return launchConfig;
	}

	
	
	public JUnitFeedbackModel(ITypeRoot testclass) {
		// set up the final fields
		this.testclass = testclass;
		parse = new ASTparse(testclass);
		if (parse.structureKnown()) {
			// set up launch config.
			ILaunchConfigurationWorkingCopy tempConfig = null;
			try {
				tempConfig = launchProvider.createLaunchConfiguration(testclass
						.findPrimaryType());
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (tempConfig == null) {
				// arg, we must give up
				launchConfig = null;
			} else {
				launchConfig = tempConfig;
			}
		} else {
			launchConfig = null;
		}
		//use the parse to get the annotations and create an empty "TestResult"
		ArrayList<MethodDeclaration> methods = parse.get_methods_by_annotation("@Test");
		for(MethodDeclaration method: methods){
			ArrayList<Annotation> annotations = parse.get_annotations(method);
			String methodName = method.getName().getIdentifier();
			testResults.put(methodName, new TestResult(annotations, methodName));
		}
	}
	
	
	// return true if everything got setup ok, and we can update, etc.
	public boolean structureKnown() {
		return (launchConfig != null);
	}
	

	

	////////////////////////////

	
	// store for results, gets updated each time
	private HashMap<String, TestResult> testResults = new HashMap<String, TestResult>();

	
	private NullProgressMonitor pm;
	
	public void updateModel() {
		if (structureKnown()) {
			pm = new NullProgressMonitor();
		
			try {
				launchConfig.launch(ILaunchManager.RUN_MODE, pm);
			} catch (CoreException e) {
				System.err.println("Uh oh, I couldn't launch an update for this model, ma");
				e.printStackTrace();
			}
			// fillModel() will get called, if things worked.
		}
		
	}
	
	// called by static method fillThisModel() below
	private void fillModel(ArrayList<ITestCaseElement> testCaseElements) {
		for (ITestCaseElement tce : testCaseElements) {
			String methodName = tce.getTestMethodName();
			testResults.get(methodName).updateModel(tce);
			// TODO: need to deal with @Ignore
			// boolean makeTestResult = true;
			// if (tce.getTestResult(true).equals(Result.IGNORED)) {
			// makeTestResult = false;
			// }
			// if (makeTestResult) {
			//
			// }
		}
		EduRideFeedback.getController().modelFilledCallback(this);
		
	}


	
	////////
	// just for debugging
	public String toString() {
		String ret = "";
		ret += "JUnitFeedbackModel for testclass: " + testclass.getElementName() + "\n";
		int i = 1;
		for (TestResult result : testResults.values()) {
			ret += "  Result " + i + ": " + result.getDescription() + "\n";
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
	public static void fillThisModel(String launchConfigName, ArrayList<ITestCaseElement> testCaseElements) {
		// TODO, find the relevant model from modelStore, and call its updateModel() method
		JUnitFeedbackModel model = modelStore.get(launchConfigName);
		if (model != null) {
			model.fillModel(testCaseElements);
		}
	}

	@Override
	public ArrayList<TestResult> getViewInputAsArrayList() {
		// TODO Auto-generated method stub
		return new ArrayList<TestResult> (testResults.values());
	}
	
	

}
