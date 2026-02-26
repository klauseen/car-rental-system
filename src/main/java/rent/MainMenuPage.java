package rent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToggleButton;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

public class MainMenuPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public MainMenuPage() {
		setTitle("Main Menu");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(900, 700);
	    setLocationRelativeTo(null);
	    setResizable(true);
	    
	    contentPane = new JPanel(new BorderLayout());
		setContentPane(contentPane);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		contentPane.add(centerPanel , BorderLayout.CENTER);
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3 , 1 , 15 , 30));
		buttonPanel.setPreferredSize(new Dimension(300 , 300));
		
		JToggleButton btnCarReg = new JToggleButton("Car Registration");
		btnCarReg.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JToggleButton btnRental = new JToggleButton("Rental");
		btnRental.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JToggleButton btnLogout = new JToggleButton("Logout");
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		buttonPanel.add(btnCarReg);
		buttonPanel.add(btnRental);
		buttonPanel.add(btnLogout);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;            
		gbc.gridy = 0;            
		gbc.weighty = 1.0;  
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		centerPanel.add(buttonPanel, gbc);
		
		btnCarReg.addActionListener(e -> {
		        CarRegistrationPage c = new CarRegistrationPage();
		        c.setVisible(true);// here will open car registration window
		});
		
		
		
		btnRental.addActionListener(e -> {
		        RentalPage r = new RentalPage();
		        r.setVisible(true);  // here will open rental window
		});
		
		
		btnLogout.addActionListener(e -> { //Hides this window
		    LoginPage log = new LoginPage();
		    log.setLocationRelativeTo(null);
		    log.setVisible(true);
		    this.dispose();
		    
		});
	}
}
