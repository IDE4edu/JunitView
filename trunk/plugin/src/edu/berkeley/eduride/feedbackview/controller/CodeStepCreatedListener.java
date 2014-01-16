package edu.berkeley.eduride.feedbackview.controller;

import org.eclipse.jdt.core.JavaCore;

import edu.berkeley.eduride.base_plugin.isafile.ISAFormatException;
import edu.berkeley.eduride.base_plugin.isafile.StepCreatedListener;
import edu.berkeley.eduride.base_plugin.model.Step;
import edu.berkeley.eduride.feedbackview.FeedbackModelProvider;

public class CodeStepCreatedListener implements StepCreatedListener {

	@Override
	public void codeStepCreated(Step step) throws ISAFormatException {
		
		if (step.hasTestClass()) {
			FeedbackModelProvider.setup(
					JavaCore.createCompilationUnitFrom(step.getSourceIFile()),					null,
					JavaCore.createCompilationUnitFrom(step.getTestClassIFile()));
		}
		

	}

	@Override
	public void htmlStepCreated(Step step) throws ISAFormatException {
		// don't care
	}

}
