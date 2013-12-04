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
	
	boolean updateContinuously = true;
	// this is set in the feedback view
	private boolean followOnEditorChange = true;

	public void setUpdateContinuously(boolean updateContinuously) {
		this.updateContinuously = updateContinuously;
	}
	public void setFollowOnEditorChange(boolean followOnEditorChange) {
		this.followOnEditorChange = followOnEditorChange;
	}


	
	IJUnitFeedbackModel currentModel;
	ITypeRoot currentSource;
	
	public IJUnitFeedbackModel getCurrentModel() {
		return currentModel;
	}
	public ITypeRoot getCurrentSource() {
		return currentSource;
	}

	
	FeedbackLaunchConfigurationShortcut launchProvider = new FeedbackLaunchConfigurationShortcut();
	// this could be null
	ILaunchConfigurationWorkingCopy currentLaunchConfig = null;
	
	
	
	
	/////// FOLLOW
	
	
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
		currentModel = (JUnitFeedbackModel) FeedbackModelProvider.getFeedbackModel(source, stepkey);
		currentSource = source;
		
		IType testClassType = currentModel.getTestClass().findPrimaryType();
		try {
			currentLaunchConfig = launchProvider.createLaunchConfiguration(testClassType);
		} catch (CoreException e) {
			// TODO deal with null LaunchConfigs elsewhere, please
			currentLaunchConfig = null;
			e.printStackTrace();
		}

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
	


	
	
	
	///// UPDATE
	
	/*
	 * update the feedback: populate the current model, and tell the view to refresh
	 */
	public void update() {
		// TODO -- if there is another one waiting, maybe stop this and
		//  just do the last one?
		
		
		// TODO do this every update?  Only update when the view is shown?  
		EduRideFeedback.asyncShowFeedbackView();
		
		// launch JUnit for this class, will call updateModel()
		NullProgressMonitor pm = new NullProgressMonitor();
		try {
			currentLaunchConfig.launch(ILaunchManager.RUN_MODE, pm);
		} catch (CoreException e) {
			System.err.println("Uh oh, feedback controller update broke bad, ma");
			e.printStackTrace();
		}
		// updateModel() will get called, if things worked.
	}	
	
	/*
	 * Callback for launched JUnit test
	 */
	public void updateModel(ArrayList<ITestCaseElement> testCaseElements) {
		// TODO -- what if current model gets changed here?
		// really, we should have the currentModel sit inside the currentLauncher
		// and, the JUnit launch will find the right launchconfig and therefore the right model
		currentModel.updateModel(testCaseElements);  // launches junit test, Feedback
		refreshView(currentModel);
	}

	
	
	private void refreshView(IJUnitFeedbackModel model) {
		FeedbackView.refresh(model);
	}
	
	
	
	
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
