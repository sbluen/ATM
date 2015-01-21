package atm;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import java.awt.Color;

/**
 * GUI built using WindowBuilder
 * @author Steven Bluen
 *
 */
public class View {

	private static final String INTROPANEL = "Intro screen";
	private static final String PINPANEL = "Screen for PIN entry";
	private static final String TRANSACTIONPANEL = "Screen for selecting transactions";
	private static final String CONFIRMATIONPANEL = "Screen for displaying confirmation after a transaction";
	private JFrame frame;
	private JTextField txtAcc;
	private JTextField txtPin;
	private JTextField txtAmount;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private static JLabel lblErrorInvalidPin;
	private JRadioButton rdbtnDeposit;
	private JRadioButton rdbtnWithdraw;
	private float balance;
	private JLabel lblBalance;
	private JButton btnEnterAmount;
	private Float amount; //not a primitive, needs to be null under certain circumstances
	private JLabel lblConfirmationMessage;
	private JLabel lblReceiptMessage1;
	private JLabel lblReceiptMessage2;

	/**
	 * Launch the application.
	 */
	public static void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View window = new View();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public View() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JPanel intro = new JPanel();
		frame.getContentPane().add(intro, INTROPANEL);
		intro.setLayout(null);
		
		JLabel lblWelcomeToAjaxbank = new JLabel("Welcome to AJAXBank ATM.");
		lblWelcomeToAjaxbank.setBounds(91, 56, 225, 22);
		lblWelcomeToAjaxbank.setFont(new Font("Tahoma", Font.PLAIN, 18));
		intro.add(lblWelcomeToAjaxbank);
		
		JLabel lblPleaseInsertA = new JLabel("Insert card or enter your account number to continue.");
		lblPleaseInsertA.setBounds(28, 174, 375, 17);
		lblPleaseInsertA.setFont(new Font("Tahoma", Font.PLAIN, 14));
		intro.add(lblPleaseInsertA);
		
		txtAcc = new JTextField();
		txtAcc.setBounds(28, 213, 212, 20);
		intro.add(txtAcc);
		txtAcc.setColumns(10);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.setBounds(314, 212, 91, 23);
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), PINPANEL);
			}
		});
		intro.add(btnEnter);
		
		JPanel pin = new JPanel();
		frame.getContentPane().add(pin, PINPANEL);
		pin.setLayout(null);
		
		JLabel lblEnterYourPin = new JLabel("Enter your PIN using the keypad below");
		lblEnterYourPin.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblEnterYourPin.setBounds(39, 43, 310, 22);
		pin.add(lblEnterYourPin);
		
		txtPin = new JTextField();
		txtPin.setBounds(39, 156, 86, 20);
		pin.add(txtPin);
		txtPin.setColumns(10);
		
		JButton btnEnter_1 = new JButton("Enter");
		btnEnter_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Model.verify(txtAcc.getText(), txtPin.getText())){
					((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), TRANSACTIONPANEL);
				}else{
					lblErrorInvalidPin.setVisible(true);
					lblErrorInvalidPin.invalidate();
				}
			}
		});
		btnEnter_1.setBounds(151, 155, 89, 23);
		pin.add(btnEnter_1);
		
		JButton btnReturnHomeFromPin = new JButton("Return to home screen");
		btnReturnHomeFromPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), INTROPANEL);
			}
		});
		btnReturnHomeFromPin.setBounds(281, 228, 143, 23);
		pin.add(btnReturnHomeFromPin);
		
		lblErrorInvalidPin = new JLabel("Error: Invalid PIN");
		lblErrorInvalidPin.setVisible(false);
		lblErrorInvalidPin.setForeground(Color.RED);
		lblErrorInvalidPin.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblErrorInvalidPin.setBounds(39, 101, 153, 25);
		pin.add(lblErrorInvalidPin);
		
		JPanel transaction = new JPanel();
		frame.getContentPane().add(transaction, TRANSACTIONPANEL);
		transaction.setLayout(null);
		
		txtAmount = new JTextField();
		txtAmount.setBounds(105, 174, 86, 20);
		transaction.add(txtAmount);
		txtAmount.setColumns(10);
		
		JLabel lblAmount = new JLabel("Amount:");
		lblAmount.setBounds(47, 177, 48, 14);
		transaction.add(lblAmount);
		
		rdbtnDeposit = new JRadioButton("Deposit");
		buttonGroup.add(rdbtnDeposit);
		rdbtnDeposit.setBounds(49, 135, 70, 23);
		transaction.add(rdbtnDeposit);
		
		rdbtnWithdraw = new JRadioButton("Withdraw");
		buttonGroup.add(rdbtnWithdraw);
		rdbtnWithdraw.setBounds(49, 109, 109, 23);
		transaction.add(rdbtnWithdraw);
		
		rdbtnDeposit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btnEnterAmount.setEnabled(true);
				
			}
		});
		
		rdbtnWithdraw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btnEnterAmount.setEnabled(true);
				
			}
		});
		
		btnEnterAmount = new JButton("Enter");
		btnEnterAmount.setEnabled(false);
		btnEnterAmount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnDeposit.isSelected()){
					amount = Float.parseFloat(txtAmount.getText());
				}else if (rdbtnWithdraw.isSelected()){
					//Withdrawals are treated as negative deposits.
					amount = -1 * Float.parseFloat(txtAmount.getText());
				}else{
					Utilities.log("No radio button checked, no action performed in response to clicking button");
					return;
				}
				Model.createTransaction(txtAcc.getText(), amount);
				((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), CONFIRMATIONPANEL);
			}
		});
		btnEnterAmount.setBounds(212, 173, 89, 23);
		transaction.add(btnEnterAmount);
		
		JButton btnReturnHomeFromTransaction = new JButton("Return to home screen");
		btnReturnHomeFromTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), INTROPANEL);
			}
		});
		btnReturnHomeFromTransaction.setBounds(281, 228, 143, 23);
		transaction.add(btnReturnHomeFromTransaction);
		
		lblBalance = new JLabel("The user should not see this message");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBalance.setBounds(49, 33, 228, 17);
		transaction.add(lblBalance);
		
		JLabel lblErrorInsufficientFunds = new JLabel("Error: Insufficient funds");
		lblErrorInsufficientFunds.setVisible(false);
		lblErrorInsufficientFunds.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblErrorInsufficientFunds.setBounds(49, 66, 143, 17);
		transaction.add(lblErrorInsufficientFunds);
		
		JPanel confirmation = new JPanel();
		frame.getContentPane().add(confirmation, CONFIRMATIONPANEL);
		confirmation.setLayout(null);
		
		lblConfirmationMessage = new JLabel("The user should not see this");
		lblConfirmationMessage.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblConfirmationMessage.setBounds(41, 42, 171, 17);
		confirmation.add(lblConfirmationMessage);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), TRANSACTIONPANEL);
			}
		});
		btnBack.setBounds(72, 200, 89, 23);
		confirmation.add(btnBack);
		
		JButton btnReturnHomeFromConfirmation = new JButton("Return to home screen");
		btnReturnHomeFromConfirmation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), INTROPANEL);
			}
		});
		btnReturnHomeFromConfirmation.setBounds(281, 228, 143, 23);
		confirmation.add(btnReturnHomeFromConfirmation);
		
		lblReceiptMessage1 = new JLabel("The user should not see this");
		lblReceiptMessage1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblReceiptMessage1.setBounds(41, 102, 171, 17);
		confirmation.add(lblReceiptMessage1);
		
		lblReceiptMessage2 = new JLabel("The user should not see this");
		lblReceiptMessage2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblReceiptMessage2.setBounds(41, 133, 171, 17);
		confirmation.add(lblReceiptMessage2);
		
		intro.addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorAdded(AncestorEvent event) {
				txtAcc.setText("");
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				//no response needed in required implementation
			}

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				//no response needed in required implementation
			}
		});
		
		pin.addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorAdded(AncestorEvent event) {
				txtPin.setText("");
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				//no response needed in required implementation
			}

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				//no response needed in required implementation
			}
		});		
		
		transaction.addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorAdded(AncestorEvent event) {
				txtAmount.setText("");
				rdbtnDeposit.setSelected(false);
				rdbtnWithdraw.setSelected(false);
				btnEnterAmount.setEnabled(false);
				amount = null; 	//necessary to make failures obvious during testing
				balance = Model.getBalance(txtAcc.getText());
				lblBalance.setText("Your account currently has a balance of $" +
				balance);
				lblBalance.setSize(lblBalance.getPreferredSize());
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				//no response needed in required implementation
			}

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				//no response needed in required implementation
			}
		});
		
		confirmation.addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorAdded(AncestorEvent event) {
				String action;
				String preposition;
				
				if (rdbtnDeposit.isSelected()){
					action = "deposited";
					preposition = "into"; 
				} else if (rdbtnWithdraw.isSelected()){
					action = "withdrawn";
					preposition = "from";
				} else{
					Utilities.log("Error: Inconsistent state reached");
					return;
				}
				
				String accno = txtAcc.getText();
				float newbalance = Model.getBalance(accno);
				float oldbalance = newbalance - amount;
				
				lblConfirmationMessage.setText(String.format("You have %s $%s %s your account.", action, Math.abs(amount), preposition));
				lblConfirmationMessage.setSize(lblConfirmationMessage.getPreferredSize());
				lblReceiptMessage1.setText(String.format("Previous balance: $%.2f", oldbalance));
				lblReceiptMessage1.setSize(lblReceiptMessage1.getPreferredSize());
				lblReceiptMessage2.setText(String.format("New balance: $%.2f", newbalance));
				lblReceiptMessage2.setSize(lblReceiptMessage2.getPreferredSize());
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				//no response needed in required implementation
			}

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				//no response needed in required implementation
			}
		});
	}
}
