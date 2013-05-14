package edu.berkeley.eduride.feedbackview;

import java.io.File;
import java.io.FileNotFoundException;


public interface FeedbackListener {

	public void logTestResult(String testClassName, String testName,
			boolean success, String message, String observed);

}
