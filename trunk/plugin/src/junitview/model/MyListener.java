package junitview.model;
import java.util.ArrayList;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

// http://www.coderanch.com/t/459170/Testing/Extending-org-junit-runner-Runner
// http://jorgemanrubia.net/2008/09/18/generating-junit-test-cases-dynamically/
// http://junit.sourceforge.net/javadoc/org/junit/runner/notification/RunListener.html#testFinished%28org.junit.runner.Description%29
public class MyListener extends RunListener{
	public ArrayList<Failure> failed;
	public ArrayList<Description> succeeded;
	
	public MyListener(){
		this.failed = new ArrayList<Failure>();
		this.succeeded = new ArrayList<Description>();
	}
	
	public void testStarted(Description d){
		succeeded.add(d);
	}
	public void testFailure(Failure f){
		failed.add(f);
		succeeded.remove(f.getDescription());
	}
}
