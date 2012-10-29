import java.util.ArrayList;
import java.util.HashSet;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;


public class MyListener extends RunListener{
	public ArrayList<Description> failed;
	public ArrayList<Description> succeeded;
	
	public MyListener(){
		this.failed = new ArrayList<Description>();
		this.succeeded = new ArrayList<Description>();
	}
	
	public void testStarted(Description d){
		succeeded.add(d);
	}
	public void testFailure(Failure f){
		Description d = f.getDescription();
		failed.add(d);
		succeeded.remove(d);
	}
}
