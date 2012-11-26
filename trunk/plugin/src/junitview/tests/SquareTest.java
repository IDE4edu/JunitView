package junitview.tests;
import static org.junit.Assert.*;

import java.lang.annotation.*;
import org.junit.Test;



public class SquareTest {
	
	@Test
//	@Name("area(Square(5)) -> 25")
	@MethodCall("new Square(5).area()")
	@hideWhenSuccessful("true")
	@Description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse magna mauris, tincidunt sed mattis eu, tempus vitae lorem. In scelerisque justo nec sem gravida sed congue lacus hendrerit. Mauris non ligula at dui sagittis vulputate in et turpis. Maecenas vel diam lorem")
	public void testArea1() {
		@Expected
		int exp = 25;
		@Observed
		int obs = new Square(5).area();
		assertEquals("testArea1 failed", exp, obs);
	}
	
	@Test
//	@Name("area(Square(3)) -> 9")
	@MethodCall("new Square(3).area()")
	public void testArea2() {
		@Expected
		int exp = 9;
		@Observed
		int obs = new Square(3).area();
		assertEquals("testArea2 failed", exp, obs);
	}
	
	
	@Test
	@MethodCall("new Square(5).perimeter()")
	@hideWhenSuccessful("true")
	public void testPerimeter() {
		@Expected
		int exp = 20;
		@Observed
		int obs = new Square(5).perimeter();
		assertEquals("testPerimiter failed", exp, obs);
	}

	
	// Anotations
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface  hideWhenSuccessful {
		public String value();
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface  MethodCall {
		public String value();
	}

	@Target({ElementType.METHOD, ElementType.LOCAL_VARIABLE})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface  Expected {
		public String value() default "<>";
	}
	
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
