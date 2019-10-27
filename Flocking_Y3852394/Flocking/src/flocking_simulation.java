import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Bird.DynamicBirds;
import drawing.Canvas;
import drawing.Utils;

/**
 * Created on 28/02/2019
 * @author Y3852394
 * Description: main program to run the simulation
 * Main GUI interface and game loop are implemented
 */

public class flocking_simulation {
	
	/* Initialise variables */
	private List<DynamicBirds> birds;
	boolean coutinueRunning = true;
	private JButton addBirdsButton;
	private JPanel lowerPanel, highPanel;
	private double xPosition, yPosition;
	Canvas canvas;
	
	/* Constructors */
	public flocking_simulation(){
		/* Frame and canvas initialisation */
		JFrame frame = new JFrame();
		canvas = new Canvas(1200,900);
		frame.setTitle("Flocking simulation");
		frame.setSize(1200, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		frame.setVisible(true);
		frame.add(canvas, BorderLayout.CENTER);
		
		/* JPanel initialisation */
		lowerPanel = new JPanel();
		highPanel = new JPanel();
		frame.add(highPanel, BorderLayout.NORTH);
		frame.add(lowerPanel, BorderLayout.SOUTH);
		
		/* JButton initialisation */
		addBirdsButton = new JButton("Add Birds");
		lowerPanel.add(addBirdsButton);
		DynamicBirds.AddJSliderLower(lowerPanel);
		DynamicBirds.AddJSliders(highPanel);
		
		/* Refresh the canvas to keep all components within the frame*/
		frame.revalidate();
		
		// Synchronised list to transform ArrayList to List which should be synchronized
		birds = Collections.synchronizedList(new ArrayList<DynamicBirds>());
		
		// Start the game loop
		gameLoop(frame,canvas);
		
	}
	
	/**
	 * The game loop adding birds and updating birds movements
	 * @param frame, canvas
	 */
	private void gameLoop(JFrame frame, Canvas canvas){
		int deltaTime = 100;	
		
		// Anonymous Classes to get order when add birds button clicked
		addBirdsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
				// Add 100 birds per time to random places of interface after pressing the button
				for(int i=0; i<100; i++) {
					xPosition = Utils.randomInt(800);
					yPosition = Utils.randomInt(600);
					birds.add(new DynamicBirds(canvas,xPosition,yPosition,highPanel));}
			}
		});
		
		while (coutinueRunning){
			// Synchronisation to try get rid of concurrentModificationExcetion error
			// Undraw previous bird
			synchronized (birds) {
				for(DynamicBirds boids: birds){
					boids.undraw();			 
				}			
			}
			
			// Implement flocking algorithms
			synchronized (birds) {
				for (DynamicBirds boids: birds){
					boids.Flocking(birds);
				}
			}

			// In case that birds hit the edges
			synchronized (birds){
				for (DynamicBirds boids: birds){
					boids.wrapPosition(frame);
				}
			}
			
			// Update location of flockmates
			synchronized (birds) {
				for (DynamicBirds boids: birds){
					boids.update();	
				}				
			}
			
			// Draw birds at calculated location
			synchronized (birds) {
				for (DynamicBirds boids: birds){
					boids.draw();
				}				
			}
			
			// Pause 100 milliseconds
			Utils.pause(deltaTime);
		}
	}
	
	/**
	 * Main function running the whole program
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Running TurtleProgram...");
		new flocking_simulation();
	}

}
