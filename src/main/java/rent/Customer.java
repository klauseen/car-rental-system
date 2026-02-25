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
	private JTable tableCars;
	private JButton btnRent;
	private JDateChooser dateFromChooser;
	private JDateChooser dateToChooser;
	private JLabel lblCarImage;
	// Database
	PreparedStatement pst;
	ResultSet rs;
	private final Action action = new SwingAction();

	public Customer(String username) {

		setTitle("Customer - View Cars (" + username + ")");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setResizable(true);
		setLayout(new BorderLayout(10, 10));

		// ---------------- Table to show cars ----------------
		tableCars = new JTable();
		JScrollPane scrollPane = new JScrollPane(tableCars);
		add(scrollPane, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout(20, 10));
		add(bottomPanel, BorderLayout.SOUTH);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

		// ---------------- Label to show car image ----------------

		JLabel lblFrom = new JLabel("From:");

		dateFromChooser = new JDateChooser();
		dateFromChooser.setBounds(60, 280, 120, 25);
		dateFromChooser.setDateFormatString("yyyy-MM-dd");
		dateFromChooser.setPreferredSize(new Dimension(130, 25));

		JLabel lblTo = new JLabel("To:");
		dateToChooser = new JDateChooser();
		dateToChooser.setBounds(230, 280, 120, 25);
		dateToChooser.setDateFormatString("yyyy-MM-dd");
		dateToChooser.setPreferredSize(new Dimension(130, 25));

		btnRent = new JButton("Rent Selected Car");

		leftPanel.add(lblFrom);
		leftPanel.add(dateFromChooser);
		leftPanel.add(lblTo);
		leftPanel.add(dateToChooser);
		leftPanel.add(btnRent);

		bottomPanel.add(leftPanel, BorderLayout.CENTER);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout(10, 10));

		lblCarImage = new JLabel();
		lblCarImage.setBounds(600, 270, 128, 85);
		lblCarImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		rightPanel.add(lblCarImage, BorderLayout.CENTER);

		bottomPanel.add(rightPanel, BorderLayout.EAST);

		btnRent.addActionListener(e -> rentCar());

		// Load cars from database
		loadCars();

		tableCars.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tableCars.getSelectedRow() != -1) {
				int row = tableCars.getSelectedRow();
				String carID = tableCars.getValueAt(row, 0).toString();

				try (Connection con = DBConfig.getConnection();
						PreparedStatement pstImage = con
								.prepareStatement("SELECT Image FROM carregistration WHERE car_number = ?")) {

					pstImage.setString(1, carID);
					try (ResultSet rsImage = pstImage.executeQuery()) {
						if (rsImage.next()) {
							byte[] imgBytes = rsImage.getBytes("Image");
							if (imgBytes != null) {
								ImageIcon fullIcon = new ImageIcon(imgBytes);
								Image img = fullIcon.getImage();

								int maxWidth = 400;
								int maxHeight = 250;

								double widthRatio = (double) maxWidth / img.getWidth(null);
								double heightRatio = (double) maxHeight / img.getHeight(null);
								double ratio = Math.min(widthRatio, heightRatio);

								int targetWidth = (int) (img.getWidth(null) * ratio);
								int targetHeight = (int) (img.getHeight(null) * ratio);

								Image scaledImg = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
								lblCarImage.setIcon(new ImageIcon(scaledImg));

								lblCarImage.setText("");
							}
						}
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		});
	}

	@SuppressWarnings({ "serial", "unused" })
	private void loadCars() {
		try {
			Connection con = DBConfig.getConnection();
			pst = con.prepareStatement(
					"SELECT car_number, Make, Model, Available, PricePerDay, Image FROM carregistration");
			rs = pst.executeQuery();

			// Table model with image column
			DefaultTableModel model = new DefaultTableModel(
					new Object[] { "Car ID", "Make", "Model", "Available", "Price per day", "Image" }, 0) {
				@Override
				public Class<?> getColumnClass(int column) {
					if (column == 5)
						return ImageIcon.class; // image column
					return Object.class;
				}

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			while (rs.next()) {
				// Load image
				byte[] imgBytes = rs.getBytes("Image");
				ImageIcon icon = null;

				if (imgBytes != null && imgBytes.length > 0) {
					icon = new ImageIcon(imgBytes);
					Image img = icon.getImage().getScaledInstance(100, 60, Image.SCALE_SMOOTH);
					icon = new ImageIcon(img);
				} else {
					// iF it doesn't exist we will show an empty placeholder
					icon = new ImageIcon(new BufferedImage(100, 60, BufferedImage.TYPE_INT_RGB));
				}

				model.addRow(new Object[] { rs.getString("car_number"), rs.getString("Make"), rs.getString("Model"),
						rs.getString("Available"), rs.getDouble("PricePerDay"), icon });
			}

			tableCars.setModel(model);
			tableCars.setRowHeight(60);

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this,"Error to upload cars: " + ex.getMessage());
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
			Connection con =DBConfig.getConnection();

			// INSERT in rental table
			String insertSql = """
					    INSERT INTO rental (car_number, date_from, date_to, days, total_fee)
					    VALUES (?, ?, ?, ?, ?)
					""";

			try(PreparedStatement pstInsert = con.prepareStatement(insertSql)){
				pst = con.prepareStatement(insertSql);
				pst.setString(1, carNo);
				pst.setDate(2, new java.sql.Date(dateFrom.getTime()));
				pst.setDate(3, new java.sql.Date(dateTo.getTime()));
				pst.setInt(4, (int) days);
				pst.setDouble(5, totalFee);

				pst.executeUpdate();
			}

			// UPDATE availability to carregistration
			String updateSql = "UPDATE carregistration SET available = 'no' WHERE car_number = ?";
			try(PreparedStatement pstUpdate = con.prepareStatement(updateSql)){
				pst.setString(1, carNo);
				pst.executeUpdate();

			}
			JOptionPane.showMessageDialog(this, "Car rented successfully!");

			// Tabel refresh after renting
			loadCars();

		} catch (Exception ex) {
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