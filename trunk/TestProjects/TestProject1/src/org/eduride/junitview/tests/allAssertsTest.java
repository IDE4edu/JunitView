package org.eduride.junitview.tests;
//import static org.junit.Assert.*;
import static org.eduride.junitview.tests.lib.EduRideAssert.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eduride.junitview.tests.lib.EduRideAssertionError;
import org.junit.Test;


public class allAssertsTest {

	
	
	@Test
	public void testEqualsMsg() {
		assertEquals("msg assert equals  baby", "2", "3");
	}
	
	@Test
	public void testEqualsNoMsg() {
		assertEquals(1, 2);
	}
	
	
	
	@Test
	public void testArrEqualsMsg() {
		int[] a = {1,2,3};
		int[] b = {1,2,4};
		assertArrayEquals("msg assert equals  baby", a, b);
	}
	
	@Test
	public void testArrEqualsNoMsg() {
		int[] a = {1,2,3};
		int[] b = {1,2,4};
		assertArrayEquals(a, b);
	}
		
	
	
	
	@Test
	public void testNull() {
		assertNull("msg assert null baby", true);
	}
	
	@Test
	public void testNotNull() {
		assertNotNull("msg assert not null baby", null);
	}
	


	@Test
	public void testTrue() {
		assertTrue("msg assert true baby", false);
	}
	
	@Test
	public void testFalse() {
		assertFalse("msg assert false  baby", true);
	}
	

	@Test
	public void testSame() {
		assertSame("msg assert same baby", 1, 2);
	}
	
	@Test
	public void testNotSame() {
		assertNotSame("msg assert not same baby", 1, 1);
	}
	
	
	@Test(expected=Exception.class)
	public void testNoException() {
		//everything passes
	}
	
	@Test
	public void testFailsMsg() {
		fail("msg assert fails baby");
	}

	
	@Test
	public void testFailsNoMsg() {
		fail();
	}	
	
	
	
	
	
	
	// Anotations
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Name {
		public String value();
	}

	@Target({ ElementType.METHOD, ElementType.LOCAL_VARIABLE })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Expected {
		public String value() default "<>";
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Description {
		public String value();
	}
}
