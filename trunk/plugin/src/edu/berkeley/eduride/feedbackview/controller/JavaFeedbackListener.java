package edu.berkeley.eduride.feedbackview.controller;

import java.util.Arrays;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Message;
import org.eclipse.jdt.junit.JUnitCore;

import edu.berkeley.eduride.base_plugin.EduRideBase;
import edu.berkeley.eduride.feedbackview.model.IFeedbackModel;

public class JavaFeedbackListener implements IElementChangedListener {

	@Override
	public void elementChanged(ElementChangedEvent elementChangedEvent) {
		// TODO Auto-generated method stub
		System.out.print("EDURIDE REPORTING IN WITH: ");
		// System.out.println(elementChangedEvent);
		// System.out.println("Type: "+elementChangedEvent.getType());
		// System.out.println("Delta: "+elementChangedEvent.getDelta());
		IJavaElementDelta delta = elementChangedEvent.getDelta();
		CompilationUnit cu = delta.getCompilationUnitAST();

		IJavaElement element = delta.getElement();
		ITypeRoot classfile = null;
		int typ = element.getElementType();
		if (typ == IJavaElement.CLASS_FILE
				|| typ == IJavaElement.COMPILATION_UNIT) {
			classfile = (ITypeRoot) element;
		} else {
			classfile = (ITypeRoot) element
					.getAncestor(IJavaElement.COMPILATION_UNIT);
			if (classfile == null) {
				classfile = (ITypeRoot) element
						.getAncestor(IJavaElement.CLASS_FILE);
			}
		}
		try {
			// If classfile null don't do anything or if we don't care about
			// this class
			if (classfile == null) {
				System.out.println("NULL CLASSFILE");
				return;
			}
			if (classfile.isStructureKnown()) {
				System.out.println("IT COMPILES!");
				// FeedbackModelProvider.updateModel(classfile.getHandleIdentifier(),elementChangedEvent);
				String stepkey = EduRideBase.getCurrentStep();
				IFeedbackModel feedbackModel = FeedbackModelProvider
						.getFeedbackModel(classfile, stepkey);
				if (feedbackModel != null) {
					feedbackModel.updateModel(elementChangedEvent);
				} else {
					System.out.println("NULL FEEDBACK MODEL");
				}
			} else {
				// spit out compilation errors to view
				IProblem[] problems = cu.getProblems();
				for (IProblem problem : problems) {
					if (problem.isError()) {
						// report problem
						System.out.println("GOT A PROBLEM");
					}
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
			return;
		}
	}

}
