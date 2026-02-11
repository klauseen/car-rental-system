package rent;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class TermsAndConditions extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public TermsAndConditions() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 619, 567);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 587, 450);
		contentPane.add(scrollPane);
		
		JTextArea txtrByRentingA = new JTextArea();
		txtrByRentingA.setWrapStyleWord(true);
		txtrByRentingA.setLineWrap(true);
		txtrByRentingA.setEditable(false);
		txtrByRentingA.setFont(new Font("Serif", Font.PLAIN, 15));
		txtrByRentingA.setText("By renting a vehicle from NikRent, you agree to the following terms and conditions:\r\n\r\n1. The renter must hold a valid driving license and be at least 18 years old.\r\n\r\n2. The vehicle must be returned in the same condition as it was rented, excluding normal wear.\r\n\r\n3. Any damage, accident, or malfunction must be reported immediately.\r\n\r\n4. The renter is responsible for all traffic violations, fines, and penalties during the rental period.\r\n\r\n5. The vehicle may not be used for illegal activities, racing, or off-road driving.\r\n\r\n6. Smoking inside the vehicle is strictly prohibited.\r\n\r\n7. Late returns may result in additional charges.\r\n\r\n8. NikRent is not responsible for any personal belongings left in the vehicle.\r\n\r\n9. The rental fee must be paid in advance.\r\n\r\n10. By clicking \"I Agree\", you accept all these terms and conditions.");
		scrollPane.setViewportView(txtrByRentingA);
		
		JLabel lblNewLabel = new JLabel("NikRent Agreement");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPane.setColumnHeaderView(lblNewLabel);
		
		JButton btnAccept = new JButton("Accept");
		btnAccept.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAccept.setBounds(131, 485, 85, 21);
		contentPane.add(btnAccept);
		btnAccept.addActionListener(e ->{
		Customer cust = new Customer();
	    cust.setVisible(true);
	    this.dispose();
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.setBounds(327, 485, 85, 21);
		contentPane.add(btnCancel);
		btnCancel.addActionListener(e ->{
			Login log = new Login();
			log.setVisible(true);
			this.dispose();
		});
	}
}
