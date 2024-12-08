package atm;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Class used for database interaction.
 * @author Steven Bluen
 *
 */
public class Model {
	private static final String SALT = "$2a$08$Z2Fcw5eXZq5Qmkf4tTTsfgSSscQucEcH";
	private static Connection conn;
	public static void initialize(){
		conn = Utilities.getConnection();
	}
	
	/**
	 * 
	 * @param accountNumber The account number to verify
	 * @param pin The pin to verify the account with
	 * @return true of verification passes or false otherwise
	 */
	public static boolean verify(String accountNumber, String pin){		
		try {
			PreparedStatement ps = conn.prepareStatement(
					"select pin, create_date from accounts where accno = ?");
			ps.setLong(1, Long.parseLong(accountNumber));
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				if (encrypt(pin, SALT+rs.getString(2)).equals(rs.getString(1))){
					Utilities.log("pin verification passed");
					DbUtils.closeQuietly(rs);
					DbUtils.closeQuietly(ps);
					return true;
				}else{
					Utilities.log("pin=" + pin);
					Utilities.log(SALT+rs.getString(2) + " should equal "+rs.getString(1));
					Utilities.log("pin verification failed, pin doesn't match record");
					DbUtils.closeQuietly(rs);
					DbUtils.closeQuietly(ps);
					return false;
				}
			}else{
				//There were no valid rows.
				Utilities.log("pin verification failed, no valid rows retrieved");
				DbUtils.closeQuietly(rs);
				DbUtils.closeQuietly(ps);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Utilities.log("pin verification failed due to an error condition");
		return false;
	}
	
	/**
	 * 
	 * @param plainText The text to encrypt
	 * @param salt the salt to encrypt with
	 * @return A bcrypt-encrypted string for storing the pin in the db
	 */
	static String encrypt(String plainText, String salt){
		return BCrypt.hashpw(plainText, salt);
	}
	
	/**
	 * Creates an account in the database. This is to be used for initialization
	 * purposes.
	 * @param accountNumber the account number for the account
	 * @param pin the account's pin (before encryption)
	 */
	public static void createAccount(String accountNumber, Float balance, String pin) {
		try {
			PreparedStatement ps = prepare(conn, 
					"insert into accounts (accno, balance, pin) values (?, ?, ?) ",
					Long.parseLong(accountNumber), balance, "");
			ps.executeUpdate();
			//Need to do this because MySQL doesn't have a returning clause
			ps = prepare(conn, "select create_date from accounts where accno=?", 
					Long.parseLong(accountNumber));
			ResultSet rs = ps.executeQuery();
			rs.next();
			String createDate = rs.getString(1);
			ps = prepare(conn, "update accounts set pin=? where accno=?",
					encrypt(pin, SALT+createDate), Long.parseLong(accountNumber));
			Utilities.log(ps.toString());
			ps.executeUpdate();

			DbUtils.closeQuietly(ps);
		} catch (SQLException e) {
			Utilities.log("Account creation failed for account number " + accountNumber);
		}
	}
	
	/**
	 * 
	 * @param accountNumber The account number to get the balance of
	 * @return the balance of the account after every pending transaction is executed
	 */
	public static float getBalance(String accountNumber) {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("select balance from accounts where accno = ?");
			ps.setString(1, accountNumber);
			ResultSet rs = ps.executeQuery();
			rs.next();
			float initialBalance = rs.getFloat(1);
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			
			ps = conn.prepareStatement("select sum(balancechange) from transactions where accno = ?");
			ps.setString(1, accountNumber);
			rs = ps.executeQuery();
			rs.next();
			float totalChange = rs.getFloat(1);
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			
			return initialBalance + totalChange;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Creates a transaction for account number accno for {@literal}amount dollars.
	 * @param accountNumber The account number this transaction will belong to
	 * @param amount The amount added to the account (negative for withdrawals)
	 */
	public static void createTransaction(String accountNumber, Float amount) {
		
		if (getBalance(accountNumber) + amount < 0){
			throw new InvalidParameterException("Cannot withdraw more money " +
		"than exists in customer account");
			
		}
		try {
			PreparedStatement ps = prepare(conn,
				"insert into transactions (accno, balancechange) values (?, ?)",
				accountNumber, amount);
			ps.executeUpdate();
			DbUtils.closeQuietly(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes an account. Used in testing for teardown.
	 * @param accountNumber The account number of the account to delete
	 */
	public static void deleteAccount(String accountNumber) {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("delete from accounts where accno = ?");
		ps.setString(1,  accountNumber);
		ps.executeUpdate();
		DbUtils.closeQuietly(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param conn The connection to prepare a statement with. This is initialized
	 * from outside this method so that it can be closed if necessary.
	 * @param sql The SQL code to parameterize and prepare
	 * @param objects The objects to prepare the SQL with
	 * @return A prepared statement containing the objects parameterized into the SQL.
	 * @throws SQLException When too many objects are passed to this method. This
	 * exception must be handled by the calling code.
	 */
	private static PreparedStatement prepare(Connection conn, String sql, Object...objects) throws SQLException{
		PreparedStatement ps = conn.prepareStatement(sql);
		for (int i=0; i<objects.length; i++){
			ps.setObject(i+1, objects[i]);
		}
		return ps;
	}
}
