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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.regex.Pattern;

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
		frame.setBounds(100, 100, 533, 288);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JPanel intro = new JPanel();
		frame.getContentPane().add(intro, INTROPANEL);
		GridBagLayout gbl_intro = new GridBagLayout();
		gbl_intro.columnWidths = new int[]{0, 212, 0, 91, 0};
		gbl_intro.rowHeights = new int[]{0, 22, 0, 17, 23, 17, 0};
		gbl_intro.columnWeights = new double[]{1., 0.0, 1.0, 0.0, 1.0};
		gbl_intro.rowWeights = new double[]{1.0, 0.0, 2.0, 0.0, 0.0, 0.0, 1.0};
		intro.setLayout(gbl_intro);
		
		JLabel lblWelcomeToAjaxbank = new JLabel("Welcome to AJAXBank ATM.");
		lblWelcomeToAjaxbank.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_lblWelcomeToAjaxbank = new GridBagConstraints();
		gbc_lblWelcomeToAjaxbank.anchor = GridBagConstraints.NORTH;
		gbc_lblWelcomeToAjaxbank.insets = new Insets(0, 0, 5, 5);
		gbc_lblWelcomeToAjaxbank.gridwidth = 3;
		gbc_lblWelcomeToAjaxbank.gridx = 1;
		gbc_lblWelcomeToAjaxbank.gridy = 1;
		intro.add(lblWelcomeToAjaxbank, gbc_lblWelcomeToAjaxbank);
		
		JLabel lblPleaseInsertA = new JLabel("Insert card or enter your account number to continue.");
		lblPleaseInsertA.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblPleaseInsertA = new GridBagConstraints();
		gbc_lblPleaseInsertA.anchor = GridBagConstraints.NORTH;
		gbc_lblPleaseInsertA.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblPleaseInsertA.insets = new Insets(0, 0, 5, 0);
		gbc_lblPleaseInsertA.gridwidth = 4;
		gbc_lblPleaseInsertA.gridx = 1;
		gbc_lblPleaseInsertA.gridy = 3;
		intro.add(lblPleaseInsertA, gbc_lblPleaseInsertA);
		
		txtAcc = new JTextField();
		GridBagConstraints gbc_txtAcc = new GridBagConstraints();
		gbc_txtAcc.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAcc.insets = new Insets(0, 0, 5, 5);
		gbc_txtAcc.gridx = 1;
		gbc_txtAcc.gridy = 4;
		intro.add(txtAcc, gbc_txtAcc);
		txtAcc.setColumns(10);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblErrorInvalidPin.setVisible(false);
				((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), PINPANEL);
			}
		});
		GridBagConstraints gbc_btnEnter = new GridBagConstraints();
		gbc_btnEnter.insets = new Insets(0, 0, 5, 5);
		gbc_btnEnter.anchor = GridBagConstraints.NORTH;
		gbc_btnEnter.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnEnter.gridx = 3;
		gbc_btnEnter.gridy = 4;
		intro.add(btnEnter, gbc_btnEnter);
		
		JLabel lblNewLabel = new JLabel("(Note that the account entry textbox is just for the testing stage)");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 5;
		intro.add(lblNewLabel, gbc_lblNewLabel);
		
		JPanel pin = new JPanel();
		frame.getContentPane().add(pin, PINPANEL);
		GridBagLayout gbl_pin = new GridBagLayout();
		gbl_pin.columnWidths = new int[]{39, 86, 89, 41, 0};
		gbl_pin.rowHeights = new int[] {0, 22, 0, 25, 0, 23, 0, 20};
		gbl_pin.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0};
		gbl_pin.rowWeights = new double[]{1., 0.0, 1., 0.0, 1., 0.0, 1.};
		pin.setLayout(gbl_pin);
		
		JLabel lblEnterYourPin = new JLabel("Enter your PIN using the keypad below");
		lblEnterYourPin.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_lblEnterYourPin = new GridBagConstraints();
		gbc_lblEnterYourPin.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblEnterYourPin.anchor = GridBagConstraints.NORTH;
		gbc_lblEnterYourPin.insets = new Insets(0, 0, 5, 0);
		gbc_lblEnterYourPin.gridwidth = 4;
		gbc_lblEnterYourPin.gridx = 1;
		gbc_lblEnterYourPin.gridy = 1;
		pin.add(lblEnterYourPin, gbc_lblEnterYourPin);
		
		lblErrorInvalidPin = new JLabel("Error: Invalid PIN");
		lblErrorInvalidPin.setVisible(false);
		lblErrorInvalidPin.setForeground(Color.RED);
		lblErrorInvalidPin.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_lblErrorInvalidPin = new GridBagConstraints();
		gbc_lblErrorInvalidPin.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblErrorInvalidPin.anchor = GridBagConstraints.NORTH;
		gbc_lblErrorInvalidPin.insets = new Insets(0, 0, 5, 5);
		gbc_lblErrorInvalidPin.gridwidth = 2;
		gbc_lblErrorInvalidPin.gridx = 1;
		gbc_lblErrorInvalidPin.gridy = 3;
		pin.add(lblErrorInvalidPin, gbc_lblErrorInvalidPin);
		
		txtPin = new JTextField();
		GridBagConstraints gbc_txtPin = new GridBagConstraints();
		gbc_txtPin.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPin.insets = new Insets(0, 0, 5, 5);
		gbc_txtPin.gridx = 1;
		gbc_txtPin.gridy = 5;
		pin.add(txtPin, gbc_txtPin);
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
		GridBagConstraints gbc_btnEnter_1 = new GridBagConstraints();
		gbc_btnEnter_1.anchor = GridBagConstraints.NORTH;
		gbc_btnEnter_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnEnter_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnEnter_1.gridx = 2;
		gbc_btnEnter_1.gridy = 5;
		pin.add(btnEnter_1, gbc_btnEnter_1);
		
		JButton btnReturnHomeFromPin = new JButton("Return to home screen");
		btnReturnHomeFromPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), INTROPANEL);
			}
		});
		GridBagConstraints gbc_btnReturnHomeFromPin = new GridBagConstraints();
		gbc_btnReturnHomeFromPin.anchor = GridBagConstraints.SOUTHEAST;
		gbc_btnReturnHomeFromPin.gridx = 4;
		gbc_btnReturnHomeFromPin.gridy = 6;
		pin.add(btnReturnHomeFromPin, gbc_btnReturnHomeFromPin);
		
		JPanel transaction = new JPanel();
		frame.getContentPane().add(transaction, TRANSACTIONPANEL);
		GridBagLayout gbl_transaction = new GridBagLayout();
		gbl_transaction.columnWidths = new int[]{49, 50, 86, 0, 61, 143, 0};
		gbl_transaction.rowHeights = new int[] {33, 17, 17, 23, 23, 23, 31, 23, 0};
		gbl_transaction.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0};
		gbl_transaction.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0};
		transaction.setLayout(gbl_transaction);
		
		JLabel lblErrorInsufficientFunds = new JLabel("Error: Insufficient funds");
		lblErrorInsufficientFunds.setVisible(false);
		
		lblBalance = new JLabel("The user should not see this message");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblBalance = new GridBagConstraints();
		gbc_lblBalance.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblBalance.insets = new Insets(0, 0, 5, 0);
		gbc_lblBalance.gridwidth = 5;
		gbc_lblBalance.gridx = 1;
		gbc_lblBalance.gridy = 1;
		transaction.add(lblBalance, gbc_lblBalance);
		lblErrorInsufficientFunds.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblErrorInsufficientFunds = new GridBagConstraints();
		gbc_lblErrorInsufficientFunds.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblErrorInsufficientFunds.insets = new Insets(0, 0, 5, 5);
		gbc_lblErrorInsufficientFunds.gridwidth = 2;
		gbc_lblErrorInsufficientFunds.gridx = 1;
		gbc_lblErrorInsufficientFunds.gridy = 2;
		transaction.add(lblErrorInsufficientFunds, gbc_lblErrorInsufficientFunds);
		
		rdbtnWithdraw = new JRadioButton("Withdraw");
		buttonGroup.add(rdbtnWithdraw);
		GridBagConstraints gbc_rdbtnWithdraw = new GridBagConstraints();
		gbc_rdbtnWithdraw.anchor = GridBagConstraints.NORTH;
		gbc_rdbtnWithdraw.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnWithdraw.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnWithdraw.gridwidth = 2;
		gbc_rdbtnWithdraw.gridx = 1;
		gbc_rdbtnWithdraw.gridy = 3;
		transaction.add(rdbtnWithdraw, gbc_rdbtnWithdraw);
		
		rdbtnWithdraw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setBtnEnterEnabled();
				
			}
		});
		
		rdbtnDeposit = new JRadioButton("Deposit");
		buttonGroup.add(rdbtnDeposit);
		GridBagConstraints gbc_rdbtnDeposit = new GridBagConstraints();
		gbc_rdbtnDeposit.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnDeposit.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnDeposit.gridwidth = 2;
		gbc_rdbtnDeposit.gridx = 1;
		gbc_rdbtnDeposit.gridy = 4;
		transaction.add(rdbtnDeposit, gbc_rdbtnDeposit);
		
		rdbtnDeposit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setBtnEnterEnabled();
				
			}
		});
		
		JLabel lblAmount = new JLabel("Amount:");
		GridBagConstraints gbc_lblAmount = new GridBagConstraints();
		gbc_lblAmount.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblAmount.insets = new Insets(0, 0, 5, 5);
		gbc_lblAmount.gridx = 1;
		gbc_lblAmount.gridy = 5;
		transaction.add(lblAmount, gbc_lblAmount);
		
		txtAmount = new JTextField();
		txtAmount.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				setBtnEnterEnabled();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				setBtnEnterEnabled();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				setBtnEnterEnabled();
			}
		});
		
		GridBagConstraints gbc_txtAmount = new GridBagConstraints();
		gbc_txtAmount.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAmount.insets = new Insets(0, 0, 5, 5);
		gbc_txtAmount.gridx = 2;
		gbc_txtAmount.gridy = 5;
		transaction.add(txtAmount, gbc_txtAmount);
		txtAmount.setColumns(10);
		
		btnEnterAmount = new JButton("Enter");
		btnEnterAmount.setEnabled(false);
		btnEnterAmount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnDeposit.isSelected()){
					amount = Float.parseFloat(txtAmount.getText());
				}else if (rdbtnWithdraw.isSelected()){
					//Withdrawals are treated as negative deposits.
					//It is also assumed that the user will not have a '-' key on the
					//ATM keypad to avoid the need for number validation.
					amount = -1 * Float.parseFloat(txtAmount.getText());
				}else{
					Utilities.log("No radio button checked, no action performed in response to clicking button");
					return;
				}
				Model.createTransaction(txtAcc.getText(), amount);
				((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), CONFIRMATIONPANEL);
			}
		});
		GridBagConstraints gbc_btnEnterAmount = new GridBagConstraints();
		gbc_btnEnterAmount.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnEnterAmount.insets = new Insets(0, 0, 5, 0);
		gbc_btnEnterAmount.gridwidth = 2;
		gbc_btnEnterAmount.gridx = 4;
		gbc_btnEnterAmount.gridy = 5;
		transaction.add(btnEnterAmount, gbc_btnEnterAmount);
		
		JButton btnReturnHomeFromTransaction = new JButton("Return to home screen");
		btnReturnHomeFromTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), INTROPANEL);
			}
		});
		GridBagConstraints gbc_btnReturnHomeFromTransaction = new GridBagConstraints();
		gbc_btnReturnHomeFromTransaction.insets = new Insets(0, 0, 5, 0);
		gbc_btnReturnHomeFromTransaction.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnReturnHomeFromTransaction.gridx = 5;
		gbc_btnReturnHomeFromTransaction.gridy = 7;
		transaction.add(btnReturnHomeFromTransaction, gbc_btnReturnHomeFromTransaction);
		
		JPanel confirmation = new JPanel();
		frame.getContentPane().add(confirmation, CONFIRMATIONPANEL);
		GridBagLayout gbl_confirmation = new GridBagLayout();
		gbl_confirmation.columnWidths = new int[] {30, 50, 30, 50, 20, 0};
		gbl_confirmation.rowHeights = new int[] {30, 17, 30, 17, 17, 30, 20, 20, 20};
		gbl_confirmation.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_confirmation.rowWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		confirmation.setLayout(gbl_confirmation);
		
		lblConfirmationMessage = new JLabel("The user should not see this");
		lblConfirmationMessage.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblConfirmationMessage = new GridBagConstraints();
		gbc_lblConfirmationMessage.gridwidth = 3;
		gbc_lblConfirmationMessage.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblConfirmationMessage.insets = new Insets(0, 0, 5, 5);
		gbc_lblConfirmationMessage.gridx = 1;
		gbc_lblConfirmationMessage.gridy = 1;
		confirmation.add(lblConfirmationMessage, gbc_lblConfirmationMessage);
		
		lblReceiptMessage1 = new JLabel("The user should not see this");
		lblReceiptMessage1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblReceiptMessage1 = new GridBagConstraints();
		gbc_lblReceiptMessage1.gridwidth = 2;
		gbc_lblReceiptMessage1.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblReceiptMessage1.insets = new Insets(0, 0, 5, 5);
		gbc_lblReceiptMessage1.gridx = 1;
		gbc_lblReceiptMessage1.gridy = 3;
		confirmation.add(lblReceiptMessage1, gbc_lblReceiptMessage1);
		
		lblReceiptMessage2 = new JLabel("The user should not see this");
		lblReceiptMessage2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblReceiptMessage2 = new GridBagConstraints();
		gbc_lblReceiptMessage2.gridwidth = 2;
		gbc_lblReceiptMessage2.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblReceiptMessage2.insets = new Insets(0, 0, 5, 5);
		gbc_lblReceiptMessage2.gridx = 1;
		gbc_lblReceiptMessage2.gridy = 4;
		confirmation.add(lblReceiptMessage2, gbc_lblReceiptMessage2);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), TRANSACTIONPANEL);
			}
		});
		GridBagConstraints gbc_btnBack = new GridBagConstraints();
		gbc_btnBack.anchor = GridBagConstraints.NORTH;
		gbc_btnBack.insets = new Insets(0, 0, 0, 5);
		gbc_btnBack.gridx = 1;
		gbc_btnBack.gridy = 6;
		confirmation.add(btnBack, gbc_btnBack);
		
		JButton btnReturnHomeFromConfirmation = new JButton("Return to home screen");
		btnReturnHomeFromConfirmation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), INTROPANEL);
			}
		});
		GridBagConstraints gbc_btnReturnHomeFromConfirmation = new GridBagConstraints();
		gbc_btnReturnHomeFromConfirmation.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnReturnHomeFromConfirmation.gridx = 3;
		gbc_btnReturnHomeFromConfirmation.gridy = 6;
		confirmation.add(btnReturnHomeFromConfirmation, gbc_btnReturnHomeFromConfirmation);
		
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
				buttonGroup.clearSelection();
				btnEnterAmount.setEnabled(false);
				amount = null; 	//necessary to make failures obvious during testing
				balance = Model.getBalance(txtAcc.getText());
				lblBalance.setText("Your account currently has a balance of $" +
				balance);
				//lblBalance.setSize(lblBalance.getPreferredSize());
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
				//lblConfirmationMessage.setSize(lblConfirmationMessage.getPreferredSize());
				lblReceiptMessage1.setText(String.format("Previous balance: $%.2f", oldbalance));
				//lblReceiptMessage1.setSize(lblReceiptMessage1.getPreferredSize());
				lblReceiptMessage2.setText(String.format("New balance: $%.2f", newbalance));
				//lblReceiptMessage2.setSize(lblReceiptMessage2.getPreferredSize());
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

	protected void setBtnEnterEnabled() {
		boolean value = true;
		if (!rdbtnDeposit.isSelected() && !rdbtnWithdraw.isSelected()){
			value = false;
		}
		if (!Pattern.matches("[0-9]+", txtAmount.getText())){
			value = false;
		}
		btnEnterAmount.setEnabled(value);
	}
}
