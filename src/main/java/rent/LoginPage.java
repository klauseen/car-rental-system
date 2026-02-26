package rent;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginPage extends JFrame {

	private JPanel contentPane;
	private JTextField textUserName;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		DBManager.initDatabase();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage frame = new LoginPage();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LoginPage() {


		setForeground(SystemColor.desktop);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(660, 357);

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(626, 294));
		panel.setBorder(BorderFactory.createEmptyBorder());

		contentPane.add(panel, new GridBagConstraints());

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUsername.setBounds(72, 112, 100, 20);
		panel.add(lblUsername);

		textUserName = new JTextField();
		textUserName.setBounds(202, 112, 211, 25);
		panel.add(textUserName);
		textUserName.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPassword.setBounds(72, 158, 100, 20);
		panel.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(202, 158, 211, 25);
		panel.add(passwordField);

		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnLogin.setBounds(202, 211, 90, 30);
		panel.add(btnLogin);

		btnLogin.addActionListener(evt -> handleLogin(evt));

		JButton btnRegister = new JButton("Register");
		btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnRegister.setBounds(323, 211, 90, 30);
		panel.add(btnRegister);

		JLabel lblLogin = new JLabel("Login");
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setBounds(193, 34, 235, 37);
		panel.add(lblLogin);
		lblLogin.setFont(new Font("Tahoma", Font.BOLD, 15));

		JCheckBox checkTerms = new JCheckBox("");
		checkTerms.setBounds(198, 185, 21, 20);
		panel.add(checkTerms);

		JButton btnViewTerms = new JButton("Terms & Conditions");
		btnViewTerms.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnViewTerms.setBounds(200, 185, 140, 20);
		btnViewTerms.setContentAreaFilled(false);
		btnViewTerms.setBorderPainted(false);
		btnViewTerms.setForeground(Color.BLUE);
		panel.add(btnViewTerms);

		btnViewTerms.addActionListener(e -> new TermsAndConditionsPage().setVisible(true));

		btnRegister.addActionListener(e -> handleRegister(checkTerms));
	}

	private void handleLogin(ActionEvent evt) {

		String username = textUserName.getText().trim();
		String password = new String(passwordField.getPassword()).trim();

		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Complete all fields!");
			return;
		}

		try {
			String role = DBManager.loginUser(username, password);

			if (role != null) {
				if (role.equalsIgnoreCase("admin")) {
					MainMenuPage adminFrame = new MainMenuPage();
					adminFrame.setVisible(true);

				} else {
					CustomerPage customerFrame = new CustomerPage(username);
					customerFrame.setVisible(true);
				}
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Invalid username or password!");
			}

		} catch (

		Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Database error!");
		}
	}

	private void handleRegister(JCheckBox checkTerms) {
		String username = textUserName.getText().trim();
		String password = new String(passwordField.getPassword()).trim();

		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Complete all fields!");
			return;
		}

		if (!checkTerms.isSelected()) {
			JOptionPane.showMessageDialog(this, "You must agree to the Terms and Conditions to register!");
			return;
		}

		try {
			String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());

			DBManager.registerUser(username, hashedPassword);

			JOptionPane.showMessageDialog(this, "Registered successfully! You can now login.");

			textUserName.setText("");
			passwordField.setText("");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error while registering! Maybe username already exists.");
		}
	}
}
