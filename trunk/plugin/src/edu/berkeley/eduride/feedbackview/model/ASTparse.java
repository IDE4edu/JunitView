package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;
import java.util.Hashtable;
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
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

public class ASTparse {

	private IProject project;
	private String test_class_name;
	private Hashtable<String, ArrayList<MethodDeclaration>> methods_by_annotation = new Hashtable<String, ArrayList<MethodDeclaration>>();
	private Hashtable<MethodDeclaration, ArrayList<Annotation>> annotations_of_a_method = new Hashtable<MethodDeclaration, ArrayList<Annotation>>();


	public ASTparse(IProject project, String test_class_name) {
		this.project = project;
		this.test_class_name = test_class_name;
		getSource();
	}

	public void getSource() {

		ICompilationUnit unit = null;

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

	private static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}

	private void createAST(IPackageFragment mypackage)
			throws JavaModelException {
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			if (unit.getElementName().equals(test_class_name)){
				// Now create the AST for the ICompilationUnits
				CompilationUnit parse = parse(unit);
				MethodVisitor visitor = new MethodVisitor();
				parse.accept(visitor);

				for (MethodDeclaration method : visitor.getMethods()) {
					List<Object> modifiers = method.modifiers();
					ArrayList<Annotation> annotations = new ArrayList<Annotation>();

					for (int i = 0; i < modifiers.size(); i++){
						if (modifiers.get(i)instanceof Annotation){
							Annotation annotation = (Annotation) modifiers.get(i);
							String annotation_name = annotation.toString();

							// annotations of a certain method
							annotations.add(annotation);

							// methods with a certain annotation
							if(methods_by_annotation.get(annotation_name) == null){
								// new annotation
								ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
								methods.add(method);
								methods_by_annotation.put(annotation_name, methods);
							} else{
								// seen annotation
								ArrayList<MethodDeclaration> methods = methods_by_annotation.get(annotation_name);
								methods.add(method);
								methods_by_annotation.put(annotation_name, methods);
							}
						}
					}

					// annotations of a certain method
					annotations_of_a_method.put(method, annotations);

					//					String blah = method.toString();
					//					System.out.println("Method name: " + method.getName()
					//							+ " Return type: " + method.getReturnType2());
				}
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

	ArrayList<MethodDeclaration> get_methods_by_annotation(String annotation){
		return methods_by_annotation.get(annotation);
	}

	ArrayList<Annotation> get_annotations(MethodDeclaration method){
		return annotations_of_a_method.get(method);
	}

	//TODO method.modifiers has annotations
	//TODO get all methods for an annotation, get all annotations for a specific method, get annotation X from method Y -- return value of annotation, or boolean if it exists and has no values
	//TODO want ^ these calls to be FAST. Want to prepopulate as much as you can...
	//TODO will be given a IProject instead of project name

}
