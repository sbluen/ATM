package atm;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import java.security.InvalidParameterException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModelTest {
	
	String accnum = "0123456789012345";

	@Before
	public void setUp() throws Exception {
		Model.initialize();
		Model.createAccount(accnum, 5000, "1234");
	}

	@After
	public void tearDown() throws Exception {
		Model.deleteAccount(accnum);
	}

	@Test
	public void testVerify(){
		assertTrue(Model.verify(accnum, "1234"));
		assertFalse(Model.verify(accnum, "1233"));
		assertFalse(Model.verify(accnum, ""));
		assertFalse(Model.verify(accnum, "' or '1'='1"));
	}
	
	@Test
	public void testTransactions(){
		assertEquals(5000, Model.getBalance(accnum), 0);
		Model.createTransaction(accnum, 50);
		assertEquals(5050, Model.getBalance(accnum), 0);
		try{
			Model.createTransaction(accnum, -10000);
			fail("withdrew nonexistant money");
		}catch (InvalidParameterException ex){
			assertThat(ex.getMessage(), is("Cannot withdraw more money " +
					"than exists in customer account"));
			assertEquals(5050, Model.getBalance(accnum), 0);
		}
	}

}
