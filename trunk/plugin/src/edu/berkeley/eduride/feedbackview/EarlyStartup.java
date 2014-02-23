package edu.berkeley.eduride.feedbackview;

import org.eclipse.ui.IStartup;

import edu.berkeley.eduride.base_plugin.IStartupSync;
import edu.berkeley.eduride.base_plugin.isafile.ISAUtil;
import edu.berkeley.eduride.base_plugin.util.Console;
import edu.berkeley.eduride.feedbackview.controller.CodeStepCreatedListener;




// can be used with either the eduride or ui startup extension points
public class EarlyStartup implements IStartupSync, IStartup {

	@Override
	public void install() {
		EduRideFeedback.cscListener = new CodeStepCreatedListener();
		ISAUtil.registerStepCreatedListener(EduRideFeedback.cscListener);
		Console.msg("  Feedback listener is installed...");
		EduRideFeedback.earlyInstalled = true;
	}

	@Override
	public void earlyStartup() {
		install();

	}


}
