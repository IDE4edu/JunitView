package edu.berkeley.eduride.feedbackview.controller;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import edu.berkeley.eduride.base_plugin.util.Console;

/*
 * When a java file is saved, we might want to update the feedback view (if that
 * view is pointing to that file, etc).
 * 
 * This object visits each node in a IResourceDelta, which is generated everytime there
 * is a change somewhere in the workspace.  It gets a targetResource, and if the changed
 * resource matches that target, fires off an update().
 * 
 * @author nate
 *
 */
public class DeltaVisitor implements IResourceDeltaVisitor {

	
	IResource targetResource;
	FeedbackController controller;
	
	public DeltaVisitor(IResource targetResource, FeedbackController controller) {
		this.targetResource = targetResource;
		this.controller = controller;
	}
	
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		
		// DEBUG
		//Console.msg("Visiting resource delta: resource '" + delta.getResource() + "' was code " + delta.getKind() + "... target is '" + targetResource + "'");
		//return true;
		
		// TODO -- if we are at a project, check that it is same as targetResource or don't go in
		// TODO -- it we are at a package, don't go in if its 'bin' stuff.
		
		if (delta.getKind() != IResourceDelta.CHANGED) {
			// if it isn't changed, it isn't something we are interested in, right?
			// or its kids?
			return false;
		}
		IResource eventResource = delta.getResource();
		if (eventResource.equals(targetResource)) {
			// woot
			Console.msg("ResourceChanged: the resource of the currentModel matches the resource changed: " + eventResource.getName());
			controller.update();
			return false;
		}
		// not a match, so lets keep going
		return true;
	}

}
