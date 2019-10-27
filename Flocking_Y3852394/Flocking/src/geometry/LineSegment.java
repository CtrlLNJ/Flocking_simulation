package geometry;

public class LineSegment {
	
	// Edit field
	CartesianCoordinate startPoint;
	CartesianCoordinate endPoint;
	
	// Initialise field used in length()
	private double distance;
	
	//?? Is this the length method meaning
	private double x1 = 1.4;
	private double x2 = 4.4;
	private double y1 = 2.2;
	private double y2 = 6.2;
	
	// Set a constructor
		public LineSegment(CartesianCoordinate x, CartesianCoordinate y){
			// Set new CartesianCoordinate get set which is in case of passing values by reference
			startPoint = new CartesianCoordinate(x.getX(), x.getY());
			endPoint = new CartesianCoordinate(y.getX(), y.getY());
		}
	
	// Getter method
	public CartesianCoordinate getStartPoint() {
		return startPoint;
	}
	
	public CartesianCoordinate getEndPoint() {
		return endPoint;
	}
	
	// Setter method
	public void setStartPoint(CartesianCoordinate startPoint) {
		this.startPoint = startPoint;
	}

	public void setEndPoint(CartesianCoordinate endPoint) {
		this.endPoint = endPoint;
	}
	
	// toString method
	public String toString() {
		return "LineSegment [startPoint=" + startPoint + ", endPoint=" + endPoint + "]";
	}

	// Method to calculate the distance between the start and end points
	public double length() {
		
		distance = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
		
		return distance;

	}

}
