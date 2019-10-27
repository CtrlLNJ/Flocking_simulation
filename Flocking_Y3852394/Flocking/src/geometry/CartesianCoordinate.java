package geometry;

/**
 * Created on 28/02/2019
 * @author Y3852394
 * Description: Include vector calculation used in the whole program and initialise normal Cartesian coordinates
 */

public class CartesianCoordinate {
	
	/* Initialise variables */
	private double xPosition;
	private double yPosition;
	
	/* Constructors */
	public CartesianCoordinate() {}
	public CartesianCoordinate(double cn, double bn) {
		 xPosition = cn;
		 yPosition = bn;
	}

	/**
	 * x setter
	 * @param xPosition
	 */
	public void setX(double xPosition) {
		this.xPosition = xPosition;
	}

	/**
	 * y setter
	 * @param yPosition
	 */
	public void setY(double yPosition) {
		this.yPosition = yPosition;
	}
	
	/**
	 * x getter
	 * @return xPosition
	 */
	public double getX() {
		return xPosition;
	}

	/**
	 * y getter
	 * @return yPosition
	 */
	public double getY() {
		return yPosition;
	}
	
	/**
	 * Return a same Cartesian coordinate according to xPosition and yPosition
	 * @return new CartesianCoordinate
	 */
	public CartesianCoordinate copy() {
		return new CartesianCoordinate(this.xPosition, this.yPosition);
	}
	
	/**
	 * Update current vector by adding a new vector
	 * @param v
	 * a Cartesian coordinate
	 */
	public void VectorAdd(CartesianCoordinate v) {
		this.xPosition += v.xPosition;
		this.yPosition += v.yPosition;
	}
	
	/**
	 * Update current vector by subtracting a new vector
	 * @param v
	 * a Cartesian coordinate
	 */
	public void VectorSub(CartesianCoordinate v) {
		this.xPosition -= v.xPosition;
		this.yPosition -= v.yPosition;
	}
	
	/**
	 * Divide current vector by a value
	 * @param value
	 * value with type double
	 */
	public void Division(double value) {
		this.xPosition /= value;
		this.yPosition /= value;
	}
	
	/**
	 * Multiply current vector by a value
	 * @param value
	 * value with type double
	 */
	public void Multiplication(double value) {
		this.xPosition *= value;
		this.yPosition *= value;
	}
	
	/**
	 * Calculate magnitude of a vector
	 * @return
	 */
	public double Magnitude() {
		return Math.sqrt(Math.pow(this.xPosition, 2) + Math.pow(this.yPosition, 2));
	}
	
	
	/**
	 * Update current vector to be a normalised vector
	 */ 
	public void normalise() {
		double magnitude = Magnitude();
		if(magnitude != 0) {
			this.xPosition /= magnitude;
			this.yPosition /= magnitude;
		}
	}
	
	/**
	 * Limit magnitude of acceleration or speed to be maximum acceleration or speed
	 * @param max
	 * variables defined by max acceleration and speed
	 */
	public void limit(double max) {
		double magnitude = Magnitude();
		if(magnitude != 0 && magnitude < max) {
			this.xPosition *= magnitude/max;
			this.yPosition *= magnitude/max;
		}
	}
	
	/**
	 * Judge the direction of birds' heading
	 * @return
	 */
	public double heading(){
		return Math.atan2(yPosition, xPosition);
	}
	
	/**
	 * Multiply value in x part only
	 * @param value
	 * value multiplied in x part of a Cartesian coordinate
	 */
	public void MultiplyX(double value){
		this.xPosition *= value;
	}
	
	/**
	 * Multiply value in y part only
	 * @param value
	 * value multiplied in y part of a Cartesian coordinate
	 */
	public void MultiplyY(double value){
		this.yPosition *= value;
	}
	
	/**
	 * Subtract two given vectors
	 * @param v1, v2
	 * Cartesian coordinates v1, v2
	 * @return
	 */
	public static CartesianCoordinate SubVectorBetween(CartesianCoordinate v1, CartesianCoordinate v2){
		return new CartesianCoordinate(v1.xPosition-v2.xPosition, v1.yPosition-v2.yPosition);
	}
	
	/**
	 * display the coordinate in console
	 */
	public String toString(){		
		return ("(" + xPosition + "," + yPosition + ")");
				
	}
}
