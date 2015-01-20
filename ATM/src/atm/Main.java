package atm;

/**
 * 
 * @author Steven Bluen
 * This is the main class used to run and boostrap the program. 
 */
public class Main {
	public static void main(String[] args){
		Model.initialize();
		View.run();
	}
}
