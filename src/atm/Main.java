package atm;

/**
 * This is the main class used to run and bootstrap the program. 
 * @author Steven Bluen
 */
public class Main {
	public static void main(String[] args){
		if (args.length > 0 && args[0].equals("prod")){
			Utilities.setEnvironment("prod");;
		} else {
			Utilities.setEnvironment("test");
		}
		Model.initialize();
		Seed.seed();
		View.run();
	}
}
