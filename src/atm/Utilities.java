package atm;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
	public static String environment = "test";
	private static String username;
	private static String password;
	
	@SuppressWarnings("unused") //for quick tests only
	private static void main(String[] args){
		Connection conn = getConnection();
	}
	
	
	/**loads database configuration from a yaml file
	 * 
	 * @param path the path to the yaml file
	 * @return the yaml file
	 */
	
	/**
	 * 
	 * @param env the environment to set
	 */
	public static void setEnvironment(String env){
		environment = env;
	}
	
	private static void getConfig() throws IOException{
		BufferedReader stream;
		stream = new BufferedReader(new FileReader(".config.txt"));
		for (String line = stream.readLine(); line != null; line = stream.readLine()) {
			if (line.contains(environment + "_username")) {
				username = line.split(":")[1].trim();
			} 
			if (line.contains(environment + "_password")) {
				password = line.split(":")[1].trim();
			}
		}
		stream.close();
	}

	/**
	 * Gets a connection to the database currently set to hardcoded values. Also
	 * logs any errors that occur, or a success message if none occur.
	 * @return A connection to the database
	 */
	public static Connection getConnection(){

		try {
			getConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		log("Loading driver...");
		Connection conn;
		
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
        	//Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	//lines below are useful for debugging classpath problems
        	ex.printStackTrace();
            System.exit(1);
            return null; //required by Java
        }
        
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/"+SCHEMA, username, password);
            log("Success");
            return conn;
        } catch (SQLException ex) {
            ex.printStackTrace();
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
