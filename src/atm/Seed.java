package atm;


/**
 * This class is used to seed the database for demonstration and testing purposes.
 * As a note, account numbers are 1 number long here unlike with typical bank cards
 * to make interface testing easier (This allows account numbers to be typed into dialog
 * boxes more easily).
 * @author Steven Bluen
 *
 */
public class Seed {
	
	@SuppressWarnings("unused") //for quick tests only
	private static void main(String[] args) {
		seed();
	}
	
	/**
	 * Seeds the database with the following account number/balance/pin combinations:
	 * 1/5000/1234
	 * 2/8000/2345
	 * 3/10000/3456
	 */
	public static void seed(){
		Model.initialize();
		Model.createAccount("1", 5000, "1234");
		Model.createAccount("2", 8000, "2345");
		Model.createAccount("3", 10000, "3456");
	}
}
