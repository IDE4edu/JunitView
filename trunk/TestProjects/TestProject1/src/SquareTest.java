import static org.junit.Assert.*;

import org.junit.Test;


public class SquareTest {

	@Test
	public void testArea() {
		Square x = new Square(5);
		assertEquals("Square Area Failed", x.area(), 55);
	}
	
	@Test
	public void testPerimeter() {
		fail("Not yet implemented");
	}

}
