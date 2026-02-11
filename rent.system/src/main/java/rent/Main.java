package rent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToggleButton;
import java.awt.Font;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 866, 750);
		
		setLocationRelativeTo(null); // for being centered
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JToggleButton btnCarReg = new JToggleButton("Car Registration");
		btnCarReg.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCarReg.setBounds(293, 154, 240, 79);
		contentPane.add(btnCarReg);
		
		btnCarReg.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		        CarRegistration c = new CarRegistration();
		        c.setVisible(true);// here will open car registration window
		    }
		});
		
		JToggleButton btnRental = new JToggleButton("Rental");
		btnRental.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		btnRental.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		        Rental r = new Rental();
		        r.setVisible(true);  // here will open rental window
		    }
		});
		
		btnRental.setBounds(293, 283, 240, 67);
		contentPane.add(btnRental);
		
		JToggleButton btnLogout = new JToggleButton("Logout");
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLogout.setBounds(293, 403, 240, 72);
		contentPane.add(btnLogout);
		btnLogout.addActionListener(e -> {
		    // Hides the current window
		    this.dispose();
		    Login log = new Login();
		    log.setVisible(true);
		});
	}
}
