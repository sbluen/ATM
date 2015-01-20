package atm;


/**
 * This class is used to seed the database for demonstration and testing purposes.
 * As a note, account numbers are 1 number long here unlike with typical bank cards
 * to make interface testing easier.
 * @author Steven Bluen
 *
 */
public class Seed {
	/**
	 * 
	 * @param args Unused - seed values are hardcoded. This arg is included only
	 * so that this class can be executed directly from the shell.
	 */
	public static void main(String[] args) {
		Model.initialize();
		Model.createAccount("1", 5000, "1234");
		Model.createAccount("2", 8000, "1337");
		Model.createAccount("3", 10000, "9001");
	}
}
