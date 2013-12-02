package edu.berkeley.eduride.feedbackview;

import java.io.File;
import java.io.FileNotFoundException;

/*
 * These are to be subclasses by tools that want to log feedback results
 * manage listeners with EduRideFeedback.registerListener and .removeListener
 */

public interface FeedbackListener {

	public void logTestResult(String testClassName, String testName,
			boolean success, String message, String observed);

}
