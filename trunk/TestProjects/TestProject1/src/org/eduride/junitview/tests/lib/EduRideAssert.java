package org.eduride.junitview.tests.lib;

//import java.lang.reflect.Method;

//import org.junit.Assert;

public class EduRideAssert extends org.junit.Assert {

	public static void assertEquals(String msg, java.lang.Object expected,
			java.lang.Object observed) throws EduRideAssertionError{
		
		try {
			org.junit.Assert.assertEquals(msg, expected, observed);
		} catch (AssertionError ae) {
			EduRideAssertionError erae = new EduRideAssertionError(ae);
			Class<org.junit.Assert> clazz = org.junit.Assert.class;
			try {
			erae.method = clazz.getMethod("org.junit.Assert.assertEquals", msg.getClass(), expected.getClass(), observed.getClass());
			} catch (NoSuchMethodException nsme) {
				// we'll just throw the erae without method set, then.  hmf.
			}
			erae.expected = expected;
			erae.observed = observed;
			throw erae;
		}
	}
}