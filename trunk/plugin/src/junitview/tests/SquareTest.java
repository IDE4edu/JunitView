package junitview.tests;
import static org.junit.Assert.*;

import java.lang.annotation.*;
import org.junit.Test;



public class SquareTest {
	
	@Test
	@MethodCall("new Square(5).area()")
	@hideWhenSuccessful
	@Description("BLAHBLAHBLAH")
	public void testAssertSame() {
		assertSame("assertSame failed", 1,2);
	}

	@Test
	@MethodCall("new Square(5).area()")
	@hideWhenSuccessful
	@Description("BLAHBLAHBLAH")
	public void testAssertArrayEquals() {
		int[] x = {1,2,3,4,5,6,7,8,8,0};
		int[] y = {1,2,3,4,5,6,7,8,9,0};
		assertArrayEquals("assertArrayEquals failed", x,y);
	}
	
	@Test
	@MethodCall("new Square(5).area()")
	@hideWhenSuccessful
	@Description("BLAHBLAHBLAH")
	public void testAssertTrue() {
		int obs = new Square(5).area();
		assertTrue("assertTrue failed", 25 == obs);
	}
	
	@Test
	@MethodCall("new Square(5).area()")
	@hideWhenSuccessful
	@Expected("25")
	@Description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse magna mauris, tincidunt sed mattis eu, tempus vitae lorem. In scelerisque justo nec sem gravida sed congue lacus hendrerit. Mauris non ligula at dui sagittis vulputate in et turpis. Maecenas vel diam lorem")
	public void testArea1() {
		int obs = new Square(5).area();
		assertEquals("testArea1 failed", 25, obs);
	}
	
	@Test
	@MethodCall("new Square(3).area()")
	@Expected("25")
	public void testArea2() {
		int exp = 9;
		@Observed
		int obs = new Square(3).area();
		assertEquals("testArea2 failed", exp, obs);
	}
	
	
	@Test
	@MethodCall("new Square(5).perimeter()")
	@hideWhenSuccessful
	@Expected("25")
	public void testPerimeter() {
		int obs = new Square(5).perimeter();
		assertEquals("testPerimiter failed", 20, obs);
	}

	
	// Anotations
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface  hideWhenSuccessful {
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface  MethodCall {
		public String value();
	}

	@Target({ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface  Expected {
		public String value();
	}
	
	// Java throws away local variuable annotations, ug.  not used
	@Target({ElementType.METHOD, ElementType.LOCAL_VARIABLE})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface  Observed {
		public String value() default "<>";
	}
	
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface  Description {
		public String value();
	}
	
}
