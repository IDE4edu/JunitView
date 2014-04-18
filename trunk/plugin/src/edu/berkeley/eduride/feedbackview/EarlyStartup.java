package edu.berkeley.eduride.feedbackview;

import org.eclipse.ui.IStartup;

import edu.berkeley.eduride.base_plugin.EduRideBase;
import edu.berkeley.eduride.base_plugin.IStartupSync;
import edu.berkeley.eduride.base_plugin.isafile.ISAUtil;
import edu.berkeley.eduride.base_plugin.util.Console;
import edu.berkeley.eduride.feedbackview.controller.CodeStepCreatedListener;




// can be used with either the eduride or ui startup extension points
public class EarlyStartup implements IStartupSync, IStartup {

	@Override
	public void install() {
		// OK, sometimes (on Macs?) this throws a ClassCircularityError, sigh
		// (the CodeStepCreatedListener)
		// because it is getting called in the startup() of BASE. This was supposed to be fixed
		// in 3.5
		EduRideBase.getDefault(); // will this force EduRideBase to be fully loaded?
		
		
		// ? Put this in a thread?  hm.  might get situations where 
		// the code step listener is created but not registered yet, or 
		// something.
		EduRideFeedback.cscListener = new CodeStepCreatedListener();
		ISAUtil.registerStepCreatedListener(EduRideFeedback.cscListener);
		EduRideFeedback.earlyInstalled = true;
		
		// UI call?
		Console.msg("  Feedback listener install method invoked...");
	}

	@Override
	public void earlyStartup() {
		install();

	}


}
