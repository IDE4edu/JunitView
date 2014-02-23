package edu.berkeley.eduride.feedbackview.controller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import edu.berkeley.eduride.base_plugin.isafile.ISAFormatException;
import edu.berkeley.eduride.base_plugin.isafile.StepCreatedListener;
import edu.berkeley.eduride.base_plugin.model.Step;
import edu.berkeley.eduride.feedbackview.FeedbackModelProvider;

public class CodeStepCreatedListener implements StepCreatedListener {

	@Override
	public void codeStepCreated(Step step) throws ISAFormatException {

		if (step.hasTestClass()) {

			try {
				// we need to get ITypeRoot for the source and the test
				IFile srcIFile = step.getSourceIFile();
				if (srcIFile == null) {
					throw new ISAFormatException("Source file cannot be found "
							+ step.getSource() + " in " + "ISA FILE TBD");
				}
				ITypeRoot srcModel = JavaCore
						.createCompilationUnitFrom(srcIFile);

				// test -- should be a qualified name
				ITypeRoot tstModel;
				String tst_qn = step.getTestClassQualifiedName();

				IJavaProject proj = step.getIJavaProject();
				if (proj == null) {
					// whoops, not a java project -- shouldn't have a <testclass> now should it?
					throw new ISAFormatException("<testclass> tag requires this ISA to be within a Java project, and it isn't, is it?");
				}
				
				// throws javaModelException?  
				IType tempType = proj.findType(tst_qn);
				if (tempType == null) {
					throw new ISAFormatException("Bad name in testclass reference '" + tst_qn + "': couldn't find the class.");
				}
				tstModel = tempType.getTypeRoot();
				
				
				FeedbackModelProvider.setup(srcModel, null, tstModel);

			} catch (JavaModelException e) {
				
				throw new ISAFormatException("JavaModel Exception: "
						+ e.getMessage());
			}

		}

	}

	@Override
	public void htmlStepCreated(Step step) throws ISAFormatException {
		// don't care
	}

}
