import static org.junit.Assert.*;

import org.junit.Test;


public class SquareTest {

	@Test
	public void testArea() {
		Square x = new Square(5);
		assertEquals("Square Area Failed", x.area(), 25);
	}
	
	@Test
	public void testPerimeter() {
		Square x = new Square(5);
		assertEquals("Square Perimeter Failed", x.perimeter(), 20);
	}

}
