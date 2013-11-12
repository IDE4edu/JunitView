package edu.berkeley.eduride.feedbackview.model;

import java.util.Arrays;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Message;
import org.eclipse.jdt.junit.JUnitCore;

import edu.berkeley.eduride.feedbackview.controller.FeedbackModelProvider;

public class JavaFeedbackListener implements IElementChangedListener{

	@Override
	public void elementChanged(ElementChangedEvent elementChangedEvent) {
		// TODO Auto-generated method stub
		System.out.println("EDURDIE REPORTING IN WITH: "+elementChangedEvent);
		System.out.println("Type: "+elementChangedEvent.getType());
		System.out.println("Delta: "+elementChangedEvent.getDelta());
		IJavaElementDelta delta = elementChangedEvent.getDelta();
		CompilationUnit cu = delta.getCompilationUnitAST();
		
		IJavaElement element = delta.getElement();
		IJavaElement classfile;
		if (element.getElementType() == IJavaElement.CLASS_FILE) {
			classfile = element;
		} else {
			classfile = element.getAncestor(IJavaElement.CLASS_FILE);
		}
		try {
		//If classfile null don't do anything or if we don't care about this class
			if (classfile == null) return;
			if(classfile.isStructureKnown()){
//				FeedbackModelProvider.updateModel(classfile.getHandleIdentifier(),elementChangedEvent);
				IFeedbackModel feedbackModel = FeedbackModelProvider.getFeedbackModel(classfile);
				feedbackModel.updateModel(elementChangedEvent);
			} else {
				//spit out compilation errors to view
				IProblem[] problems = cu.getProblems();
				for (IProblem problem: problems){
					if (problem.isError()){
						//report problem
				
					}
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
			return;
		}
	}
	

}
