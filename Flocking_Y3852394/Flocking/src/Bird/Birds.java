package Bird;

import javax.swing.JFrame;

import drawing.Canvas;
import drawing.Utils;
import geometry.CartesianCoordinate;

/**
 * Created on 28/02/2019
 * @author Y3852394
 * Description: Conclude draw and undraw and move and turn function to control birds
 * including the fields to wrap location of birds
 */

public class Birds{
	
	/* Initialise variables */
	public Canvas myCanvas;
	// 0 points to no pen used, 1 points to pen used
	private int penUse = 0;
	// move needs angle of how much turtle turns
	protected double direction = 0;
	// Pointing to birds current location
	CartesianCoordinate myLocation;
	double PI = 3.1415926;
	public CartesianCoordinate speed, acceleration;
	

	/* Constructors */
	public Birds(Canvas myCanvas) {
		this.myCanvas = myCanvas;

		// Initialisation for turtle object
		// Define starting position of the bird is (300,200)
		this.myLocation = new CartesianCoordinate(300,200);
		
		// Choose initial speed as a random vector from the range 5 to 10
		speed = new CartesianCoordinate(Utils.randomIntBetween(5,10), Utils.randomIntBetween(5,10));
		// Set initial acceleration as 0
		acceleration = new CartesianCoordinate(0,0);
	}

	/**
	 * The bird is moved in its current direction for the given number of pixels.
	 * If the pen is down when the robot moves, a line will be drawn on the floor.
	 *
	 * @param pixels The number of pixels to move.
	 */
	public void move(double pixels) {
		// Store old position of birds
		CartesianCoordinate oldLocation = myLocation.copy();
		
		// Calculate the next position of birds
		this.myLocation.setX(oldLocation.getX() + Math.cos(this.direction) * pixels);
		this.myLocation.setY(oldLocation.getY() + Math.sin(this.direction) * pixels);
		
		// If the pen is down, turtle is moving with a line
		if(penUse == 1){
			// draw a line between two points
			myCanvas.drawLineBetweenPoints(oldLocation,myLocation);		
		}
	}

	/**
	 * Rotates the bird clockwise by the specified angle in radius.
	 *
	 * @param angle
	 * angle in radius that rotates
	 */
	public void turn(double angle) {
		// Judge the direction of the turtle
		this.direction = angle + this.direction;
	}

	/**
	 * Moves the pen off the canvas so that the bird routes isnt drawn for any subsequent movements.
	 */
	public void putPenUp() {
		penUse = 0;
	}

	/**
	 * Lowers the pen onto the canvas so that the bird route is drawn.
	 */
	public void putPenDown() {
		penUse = 1;
	}
	
	/**
	 * Draw a triangle as a bird
	 */
	public void draw(){
		
		// Set the moving direction of birds to be the speed direction
		this.turn(speed.heading()+3*PI/2);
		
		// Draw the shape
		this.putPenDown();
		for (int i = 0; i < 3; i++) {
			this.turn(Math.toRadians(120));
			this.move(10);
		}
		
		this.direction = 0;
		this.putPenUp();
	}
	
	/**
	 * Undraw method removes the previously drawn bird from the canvas
	 */
	public void undraw(){
		// Delete three edges of triangle
		for(int i=0; i<3; i++){
			myCanvas.removeMostRecentLine();
		}
	}
	
	/**
	 * Calculate and return the distance between two coordinates
	 * 
	 * @param Turtle_start, Turtle_end
	 * starting coordinate and ending coordinate
	 */
	public static double Distance(CartesianCoordinate Turtle_start, CartesianCoordinate Turtle_end){
		// Variables recording starting and ending coordinates
		double startPosX = Turtle_start.getX();
		double startPosY = Turtle_start.getY();
		double endPosX = Turtle_end.getX();
		double endPosY = Turtle_end.getY();
		
		// Use distance equation to calculate distance between two points
		double distance = Math.sqrt(Math.pow(startPosX-endPosX,2) + Math.pow(startPosY-endPosY,2));
		
		return distance;
	}
	
	/**
	 * Calculate the angle between a point and coordinate of bird's current location
	 * 
	 * @param xposition, yposition
	 * x coordinate of a point, y coordinate of a point
	 */
	public double CalculateAnglePoints(double xposition, double yposition){
		// Use arctan to compute angle between a point and current location of a bird in radius 
		double angle = Math.atan2(yposition-myLocation.getY(),xposition-myLocation.getX());

		return angle;
	}
	
	/**
	 * Used to wrap the birds' position when it leaves a given range
	 * To set birds turn 180 degree back to the window
	 * @param j
	 * get the current width and height of a JFrame j
	 * minimum x and y position are assumed as 10 to avoid panels
	 */
	public void wrapPosition(JFrame j){
		// Variables recoding width and height of the interface
		int max_x_position = j.getWidth();
		int max_y_position = j.getHeight();
		
		// If the birds hit the edge, set the birds' speed 180 around
		// Hit right edge
		if(this.myLocation.getX() >= max_x_position-10){
			speed.MultiplyX(-1);
		}
		
		// Hit left edge
		else if(this.myLocation.getX() <= 10){
			speed.MultiplyX(-1);
		}
		
		// Hit bottom edge
		else if(this.myLocation.getY() >= max_y_position-10){
			speed.MultiplyY(-1);
		}
		
		// Hit upper edge
		else if(this.myLocation.getY() <= 10){
			speed.MultiplyY(-1);
		}		
	}
}
