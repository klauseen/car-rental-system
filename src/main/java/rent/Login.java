package rent;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame {

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
		lblUsername.setBounds(72, 96, 100, 20);
		panel.add(lblUsername);

		textUserName = new JTextField();
		textUserName.setBounds(202, 96, 211, 25);
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
		btnLogin.setBounds(202, 207, 90, 30);
		panel.add(btnLogin);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnCancel.setBounds(526, 254, 90, 30);
		panel.add(btnCancel);

		btnLogin.addActionListener(evt -> handleLogin(evt));
		btnCancel.addActionListener(e -> System.exit(0));

		JButton btnRegister = new JButton("Register");
		btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnRegister.setBounds(323, 207, 90, 30);
		panel.add(btnRegister);

		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(193, 34, 235, 37);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));

		btnRegister.addActionListener(e -> handleRegister());
	}

	private void handleLogin(ActionEvent evt) {

		String username = textUserName.getText().trim();
		String password = new String(passwordField.getPassword()).trim();

		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Complete all fields!");
			return;
		}

		String sql = "SELECT password, role FROM users WHERE username = ?";

		try (Connection conn = DBConfig.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, username);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {

				String storedHash = rs.getString("password");
				String role = rs.getString("role");

				if (org.mindrot.jbcrypt.BCrypt.checkpw(password, storedHash)) {
					if (role.equalsIgnoreCase("admin")) {

						MainMenu adminFrame = new MainMenu();
						adminFrame.setVisible(true);

					} else {

						Customer customerFrame = new Customer(username);
						customerFrame.setVisible(true);
					}

					this.dispose();

				}
			} else {
				JOptionPane.showMessageDialog(this, "Invalid username or password!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Database error!");
		}
	}

	private void handleRegister() {
		String username = textUserName.getText().trim();
		String password = new String(passwordField.getPassword()).trim();

		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Complete all fields!");
			return;
		}

		String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());

		String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'customer')";

		try (Connection conn = DBConfig.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, username);
			pst.setString(2, hashedPassword);

			pst.executeUpdate();

			JOptionPane.showMessageDialog(this, "Registered successfully! You can now login.");
			textUserName.setText("");
			passwordField.setText("");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error while registering! Maybe username already exists.");
		}
	}
}
