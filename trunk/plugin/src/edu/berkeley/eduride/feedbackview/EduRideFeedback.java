package edu.berkeley.eduride.feedbackview;

import java.util.ArrayList;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jdt.internal.junit.ui.TestRunnerViewPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.berkeley.eduride.feedbackview.controller.JavaFeedbackListener;
import edu.berkeley.eduride.feedbackview.model.TestList;
import edu.berkeley.eduride.feedbackview.views.FeedbackView;

/**
 * The activator class controls the plug-in life cycle
 */
public class EduRideFeedback extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "EduRideFeedbackView"; //$NON-NLS-1$

	// The shared instance
	private static EduRideFeedback plugin = null;

	private TestList test;

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
		JavaCore.addElementChangedListener(new JavaFeedbackListener());
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

	public void asyncupdateTests(TestList tl) {
		test = tl;
		getDisplay().asyncExec(new Runnable() {
			public void run() {
				//getFeedbackView();
				if (feedbackView != null) {
					feedbackView.updateTests(test);
				}
			}
		});
		
	}
	
	
	//////////////////////
	////  Event listener stuff

	
	static ArrayList<FeedbackListener> listeners = new ArrayList<FeedbackListener>();

	public static boolean registerListener(FeedbackListener l) {
		boolean result = listeners.add(l);
		return (result);
	}

	public static boolean removeListener(FeedbackListener l) {
		return (listeners.remove(l));
	}
	
    public static void logTestResult(String testClassName, String testName,
			boolean success, String message, String observed) {
    	for (FeedbackListener l : listeners) {
    		l.logTestResult(testClassName, testName, success, message, observed);
    	}
    }
	
}
