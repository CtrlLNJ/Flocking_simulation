package Bird;

import java.awt.BorderLayout;
import java.awt.MouseInfo;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import drawing.Canvas;
import geometry.CartesianCoordinate;

/**
 * Created on 28/02/2019
 * @author Y3852394
 * Description: Update methods of birds, boids algorithms and add sliders controlling speed, influence 
 * of alignment, cohesion and separation, reaction radius. Meanwhile, avoiding mouse method is implemented
 */

public class DynamicBirds extends Birds {  

	/* Initialise variables */
	private boolean included = true;
	
	// Variables storing numbers controlled by sliders and set initial value
	// Including CoAlignment, CoCohesion, CoSeparation, Radius and maxSpeed
	private static double CoAlignment = 0.5;
	private static double CoCohesion = 0.5;
	private static double CoSeparation = 0.1;
	private static double Radius = 100;
	private static double maxSpeed = 10;
	
	// Define max acceleration
	final double maxAcceleration = 0.2;

	// Add three JSliders controlling coefficients cohesion, alignment and separation 
	// as well as speed magnitude, distance from predators to birds
	private static JSlider ControlAlignment;
	private static JSlider ControlCohesion;
	private static JSlider ControlSeparation;
	private static JSlider ControlSpeed;
	private static JSlider Predator;
	
	// Add three JLabels to show titles of cohesion, alignment and separation
	// Add two JLabels to show titles of bird speed and reaction distance
	private static JLabel AlignmentLabel = new JLabel("Alignment");
	private static JLabel CohesionLabel = new JLabel("Cohesion");
	private static JLabel SeparationLabel = new JLabel("Separation");
	private static JLabel BirdsSpeed = new JLabel("Speed magnitude");
	private static JLabel ReactionDis = new JLabel("Reaction Distance");
	
	/* Constructors */
	public DynamicBirds(Canvas canvas) {
		super(canvas);
	}

	public DynamicBirds(Canvas canvas, double xPosition, double yPosition, JPanel panel) {
		super(canvas);
		
		// Define acceleration and initial location
		acceleration = new CartesianCoordinate();	
		this.myLocation = new CartesianCoordinate(xPosition, yPosition);	
		
		// Move the bird to coordinates of myLocation
		this.move(myLocation.getX());
		this.turn(Math.toRadians(90));
		this.move(myLocation.getY());
		
		// Set the DynamicBirds appearing at the given location
		this.draw();
	}
	
	/**
	 * Add three JSliders in high panel to control alignment cohesion and separation
	 * @param panel
	 */
	public static void AddJSliders(JPanel panel){
		// JSlider initialisation
		ControlAlignment = new JSlider(0,10,5);
		ControlCohesion = new JSlider(0,10,5);
		ControlSeparation = new JSlider(0,10,1); 
		ControlSpeed = new JSlider(1,40,10);
		
		// Set increment for slider
		ControlCohesion.setMajorTickSpacing(3);
		ControlCohesion.setMinorTickSpacing(1);
		ControlAlignment.setMajorTickSpacing(3);
		ControlAlignment.setMinorTickSpacing(1);
		ControlSeparation.setMinorTickSpacing(3);
		ControlSeparation.setMajorTickSpacing(1);
		
		// Add JSliders into the panel
		panel.add(BirdsSpeed, BorderLayout.NORTH);
		panel.add(ControlSpeed, BorderLayout.NORTH);
		panel.add(AlignmentLabel, BorderLayout.NORTH);
		panel.add(ControlAlignment, BorderLayout.NORTH);
		panel.add(CohesionLabel, BorderLayout.NORTH);
		panel.add(ControlCohesion, BorderLayout.NORTH);
		panel.add(SeparationLabel, BorderLayout.NORTH);
		panel.add(ControlSeparation, BorderLayout.NORTH);
		
		// The coefficients ready to be multiplied with alignment, cohesion and separation by setting the sliders
		// speed magnitude of flockmates by adjusting slider
		// Anonymous class
		ControlAlignment.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				// Avoid coefficient being an integer thus let value from slider timed by a decimal number
				CoAlignment = ControlAlignment.getValue()*1.0/10;			
			}
		});
		
		ControlCohesion.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				// Avoid coefficient being an integer thus let value from slider timed by a decimal number
				CoCohesion = ControlCohesion.getValue()*1.0/10;
			}
		});
		
		ControlSeparation.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				// Avoid coefficient being an integer thus let value from slider timed by a decimal number
				CoSeparation = ControlSeparation.getValue()*1.0/10;
			}
		});
		
		// The speed magnitude of flocking
		ControlSpeed.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				maxSpeed = ControlSpeed.getValue();
			}
		});
	}
	
	/**
	 * Add a JSliders in lower panel to control reaction distance from predators to flockmates
	 * @param panel
	 */
	public static void AddJSliderLower(JPanel panel){
		// JSlider initialisation
		Predator = new JSlider(100,300,200);
		
		// Add JSliders to the panel
		panel.add(ReactionDis, BorderLayout.NORTH);
		panel.add(Predator, BorderLayout.NORTH);
		
		// Determine reaction distance from predators to birds by setting the slider
		Predator.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				Radius = Predator.getValue();
			}
		});
	}
	
	/**
	 * Update method is to update location of dynamic birds and redraw it in a new location
	 */
	public void update() {	
		speed.VectorAdd(this.acceleration);
		myLocation.VectorAdd(this.speed);
	}
	
	/**
	 * Alignment is using steering force towards the average heading of local flockmates
	 * @param Arraylist<DynamicBirds> birds
	 * A list presumably includes other boids
	 */
	public CartesianCoordinate Alignment(List<DynamicBirds> birds){
		// Set preferred distance of the circle that boids are aligned by
		double perception = 100;
		
		// With assuming the weight of every bird is very small, the steering force can
		// be regarded as an acceleration
		CartesianCoordinate steering = new CartesianCoordinate (0,0);
		int count = 0;
		
		// Include birds close to the bird we concentrate on as flockmates
		for(DynamicBirds b: birds){
			if(!b.included){
				continue;
			}
			
			// Calculate the distance from the centre bird to other birds in the array list
			double distance = Birds.Distance(myLocation, b.myLocation);
			
			// If the bird is not itself and this bird is in the circle with radius of perception,
			// count is added 1 to calculate number of birds in flocking group
			if((distance>0) && (distance<perception)){
				// Add the speed of birds inside the circle with radius of perception for average later
				steering.VectorAdd(b.speed);
				count++;
			}
		}
		
		// If there are birds near the centre bird
		if(count>0){
			// Take average of all distances from the other bird to the centre bird
			steering.Division(count);
			// Set birds to go in the direction of my neighbour but at maximum speed
			// Desired velocity
			steering.normalise();
			steering.Multiplication(DynamicBirds.maxSpeed);
			
			// A steering force equals some desired velocity - current actual velocity
			steering.VectorSub(this.speed);
			
			// Limit the magnitude, the length of the vector to maximum acceleration
			steering.limit(this.maxAcceleration);
		}
		
		// Multiply an eighth to the steering force to change velocity 12.5%
		steering.Multiplication(0.125);
		
		// Return steering force
		return steering;
	}
	
	/**
	 * Cohesion makes a group of birds steer move towards the average position of local flockmates
	 * @param birds
	 *  A list presumably includes other boids
	 * @return 
	 */
	public CartesianCoordinate Cohesion(List<DynamicBirds> birds){
		// Set preferred distance of the circle that boids are aligned by
		double perception = 100;
		
		// With assuming the weight of every bird is very small, the steering force can
		// be regarded as the acceleration
		CartesianCoordinate steering = new CartesianCoordinate (0,0);
		int count = 0;
		
		// Include birds close to the bird we concentrate on as flockmates
		for(DynamicBirds b: birds){
			if(!b.included){
				continue;
			}
			
			// Calculate the distance from the centre bird to other birds in the array list
			double distance = Birds.Distance(this.myLocation, b.myLocation);
			
			// If the bird is not itself and this bird is in the circle with radius of perception
			if((distance>0) && (distance<perception)){
				// Add the location vectors of birds within radius of perception
				steering.VectorAdd(b.myLocation);
				count++;
			}
		}
		
		// If there are birds near the centre bird
		if(count>0){
			// Take average of all distances from the other bird to the centre bird
			steering.Division(count);
			
			// Steer the bird to the average location
			// Take the average location - the current location
			steering.VectorSub(this.myLocation);
			
			// Set birds to go in the direction of my neighbour but at maximum speed
			// Desired velocity
			steering.normalise();
			steering.Multiplication(DynamicBirds.maxSpeed);
			
			// A steering force is equals some desired velocity - current actual velocity
			steering.VectorSub(this.speed);
			
			// Limit the magnitude, the length of the vector to maximum acceleration
			steering.limit(this.maxAcceleration);
		}
		
		// Move flockmates 1% of the way towards the centre so that we multiply steering from Cohesion with 0.01
		steering.Multiplication(0.01);
		
		// Return steering force
		return steering;
	}
	
	/**
	 * Separation is to steer to avoid crowding local flockmates
	 * @param birds
	 * A list presumably includes other boids
	 */
	public CartesianCoordinate Separation(List<DynamicBirds> birds){
		// Set preferred radius of the circle that boids are separated by
		double perception = 20;
		
		// Assuming the weight of every bird is very small, the steering force can
		// be regarded as the acceleration
		CartesianCoordinate steering = new CartesianCoordinate (0,0);
		int count = 0;
		
		// Include birds close to the bird we concentrate on as flockmates
		for(DynamicBirds b: birds){
			if(!b.included){
				continue;
			}
			
			// Calculate the distance from the centre bird to other birds in the array list
			double distance = Birds.Distance(myLocation, b.myLocation);
			
			// If the bird is not itself and this bird is in the circle with radius of perception
			if((distance>0) && (distance<perception)){
				// Vector points from the other birds to the bird itself which points to the opposite direction
				// Position of the bird itself - the other birds' location
				CartesianCoordinate Difference = CartesianCoordinate.SubVectorBetween(myLocation, b.myLocation);
				
				// Difference to be inversely proportional to the distance of local flockmate 
				// so we want the proportion 
				Difference.Division(distance);
				// Sum all vectors pointing away to be steering force
				steering.VectorAdd(Difference);
				
				// Add the location vectors of birds within radius of perception
				count++;
			}
		}
		
		// If there are birds near the centre bird
		if(count>0){
			// Take average of vectors pointing away
			steering.Division(count);
			// Set birds to go in the direction of my neighbour but at maximum speed
			// Desired velocity
			steering.normalise();
			steering.Multiplication(DynamicBirds.maxSpeed);
			
			// A steering force is equals some desired velocity - current actual velocity
			steering.VectorSub(this.speed);
			
			// Limit the magnitude, the length of the vector to maximum acceleration
			steering.limit(this.maxAcceleration);
		}
		
		// Return steering force
		return steering;
	}
	
	
	/**
	 * Flocking takes three rules and add the steering force to the acceleration
	 * @param birds
	 * A list presumably includes other boids
	 */	
	public void Flocking(List<DynamicBirds> birds){
		// Acceleration sohuldn't accumulate over time
		// Every moment, we should start with a net acceleration with zero
		acceleration.Multiplication(0);
		
		// The first rule is alignment to average birds' velocity
		CartesianCoordinate rule1 = Alignment(birds);
		
		// The second rule is Cohesion to steer bird to the average location
		CartesianCoordinate rule2 = Cohesion(birds);
		
		// The third rule is Separation to forbid crowding flockmates
		CartesianCoordinate rule3 = Separation(birds);
		
		// The fourth tule is to let birds avoid mouse
		CartesianCoordinate rule4 = avoidPredator(birds);
		
		// Use sliders to set how much influence three rules should give
		rule1.Multiplication(CoAlignment);
		rule2.Multiplication(CoCohesion);
		rule3.Multiplication(CoSeparation);
		
		// To calculate total force on one item, we just add all vectors together
		// Apply the first rule into the acceleration
		acceleration.VectorAdd(rule1);
		// Apply the second rule into the acceleration
		acceleration.VectorAdd(rule2);
		// Apply the third rule into the acceleration;
		acceleration.VectorAdd(rule3);
		// Apply the fourth rule into the acceleration
		acceleration.VectorAdd(rule4);
		
	}
	
	/**
	 * Acquire mouse location and make birds within the circle with ReactionRadius controlled
	 * by slider stay away from mouse
	 * @param birds
	 * List storing birds added into canvas
	 */
	public CartesianCoordinate avoidPredator(List<DynamicBirds> birds){
		CartesianCoordinate mouseLocation = new CartesianCoordinate();
		CartesianCoordinate steering = new CartesianCoordinate();
		
		// Get mouse location and set it to mouseLocation variable
		mouseLocation.setX(MouseInfo.getPointerInfo().getLocation().getX());
		mouseLocation.setY(MouseInfo.getPointerInfo().getLocation().getY());
		
		for(DynamicBirds b: birds){	
			// Calculate distance between birds' location and mouse current location
			double distance = Birds.Distance(b.myLocation, mouseLocation);
			
			// If birds are in the circle centred mouse location with Radius
			if(distance <= Radius){			
				// Calculate direction of birds
				steering = CartesianCoordinate.SubVectorBetween(b.myLocation, mouseLocation);
				
				// The steering force is set to be the portion in the direction pointing oppositely to mouse with distance
				steering.Division(distance);
				
				// limit the steering force to be max acceleration but in direction 
				// and proportional to how far between mouse and every flockmate 
				steering.limit(maxAcceleration);
			}
		}
		
		// Return steering force
		return steering;
	}
	
}

