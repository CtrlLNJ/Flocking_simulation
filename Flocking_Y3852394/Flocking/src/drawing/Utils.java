package drawing;
import java.util.Random;

/**
 * Created on 28/02/2019
 * @author Y3852394
 * Description: Generate random numbers and pause for several time
 */

public class Utils {
	private static Random randomGenerator = new Random();

	public static void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// We are happy with interruptions, so do not report exception
		}
	}

	public static double randomDouble() {
		return randomGenerator.nextFloat();
	}
	
	public static double randomDoubleBetween(double max, double min){
		return (Math.random()*((max-min)+1)) + min;
	}

	public static int randomInt(int paramInt) {
		return randomGenerator.nextInt(paramInt);
	}
	
	public static int randomIntBetween(int Int1, int Int2){
		return randomGenerator.nextInt(Int2-Int1) + Int1;
	}
}
