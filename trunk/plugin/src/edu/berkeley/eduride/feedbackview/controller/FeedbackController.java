package edu.berkeley.eduride.feedbackview.controller;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

import edu.berkeley.eduride.base_plugin.EduRideBase;
import edu.berkeley.eduride.feedbackview.EduRideFeedback;
import edu.berkeley.eduride.feedbackview.model.FeedbackLaunchConfigurationShortcut;
import edu.berkeley.eduride.feedbackview.model.IFeedbackModel;
import edu.berkeley.eduride.feedbackview.model.IJUnitFeedbackModel;
import edu.berkeley.eduride.feedbackview.model.JUnitFeedbackModel;
import edu.berkeley.eduride.feedbackview.views.FeedbackView;



/*
 * Controller class for Feedback, coordinating between IFeedbackModels and FeedbackView
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
	private boolean followOnEditorChange = true;

	
	// setters called from the viewer
	public void setUpdateContinuously(boolean updateContinuously) {
		this.updateContinuously = updateContinuously;
	}
	public void setFollowOnEditorChange(boolean followOnEditorChange) {
		this.followOnEditorChange = followOnEditorChange;
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

	}

	
	
	public void follow(ITypeRoot source) {
		follow(source, EduRideBase.getCurrentStep());
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
			follow(source, EduRideBase.getCurrentStep());
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
	}
	
	
	
	private void updateModel(IJUnitFeedbackModel model) {
		model.updateModel();
	}
	

	
	/*
	 * Callback when model is filled, from JUnitRunListener
	 */
	public void modelFilledCallback(IJUnitFeedbackModel model) {
		refreshView(model);
	}

	
	
	private void refreshView(IJUnitFeedbackModel model) {
		// TODO -- worry about concurrency, stacked update/refreshes here?
		EduRideFeedback.getFeedbackView().refresh(model);
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
	
	
	
	
	//////////////////////////

	/*
	 * Check if we are 
	 * (non-Javadoc)
	 * @see org.eclipse.jdt.core.IElementChangedListener#elementChanged(org.eclipse.jdt.core.ElementChangedEvent)
	 */
	@Override
	public void elementChanged(ElementChangedEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	////////////////////////
	


	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		if (followOnEditorChange) {
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
