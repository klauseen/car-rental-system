package rent;

import java.awt.BorderLayout;
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
		setTitle("Terms and Conditions");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(true);
		setLayout(new BorderLayout(10, 15));

		JLabel lblAgreement = new JLabel("NikRent Agreement");
		lblAgreement.setHorizontalAlignment(SwingConstants.CENTER);
		lblAgreement.setFont(new Font("Tahoma", Font.PLAIN, 15));
		add(lblAgreement, BorderLayout.NORTH);

		JTextArea txtByRenting = new JTextArea();
		txtByRenting.setWrapStyleWord(true);
		txtByRenting.setLineWrap(true);
		txtByRenting.setEditable(false);
		txtByRenting.setFont(new Font("Serif", Font.PLAIN, 15));
		txtByRenting.setText("""
				By renting a vehicle from NikRent, you agree to the following terms and conditions:

				1. The renter must hold a valid driving license and be at least 18 years old.

				2. The vehicle must be returned in the same condition as it was rented, excluding normal wear.

				3. Any damage, accident, or malfunction must be reported immediately.

				4. The renter is responsible for all traffic violations, fines, and penalties during the rental period.

				5. The vehicle may not be used for illegal activities, racing, or off-road driving.

				6. Smoking inside the vehicle is strictly prohibited.

				7. Late returns may result in additional charges.

				8. NikRent is not responsible for any personal belongings left in the vehicle.

				9. The rental fee must be paid in advance.

				10. By clicking "Okay", you accept all these terms and conditions.
				""");

		JScrollPane scrollPane = new JScrollPane(txtByRenting);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton btnOkay = new JButton("Okay");
		btnOkay.setFont(new Font("Tahoma", Font.PLAIN, 12));
		buttonPanel.add(btnOkay);

		add(buttonPanel, BorderLayout.SOUTH);

		btnOkay.addActionListener(e -> {
			this.dispose();
		});

	}
}
