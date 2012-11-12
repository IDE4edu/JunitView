package junitview.tests;
public class Square {
	
	int height;
	
	public Square (int height){
		this.height = height;
	}
	
	public int area(){
		return height * height;
	}
	
	public int perimeter(){
		return 4 * height;
	}

}
