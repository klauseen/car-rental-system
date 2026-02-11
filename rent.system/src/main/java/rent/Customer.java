package rent;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import java.sql.*;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Customer extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableCars;
    private JButton btnRent;
    private JDateChooser dateFromChooser;
    private JDateChooser dateToChooser;
    private JLabel lblCarImage;

    // Database
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    private final Action action = new SwingAction();

    public Customer() {
        setTitle("Customer - View Cars");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // ---------------- Table to show cars ----------------
        tableCars = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableCars);
        scrollPane.setBounds(10, 10, 760, 250);
        contentPane.add(scrollPane);

        // ---------------- Label to show car image ----------------
        lblCarImage = new JLabel();
        lblCarImage.setBounds(600, 270, 128, 85);
        lblCarImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        contentPane.add(lblCarImage);

        JLabel lblFrom = new JLabel("From:");
        lblFrom.setBounds(10, 280, 50, 25);
        contentPane.add(lblFrom);

        dateFromChooser = new JDateChooser();
        dateFromChooser.setBounds(60, 280, 120, 25);
        dateFromChooser.setDateFormatString("yyyy-MM-dd");
        contentPane.add(dateFromChooser);

        JLabel lblTo = new JLabel("To:");
        lblTo.setBounds(200, 280, 30, 25);
        contentPane.add(lblTo);

        dateToChooser = new JDateChooser();
        dateToChooser.setBounds(230, 280, 120, 25);
        dateToChooser.setDateFormatString("yyyy-MM-dd");
        contentPane.add(dateToChooser);

        btnRent = new JButton("Rent Selected Car");
        btnRent.setBounds(400, 277, 200, 30);
        contentPane.add(btnRent);

        // Load cars from database
        loadCars();

        btnRent.addActionListener(e -> rentCar());

        tableCars.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableCars.getSelectedRow() != -1) {
                ImageIcon icon = (ImageIcon) tableCars.getValueAt(tableCars.getSelectedRow(), 5);
                lblCarImage.setIcon(icon);
            }
        });
    }

    @SuppressWarnings({ "serial", "unused" })
	private void loadCars() {
        try {
        	con = DriverManager.getConnection(
        		    DBConfig.URL,
        		    DBConfig.USER,
        		    DBConfig.PASSWORD
        		);
            pst = con.prepareStatement("SELECT Car_no, Make, Model, Available, PricePerDay, Image FROM carregistration");
            rs = pst.executeQuery();

            // Table model with image column
            DefaultTableModel model = new DefaultTableModel(
                    new Object[]{"Car ID", "Make", "Model", "Available", "Price per day", "Image"}, 0) {
                @Override
                public Class<?> getColumnClass(int column) {
                    if (column == 5) return ImageIcon.class; // image column
                    return Object.class;
                }
            };

            while (rs.next()) {
                // Load image
                String imagePath = rs.getString("Image");
                ImageIcon icon = null;
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    icon = new ImageIcon(imgFile.getAbsolutePath());
                    Image img = icon.getImage().getScaledInstance(100, 60, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(img);
                } else {
                    // iF it doesn't exist we will show an empty placeholder
                    icon = new ImageIcon(new BufferedImage(100, 60, BufferedImage.TYPE_INT_RGB));
                }

                // fallback: empty image
                if (icon == null) {
                    icon = new ImageIcon(new BufferedImage(100, 60, BufferedImage.TYPE_INT_RGB));
                }
                
                

                model.addRow(new Object[]{
                        rs.getString("car_no"),
                        rs.getString("Make"),
                        rs.getString("Model"),
                        rs.getString("Available"),
                        rs.getDouble("PricePerDay"),
                        icon
                        
                        
                });
            }

            tableCars.setModel(model);
            tableCars.setRowHeight(60);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void rentCar() {
        int selectedRow = tableCars.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car first.");
            return;
        }

        String carNo = tableCars.getValueAt(selectedRow, 0).toString();
        String available = tableCars.getValueAt(selectedRow, 3).toString();
        
        double pricePerDay = Double.parseDouble(tableCars.getValueAt(selectedRow, 4).toString());

        if (!available.equalsIgnoreCase("yes")) {
            JOptionPane.showMessageDialog(this, "This car is not available for rent.");
            return;
        }

        Date dateFrom = dateFromChooser.getDate();
        Date dateTo = dateToChooser.getDate();

        if (dateFrom == null || dateTo == null) {
            JOptionPane.showMessageDialog(this, "Please select rental period.");
            return;
        }

        long diffMillis = dateTo.getTime() - dateFrom.getTime();
        long days = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
        if (days <= 0) {
            JOptionPane.showMessageDialog(this, "Invalid rental period.");
            return;
        }

        double totalFee = days * pricePerDay;
        
        try {
        	Connection con = DriverManager.getConnection(
        		    DBConfig.URL,
        		    DBConfig.USER,
        		    DBConfig.PASSWORD
        		);

            // INSERT in rental table
            String insertSql = """
                INSERT INTO rental (car_no, date_from, date_to, days, total_fee)
                VALUES (?, ?, ?, ?, ?)
            """;

            pst = con.prepareStatement(insertSql);
            pst.setString(1, carNo);
            pst.setDate(2, new java.sql.Date(dateFrom.getTime()));
            pst.setDate(3, new java.sql.Date(dateTo.getTime()));
            pst.setInt(4, (int) days);
            pst.setDouble(5, totalFee);

            pst.executeUpdate();

            // UPDATE availability to carregistration
            String updateSql = "UPDATE carregistration SET available = 'no' WHERE car_no = ?";
            pst = con.prepareStatement(updateSql);
            pst.setString(1, carNo);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Car rented successfully!");

            // Tabel refresh after renting
            loadCars();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while renting car");
        }
       
    }
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}