package edu.berkeley.eduride.feedbackview.controller;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

import edu.berkeley.eduride.base_plugin.util.ProjectUtil;
import edu.berkeley.eduride.feedbackview.EduRideFeedback;
import edu.berkeley.eduride.feedbackview.FeedbackModelProvider;
import edu.berkeley.eduride.feedbackview.model.FeedbackLaunchConfigurationShortcut;
import edu.berkeley.eduride.feedbackview.model.IFeedbackModel;
import edu.berkeley.eduride.feedbackview.model.IJUnitFeedbackModel;
import edu.berkeley.eduride.feedbackview.model.JUnitFeedbackModel;
import edu.berkeley.eduride.feedbackview.views.FeedbackView;



/*
 * Controller singleton for Feedback, coordinating between IFeedbackModels and FeedbackView
 * There is only one instance of this, registered to the feedbackview activator
 * 
 * Two main functions: 
 *   - follow -- switch the current focus 
 *   - update -- get new feedback for current focus
 * 
 * Three main entry events: 
 * -- a java editor is activated, in which case we need to set up the right 
 * feedback model, switch the view (if necessary), etc.  Slowness is ok here.
 * -- the feedback needs updating (maybe manually invoked): update the model, make sure the 
 * -- java model change: basically, they edited something in the 
 * 
 */
public class FeedbackController implements IElementChangedListener, IPartListener2
{
	
	// update on every JavaModel change
	boolean updateContinuously = true;
	// link view to editor -- set new feedback model when editor is opened 
	private boolean followOnEditorFocus = true;

	
	// getters/setters called from the viewer
	public boolean getUpdateContinuously() {
		return updateContinuously;
	}
	public void setUpdateContinuously(boolean updateContinuously) {
		this.updateContinuously = updateContinuously;
	}
	public boolean getFollowOnEditorFocus() {
		return followOnEditorFocus;
	}
	public void setFollowOnEditorFocus(boolean followOnEditorFocus) {
		this.followOnEditorFocus = followOnEditorFocus;
	}

	
	////// state

	// the current model to be represented in the view
	private IJUnitFeedbackModel currentModel;
	// source code (in the editor) driving the current model -- not testclass!
	private ITypeRoot currentSource;
	
	public IJUnitFeedbackModel getCurrentModel() {
		return currentModel;
	}
	public ITypeRoot getCurrentSource() {
		return currentSource;
	}


	private boolean currentlyProcessing = false;
	
	
	
	
	
	////////////////////
	/////  FOLLOW
	
	
	/*
	 * Switches the current focus for the FeedbackModel;
	 * 	 * 
	 * This 'follow' method does the real work; other 'follow' methods
	 * call this one.
	 * 
	 * @Param source the java source file, not the JUnit class
	 */
	public void follow(ITypeRoot source, String stepkey) {
		//TODO -- casting isn't right?  Maybe?
		IJUnitFeedbackModel model = (JUnitFeedbackModel) FeedbackModelProvider.getFeedbackModel(source, stepkey);
		
		// TODO set up queue for when this changes?  eh.
		currentModel = model;
		currentSource = source;
		
		// DEBUG
		if (model != null) {
			System.err.println("Told to follow " + source.getElementName() + " so setting up model on " + model.getTestClass().getElementName());
		}
	}

	
	
	public void follow(ITypeRoot source) {
		follow(source, ProjectUtil.getCurrentStep());
	}
	
	
	/*
	 * Sets the current FeedbackModel (that will be rendered in the 
	 * feedback view).
	 */
	public void follow(IEditorPart editor) {
		IEditorInput input = editor.getEditorInput();
		ITypeRoot source = null;
		if (input.exists()) {
			source = JavaUI.getEditorInputTypeRoot(input);
		}
		if (source != null) {
			follow(source, ProjectUtil.getCurrentStep());
		}
	}
	


	
	
	
	///////////////////////
	//// UPDATE
	
	
	// TODO be smart about updates queueing up on top of eachother.
	/*
	 * So, currentlyProcessing is a boolean which is set to true when update
	 * starts, and false when the refresh has happened.  Could store the model in 
	 * there in case it changes, but we mostly care about multliple updates happening
	 * on the same model.
	 * 
	 * Solutions?
	 *  - update() checks if there is a queue of calls, and clears all but the last one.
	 *    (but, its doesn't care which call its on?)
	 *  - if another one comes in while currently processing, have 
	 *    modelFilledCallback() check and restart the update process, instead of 
	 *    continuing to refresh the view.
	 */
	
	
	/*
	 * starting point for update process
	 * update the feedback: populate the current model, and tell the view to refresh
	 */
	public void update() {
		currentlyProcessing = true;
		updateModel(currentModel);
		
		// TODO 
		// show this every update? 
		// Or, check if the view is visible first, and only update if it is?
		// do this in refreshView()?
		EduRideFeedback.asyncShowFeedbackView();
		
		
		// DEBUG
		System.err.println("Starting update on model of " + currentModel.getTestClass().getElementName());
	}
	
	/*
	 * starting point for update process
	 */
	public void updateNoCompile() {
		currentlyProcessing = true;
		refreshView(currentModel, false);
	}
	
	
	////////////////
	// internal methods or callbacks -- use above methods to *start* the update process
	
	private void updateModel(IJUnitFeedbackModel model) {
		model.updateModel();
	}
	

	
	/*
	 * Callback when model is filled, from JUnitRunListener
	 */
	public void modelFilledCallback(IJUnitFeedbackModel model) {
		// DEBUG
		System.err.println("Model filled callback on " + model.getTestClass().getElementName());

		refreshView(model, true);
	}

	
	
	private void refreshView(IJUnitFeedbackModel model, boolean compiles) {
		// TODO -- worry about concurrency, stacked update/refreshes here?
		EduRideFeedback.getFeedbackView().refresh(model, compiles);
	}
	
	
	/*
	 * Callback when refresh is all done, from  FeedbackView
	 */
	public void refreshFinishedCallback(IJUnitFeedbackModel model) {
		currentlyProcessing = false;
	}
	

	
	
	/////// used to be in EduRideFeedback activator.  It goes here if anywhere...
	
//	public void asyncupdateTests(TestList tl) {
//		test = tl;   // store it in the field so thread can get to it
//		getDisplay().asyncExec(new Runnable() {
//			public void run() {
//				//getFeedbackView();
//				if (feedbackView != null) {
//					feedbackView.updateTests(test);
//				}
//			}
//		});
//		
//	}
	
	
	
	
	
	
	///////////////////////////////
	////  IElementChangedListener
	
	
	/*
	 * 	  gets called on JavaModel changes (to anything), calls update() maybe
	 */
	@Override
	public void elementChanged(ElementChangedEvent event) {
		if (currentModel == null) {
			// an early call, or something.
			return;
		}

		// TODO deal with updateContinuously
		ITypeRoot targetType = currentModel.getTestClass();
		ITypeRoot eventType = null;

		IJavaElement element = event.getDelta().getElement();
		int typ = element.getElementType();
		if (typ == IJavaElement.CLASS_FILE
				|| typ == IJavaElement.COMPILATION_UNIT) {
			eventType = (ITypeRoot) element;
		} else {
			eventType = (ITypeRoot) element
					.getAncestor(IJavaElement.COMPILATION_UNIT);
			if (eventType == null) {
				eventType = (ITypeRoot) element
						.getAncestor(IJavaElement.CLASS_FILE);
			}
		}

		// See if we should ignore this event.
		if (eventType == null) {
			// DEBUG
			System.out
					.println("elementChanged: eventTYPE is null -- couldn't get a Type, I guess");
			return;
		}
		if (!eventType.exists()) {
			// DEBUG
			System.out
					.println("elementChanged: hm, eventType doesn't actually exist");
			return;
		}
		if (!eventType.equals(targetType)) {
			// DEBUG
			System.out.println("elementChanged: we don't care about "
					+ eventType.getElementName());
			return;
		}

		// aha, something changed in our class. Lets do this.
		try {
			if (!eventType.isStructureKnown()) {
				// DEBUG
				System.out
						.println("elementChanged: our type, but it doesn't compile");
				IProblem[] problems = event.getDelta().getCompilationUnitAST()
						.getProblems();
				for (IProblem problem : problems) {
					if (problem.isError()) {
						// report problem
						System.out.println("  -- error: " + problem);
					}
				} //DEBUG

				updateNoCompile();
			} else {
				// DEBUG
				System.out
						.println("elementChanged: our type, and it compiles!  woot!");
				update();

			}

		} catch (JavaModelException e) {
			// whelp, failed trying isStructureKnown -- either doesn't exist, or
			// ?
			System.out
					.println("elementChanged: we failed to figure out if it compiled, and died.");
			e.printStackTrace();
			return;
		}

	}
	
	
	
	
	
	
	
	//////////////////////////
	////  IPartListener2
	
	//calls follow(), sometimes
	


	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		if (followOnEditorFocus) {
			IWorkbenchPart part = partRef.getPart(false);
			IEditorPart editor = null;
			if (part != null && part instanceof IEditorPart) {
				editor = (IEditorPart) part.getAdapter(IEditorPart.class);
			}
			if (editor != null) {
				follow(editor);
			} else {
				// tell somebody?
			}
		}

	}


	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// follow here as well?
	}

	

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
	}

	
	/// we do nothing when things are deselected, etc.

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
	}


	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
	}



	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
	}


	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
	}


	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
	}

	
}
