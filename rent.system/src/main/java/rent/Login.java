package rent;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textUserName;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setForeground(SystemColor.desktop);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 652, 357);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "LOGIN", TitledBorder.CENTER, TitledBorder.BELOW_TOP, null, SystemColor.desktop));
		panel.setBounds(10, 57, 618, 247);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_1.setBounds(27, 43, 79, 22);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Password");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_2.setBounds(27, 104, 79, 22);
		panel.add(lblNewLabel_2);
		
		textUserName = new JTextField();
		textUserName.setBounds(27, 75, 224, 19);
		panel.add(textUserName);
		textUserName.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnLogin.setBounds(27, 188, 100, 34);
		panel.add(btnLogin);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnCancel.setBounds(159, 188, 92, 34);
		panel.add(btnCancel);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(27, 136, 224, 19);
		panel.add(passwordField);
		
		JButton btnCustomer = new JButton("Customer");
		btnCustomer.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnCustomer.setBounds(365, 75, 202, 80);
		panel.add(btnCustomer);
		btnCustomer.addActionListener(e -> {
		    TermsAndConditions tac = new TermsAndConditions();
		    tac.setVisible(true);
		    this.dispose();
		});
		
		
		
		JLabel lblNewLabel = new JLabel("Nikolaos' Rent Login\r\n\r\n");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel.setToolTipText("");
		lblNewLabel.setBounds(12, 10, 235, 37);
		contentPane.add(lblNewLabel);
		
		btnLogin.addActionListener(evt -> handleLogin(evt));
		btnCancel.addActionListener(e -> System.exit(0));
	}
	
	private void handleLogin(java.awt.event.ActionEvent evt) {
		
		String username = textUserName.getText();
		char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        System.out.println("Parola introdusă: " + password);
        
        if (username.equals("Nikolaos") && password.equals("123")) {
        	
        	Main m = new Main();
        	this.setVisible(false);
        	m.setVisible(true);
        }
        else {
        	JOptionPane.showMessageDialog(this,"Username and password do not match");
        }
	}
}
