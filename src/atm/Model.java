package atm;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;

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
					"select pin from accounts where accno = ?");
			ps.setString(1, accountNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				if (encrypt(pin).equals(rs.getString(1))){
					Utilities.log("pin verification passed");
					DbUtils.closeQuietly(rs);
					DbUtils.closeQuietly(ps);
					return true;
				}else{
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
		
	static String encrypt(String plainText){
		return BCrypt.hashpw(plainText, SALT);
	}
	
	/**
	 * Creates an account in the database. This is to be used for initialization
	 * purposes.
	 * @param accountNumber the account number for the account
	 * @param pin the account's pin (before encryption)
	 */
	public static void createAccount(String accountNumber, float balance, String pin) {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("insert into accounts (accno, balance, pin) values (?, ?, ?)");
			ps.setString(1, accountNumber);
			ps.setFloat(2, balance);
			ps.setString(3, encrypt(pin));
			ps.executeUpdate();
			DbUtils.closeQuietly(ps);
		} catch (SQLException e) {
			e.printStackTrace();
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
	public static void createTransaction(String accountNumber, float amount) {
		
		if (getBalance(accountNumber) + amount < 0){
			throw new InvalidParameterException("Cannot withdraw more money " +
		"than exists in customer account");
			
		}
		try {
			PreparedStatement ps = conn.prepareStatement("insert into transactions (accno, balancechange) values (?, ?)");
			ps.setString(1,  accountNumber);
			ps.setFloat(2, amount);
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
}
