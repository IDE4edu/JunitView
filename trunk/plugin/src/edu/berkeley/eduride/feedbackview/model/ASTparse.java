package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class ASTparse {

	private String project_name;
	private String test_class_name;

	public ASTparse(String project_name, String test_class_name) {
		this.project_name = project_name;
		this.test_class_name = test_class_name;
	}

	public void getSource() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		IProject[] projects = workspace.getRoot().getProjects();

		ICompilationUnit unit = null;

		for (IProject project : projects) {
			if (project.getName().equals(project_name)) {
				try {
					IPackageFragment[] packages = JavaCore.create(project)
							.getPackageFragments();
					for (IPackageFragment mypackage : packages) {
						if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
							createAST(mypackage);
						}

					}
				} catch (JavaModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}

	private void createAST(IPackageFragment mypackage)
			throws JavaModelException {
		ICompilationUnit[] units = mypackage.getCompilationUnits();
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			// Now create the AST for the ICompilationUnits
			CompilationUnit parse = parse(unit);
			MethodVisitor visitor = new MethodVisitor();
			parse.accept(visitor);

			for (MethodDeclaration method : visitor.getMethods()) {
				System.out.print("Method name: " + method.getName()
						+ " Return type: " + method.getReturnType2());
			}

		}
	}

	private class MethodVisitor extends ASTVisitor {
		List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();

		@Override
		public boolean visit(MethodDeclaration node) {
			methods.add(node);
			return super.visit(node);
		}

		public List<MethodDeclaration> getMethods() {
			return methods;
		}
	}

}
