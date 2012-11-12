package junitview.tests;
import static org.junit.Assert.*;

import java.lang.annotation.*;
import org.junit.Test;



public class SquareTest {


	
	
	@Test
	@Name("area(Square(5))")
	@Description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse magna mauris, tincidunt sed mattis eu, tempus vitae lorem. In scelerisque justo nec sem gravida sed congue lacus hendrerit. Mauris non ligula at dui sagittis vulputate in et turpis. Maecenas vel diam lorem")
	public void testArea1() {
		Square x = new Square(5);
		assertEquals("area is wrong.", x.area(), 25);
	}
	
	@Test
	@Name("area(Square(3)) -> 9")
	public void testArea2() {
		Square x = new Square(3);
		@Expected
		int exp = x.area();
		assertEquals(exp, 9);
	}
	
	
	@Test
	public void testPerimeter() {
		Square x = new Square(5);
		assertEquals("perimiter failed", x.perimeter(), 20);
	}

	
	// Anotations
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface  Name {
		public String value();
	}

	@Target({ElementType.METHOD, ElementType.LOCAL_VARIABLE})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface  Expected {
		public String value() default "<>";
	}
	
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface  Description {
		public String value();
	}
	
}
