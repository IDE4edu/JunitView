package edu.berkeley.eduride.feedbackview;

import java.util.ArrayList;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.berkeley.eduride.base_plugin.isafile.ISAUtil;
import edu.berkeley.eduride.base_plugin.util.IPartListenerInstaller;
import edu.berkeley.eduride.feedbackview.controller.CodeStepCreatedListener;
import edu.berkeley.eduride.feedbackview.controller.FeedbackController;
import edu.berkeley.eduride.feedbackview.views.FeedbackView;

/**
 * The activator class controls the plug-in life cycle
 */
public class EduRideFeedback extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "EduRideFeedbackView"; //$NON-NLS-1$
	// The shared instance
	private static EduRideFeedback plugin = null;

	
	public static CodeStepCreatedListener cscListener = null;
	public static boolean earlyInstalled = false;

	/**
	 * The constructor
	 */
	public EduRideFeedback() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		//JavaCore.addElementChangedListener(new JavaFeedbackListener());
		initController();
	}

	// getDefault().

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static EduRideFeedback getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		if (plugin == null)
			return null;
		IWorkbench workBench = plugin.getWorkbench();
		if (workBench == null)
			return null;
		return workBench.getActiveWorkbenchWindow();
	}

	public static IWorkbenchPage getActivePage() {
		IWorkbenchWindow activeWorkbenchWindow = getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null)
			return null;
		return activeWorkbenchWindow.getActivePage();
	}

	
	///////////////////
	/// controller
	
	// singleton
	private static FeedbackController controller = null;
	
	private static void initController() {
		controller = new FeedbackController();
		if (cscListener == null) {
			// whoa, early startup wasn't done?
			cscListener = new CodeStepCreatedListener();
		}

		
		// TODO -- worry if things happen while the 4 following steps are partially taken?
		
		//register for Step creation events, puts entries in FeedbackModelProvider hash tables
		if (!earlyInstalled) {
			ISAUtil.registerStepCreatedListener(cscListener);
		}
			
		// gets JavaModel change events
		JavaCore.addElementChangedListener(controller);
		
		// install on Editors for open events
		IPartListenerInstaller.installOnWorkbench(controller, "Feedback");
		// TODO deal with editors already opened?
		
		// get Resource change events (file saves)
		ResourcesPlugin.getWorkspace().addResourceChangeListener(controller, IResourceChangeEvent.POST_CHANGE);
		
		FeedbackView v = feedbackView;
		if (v != null) {
			v.setController(controller);
		}
		
	}
	
	
	
	public static FeedbackController getController() {
		return controller;
	}
	
	
	
	
	
	
	// //////////////////////////////////

	private static FeedbackView feedbackView;

	public static void setFeedbackView(FeedbackView fv) {
		feedbackView = fv;
	}

	public static FeedbackView getFeedbackView() {
		// stolen from below, from JUnitPlugin
		if (feedbackView == null) {
			IWorkbenchPage page = EduRideFeedback.getActivePage();
			if (page != null) {
				// does this need to be in a Runnable, ala asyncShowFeedbackView?  
				//  maybe will get Thread problems...
				feedbackView = (FeedbackView) page.findView(FeedbackView.ID);
			}
		}
		return feedbackView;
	}

	// copied from JUnitPlugin
	public static void asyncShowFeedbackView() {
		getDisplay().asyncExec(new Runnable() {
			public void run() {
				FeedbackView fv = showFeedbackViewInActivePage();
				EduRideFeedback.setFeedbackView(fv);
			}
		});
	}

	// copied from JUnitPlugin
	public static IViewPart iview;
	public static FeedbackView showFeedbackViewInActivePage() {
		try {
			// Have to force the creation of view part contents
			// otherwise the UI will not be updated
			IWorkbenchPage page = EduRideFeedback.getActivePage();
			if (page == null)
				return null;
			FeedbackView view = (FeedbackView) page.findView(FeedbackView.ID);
			iview = page.findView("org.eclipse.jdt.junit.ResultView");
			if (view == null) {
				// create and show the result view if it isn't created yet.
				return (FeedbackView) page.showView(FeedbackView.ID, null,
						IWorkbenchPage.VIEW_VISIBLE);
			} else {
				page.hideView(iview);
				return (FeedbackView) page.showView(FeedbackView.ID, null,
						IWorkbenchPage.VIEW_VISIBLE);
			}
		} catch (PartInitException pie) {
			// EduRideFeedback.log(pie);
			return null;
		}
	}

	// copied from JUnitPlugin
	private static Display getDisplay() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

	// ////////////


	
	
	
	
	//////////////////////
	////  Event listener stuff

	
	static ArrayList<FeedbackListener> feedbackListeners = new ArrayList<FeedbackListener>();

	public static boolean registerListener(FeedbackListener l) {
		boolean result = feedbackListeners.add(l);
		return (result);
	}

	public static boolean removeListener(FeedbackListener l) {
		return (feedbackListeners.remove(l));
	}
	
    public static void logTestResult(String testClassName, String testName,
			boolean success, String message, String observed) {
    	for (FeedbackListener l : feedbackListeners) {
    		l.logTestResult(testClassName, testName, success, message, observed);
    	}
    }
	
}
