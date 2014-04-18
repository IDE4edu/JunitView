package edu.berkeley.eduride.feedbackview.model;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;


public class FeedbackLaunchConfigurationShortcut extends JUnitLaunchShortcut {

	@Override
	public ILaunchConfigurationWorkingCopy createLaunchConfiguration(
			IJavaElement element) throws CoreException {
		ILaunchConfigurationWorkingCopy lc = super.createLaunchConfiguration(element);
		//lc.
		return lc;
	}
}
