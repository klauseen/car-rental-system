package rent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
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
	    //setDefaultCloseOperation(JFrame.);
	    setBounds(100, 100, 905, 557);
	    contentPane = new JPanel();
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    contentPane.setLayout(null);
	    setContentPane(contentPane);

	    // main panel
	    JPanel panel = new JPanel();
	    panel.setBorder(new TitledBorder(null, "Rental", TitledBorder.LEADING, TitledBorder.BELOW_TOP, null, null));
	    panel.setBounds(10, 10, 859, 500);
	    panel.setLayout(null);
	    contentPane.add(panel);

	    // table model
	    model = new DefaultTableModel(
	        new Object[][] {},
	        new String[] {"Car_No", "Make", "Model", "Period", "Fee"}
	    );

	    tblRented = new JTable(model);
	    tblRented.setRowHeight(25);

	    // JPanel for the table to show the header
	    JScrollPane scrollPane = new JScrollPane(tblRented);
	    scrollPane.setBounds(50, 50, 750, 400);
	    panel.add(scrollPane);

	    // Loads data from DB
	    loadRentals();
	}
	
	private void loadRentals() {
		try {
        	con = DriverManager.getConnection(
        		    DBConfig.URL,
        		    DBConfig.USER,
        		    DBConfig.PASSWORD
        		);
		        PreparedStatement pst = con.prepareStatement("""
		            SELECT 
		                r.car_no,
		                c.make,
		                c.model,
		                CONCAT(r.date_from, ' → ', r.date_to) AS period,
		                r.total_fee
		            FROM rental r
		            JOIN carregistration c ON r.car_no = c.car_no
		        """);
		        ResultSet rs = pst.executeQuery();

		        DefaultTableModel model = (DefaultTableModel) tblRented.getModel();
		        model.setRowCount(0); // clears the table

		        // Set to track unique records
		        Set<String> seen = new HashSet<>();

		        while (rs.next()) {
		            // Creates a unique key for every row
		            String key = rs.getString("car_no") + "|" +
		                         rs.getString("period") + "|" +
		                         rs.getDouble("total_fee");

		            if (!seen.contains(key)) {
		                model.addRow(new Object[]{
		                    rs.getString("car_no"),
		                    rs.getString("make"),
		                    rs.getString("model"),
		                    rs.getString("period"),
		                    rs.getDouble("total_fee")
		                });
		                seen.add(key); // marks that's added
		            }
		        }
		}
		        catch (SQLException ex) {
		        ex.printStackTrace();
		    }
	}
}
