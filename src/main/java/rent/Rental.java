package rent;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Rental extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tblRented;
	private DefaultTableModel model;
	Connection con;

	public Rental() {
		setTitle("Rental Records");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setExtendedState(JFrame.NORMAL);
		setSize(900 , 600);
		setLocationRelativeTo(null);

	    // main panel
	    contentPane = new JPanel(new BorderLayout(10 , 10));
	    contentPane.setBorder(new EmptyBorder(10 , 10 , 10 , 10));
	    setContentPane(contentPane);
	    
	    JPanel mainPanel = new JPanel(new BorderLayout(10 , 10));
	    mainPanel.setBorder(new EtchedBorder());
	    contentPane.add(mainPanel , BorderLayout.CENTER);
	    

	    // table model
	    model = new DefaultTableModel(
	        new Object[][] {},
	        new String[] {"car_number", "Make", "Model", "Period", "Fee"}
	    );

	    tblRented = new JTable(model);
	    tblRented.setRowHeight(25);

	    // JPanel for the table to show the header
	    JScrollPane scrollPane = new JScrollPane(tblRented);
	    mainPanel.add(scrollPane , BorderLayout.CENTER);

	    // Loads data from DB
	    loadRentals();
	}
	
	private void loadRentals() {
		try {
		        DefaultTableModel model = (DefaultTableModel) tblRented.getModel();
		        model.setRowCount(0); // clears the table
		        
		        List<RentalReport> reports = DBManager.getRents();
		        
		        for(RentalReport report : reports) {
		        	model.addRow(new Object[] {
		        			report.getCarNo(),
		        			report.getMake(),
		        			report.getModel(),
		        			report.getPeriod(),
		        			report.getTotalFee()
		        	});
		        }
		}
		        catch (Exception ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
		    }
	}
}
