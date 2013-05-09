package edu.berkeley.eduride.feedbackview.model;

import java.util.Hashtable;

import org.eclipse.core.resources.IProject;

public class TestListStore {

	Hashtable<String, TestList> store;
	
	public TestListStore() {
		store = new Hashtable<String, TestList>();
	}
	
	// No package info in the javaFileName, maybe. Thank you, JUnit.
	public TestList getTestList(IProject proj, String javaFileName) {
		if (!(proj.exists())) {
			return null;   // ?right thing to do?
		}
		TestList tl = null;
		String key = makeKey(proj, javaFileName);
		if (store.containsKey(key)) {
			tl = store.get(key);
		} else {
//			tl = new TestList(proj, javaFileName);
//			store.put(key, tl);
		}
		return tl;
	}
	
	
	public void makeTestList (IProject proj, String javaFileName) {
		if (proj.exists()) {
			String key = makeKey(proj, javaFileName);
			if (!(store.containsKey(key))) {
//				TestList tl = new TestList(proj, javaFileName);
//				store.put(key, tl);
			}
		}
	}
	
	private String makeKey(IProject proj, String javaFileName) {
		return proj.getFullPath().toString() + "/" + javaFileName;
	}
}
