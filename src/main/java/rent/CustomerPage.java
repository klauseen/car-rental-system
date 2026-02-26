package rent;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.util.List;
import java.sql.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CustomerPage extends JFrame {

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

	public CustomerPage(String username) {

		setTitle("Customer - View Cars (" + username + ")");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setResizable(true);
		setLayout(new BorderLayout(10, 10));

		// ---------------- Table to show cars ----------------
		DefaultTableModel model = new DefaultTableModel(
				new Object[] { "Car Number", "Make", "Model", "Available", "Price/Day", "Image" }, 0) {

			public Class<?> getColumnClass(int column) {
				if (column == 5) {
					return ImageIcon.class;
				}
				return Object.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableCars = new JTable(model);
		tableCars.setRowHeight(60);
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

				try {
					byte[] imgBytes = DBManager.getCarImage(carID);

					if (imgBytes != null && imgBytes.length > 0) {
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
					} else {
						lblCarImage.setIcon(null);
						lblCarImage.setText("No image available");
					}

				} catch (Exception ex) {
					ex.printStackTrace();
					lblCarImage.setText("Error loading page");
				}
			}
		});
	}

	private void loadCars() {
		try {

			DefaultTableModel model = (DefaultTableModel) tableCars.getModel();
			model.setRowCount(0);

			List<Cars> cars = DBManager.getAllCars();

			for (Cars car : cars) {

				ImageIcon icon = null;
				if (car.getImage() != null) {
					icon = new ImageIcon(car.getImage());
					Image img = icon.getImage().getScaledInstance(100, 60, Image.SCALE_SMOOTH);
					icon = new ImageIcon(img);
				}

				model.addRow(new Object[] { car.getRegNo(), car.getMake(), car.getModel(), car.getAvailable(),
						car.getPricePerDay(), icon });
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	private void rentCar() {
		int selectedRow = tableCars.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a car first.");
			return;
		}

		String regNo = tableCars.getValueAt(selectedRow, 0).toString();
		String available = tableCars.getValueAt(selectedRow, 3).toString();
		double pricePerDay = Double.parseDouble(tableCars.getValueAt(selectedRow, 4).toString());

		if (!available.equalsIgnoreCase("Yes")) {
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
			DBManager.processRental(regNo, dateFrom, dateTo, (int) days, totalFee);

			JOptionPane.showMessageDialog(this, "Car rented successfully. Total: " + totalFee);

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