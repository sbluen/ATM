package atm;

/**
 * This is the main class used to run and bootstrap the program. 
 * @author Steven Bluen
 */
public class Main {
	public static void main(String[] args){
		Seed.seed();
		Model.initialize();
		View.run();
	}
}
