package edu.berkeley.eduride.feedbackview.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.lang.model.type.TypeVisitor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.berkeley.eduride.base_plugin.util.Console;

public class ASTparse {

	private boolean structureKnown;
	private Hashtable<String, ArrayList<MethodDeclaration>> methods_by_annotation = new Hashtable<String, ArrayList<MethodDeclaration>>();
	private Hashtable<MethodDeclaration, ArrayList<Annotation>> annotations_of_a_method = new Hashtable<MethodDeclaration, ArrayList<Annotation>>();
	private Hashtable<String, ArrayList<Annotation>> annotations_of_a_methodname = new Hashtable<String, ArrayList<Annotation>>();

	private String title = null;
	
	public String getTitle() {
		return title;
	}

//	public ASTparse(IProject project, String test_class_name) {
//		this.project = project;
//		this.test_class_name = test_class_name;
//		getSource();
//	}
	
	public ASTparse(ITypeRoot root){
		try {
			createAST(root);
			structureKnown = true;
		} catch (JavaModelException e) {
			Console.err(e);
			structureKnown = false;
		}
	}

	
	public boolean structureKnown() {
		return structureKnown;
	}
	


	
	private void createAST(ITypeRoot root)
			throws JavaModelException {
		CompilationUnit parse = parse(root);
		
		// grab the title Description if its there.
		EdurideClassVisitor cVisitor = new EdurideClassVisitor();
		parse.accept(cVisitor);
		
		
		
		// deal with methods
		EdurideMethodVisitor mVisitor = new EdurideMethodVisitor();
		parse.accept(mVisitor);
		for (MethodDeclaration method : mVisitor.getMethods()) {
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
			annotations_of_a_methodname.put(method.getName().getFullyQualifiedName(), annotations);
		}
	}

	
	

	private static CompilationUnit parse(ITypeRoot root) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(root);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}

/*
 
 			IAnnotationBinding binding = annotation.resolveAnnotationBinding();
			IMemberValuePairBinding[] valuePairs = binding
					.getDeclaredMemberValuePairs();
			String annotationName = binding.getName();
			String annotationValue = null;
			for (IMemberValuePairBinding valuePair : valuePairs) {
				if (valuePair.getName().equals("value")) {
					annotationValue = (String) valuePair.getValue();
					break;
				}
			}
			annotations.put(annotationName, annotationValue);

 
 */
	
	static final String TITLE_ANNOTATION = "Description";
	
	private class EdurideClassVisitor extends ASTVisitor {

		@Override
		public boolean visit(TypeDeclaration node) {
			List<Object> mods = node.modifiers();
			for (Object mod : mods) {
				if (mod instanceof Annotation) {
					Annotation annotation = (Annotation) mod;
					IAnnotationBinding binding = annotation.resolveAnnotationBinding();
					IMemberValuePairBinding[] valuePairs = binding.getDeclaredMemberValuePairs();
					String annotationName = binding.getName();
					String annotationValue = "";
					if (annotationName.equals(TITLE_ANNOTATION)) {
						for (IMemberValuePairBinding valuePair : valuePairs) {
							if (valuePair.getName().equals("value")) {
								annotationValue = (String) valuePair.getValue();
								break;
							}
						}
						title = annotationValue;
					}
				}
			}

			return super.visit(node);
		}

	}
	
	
	private class EdurideMethodVisitor extends ASTVisitor {
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
	
	ArrayList<Annotation> get_annotations(String methodName) {
		return annotations_of_a_methodname.get(methodName);
	}

}
