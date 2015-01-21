package atm;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class used for misc methods. These will likely need to be changed in the future 
 * before any deployment.
 * @author Steven Bluen
 *
 */
public class Utilities {
	private static String SCHEMA = "atm"; 
	
	@SuppressWarnings("unused") //for quick tests only
	private static void main(String[] args){
		Connection conn = getConnection();
	}

	/**
	 * Gets a connection to the database currently set to hardcoded values. Also
	 * logs any errors that occur, or a success message if none occur.
	 * @return A connection to the database
	 */
	public static Connection getConnection(){
		log("Loading driver...");
		Connection conn;
		
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	//lines below are useful for debugging classpath problems
        	log(ex.getMessage());
        	log(ex.toString());
        	log(ex.getStackTrace().toString());
            System.exit(1);
        }
        
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/"+SCHEMA, "guest", "guest");
            log("Success");
            return conn;
        } catch (SQLException ex) {
            // handle any errors
            log("SQLException: " + ex.getMessage());
            log("SQLState: " + ex.getSQLState());
            log("VendorError: " + ex.getErrorCode());
            System.exit(1);
            return null; //required by Java
        }
	}
	
	/**
	 * Prints a message to the console for logging.
	 * @param message The message to be logged.
	 */
	public static void log(String message){
		System.out.println(message);
	}
}
