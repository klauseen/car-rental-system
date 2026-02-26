package rent;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CarRegistrationPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtMake;
	private JTextField txtModel;
	private JTextField txtColour;
	private JTextField txtType;
	private JTextField txtPricePerDay;
	private JTable tblCars;
	String imagePath = null;
	private JTextField txtReg;
	/**
	 * Launch the application.
	 */

	private JComboBox<String> cBAvailable;

	private void loadCars() {

		try {
			DefaultTableModel model = (DefaultTableModel) tblCars.getModel();
			model.setRowCount(0);

			List<Cars> cars = DBManager.getAllCars();

			for (Cars car : cars) {
				ImageIcon icon = null;
				if (car.getImage() != null && car.getImage().length > 0) {
					icon = new ImageIcon(car.getImage());
					Image img = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
					icon = new ImageIcon(img);
				}

				model.addRow(new Object[] { icon, car.getRegNo(), car.getMake(), car.getModel(), car.getColour(),
						car.getType(), car.getPricePerDay(), car.getAvailable()

				});
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error loading Cars: " + e.getMessage());
		}

	}

	public void autoID() {
		try {
			String nextID = DBManager.IDAuto();

			txtReg.setText(nextID);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error to database: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@SuppressWarnings("serial")
	public CarRegistrationPage() {

		setTitle("Car Registration");
		setSize(1200, 700);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
		setContentPane(mainPanel);

		JPanel leftPanel = new JPanel(new GridBagLayout());
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel bottomPanel = new JPanel(new BorderLayout());

		JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel lblRegNo = new JLabel("Registration No");
		lblRegNo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;

		leftPanel.add(lblRegNo, gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtReg = new JTextField();
		txtReg.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtReg.setColumns(5);
		leftPanel.add(txtReg, gbc);

		JLabel lblMake = new JLabel("Make");
		lblMake.setFont(new Font("Tahoma", Font.PLAIN, 12));

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;

		leftPanel.add(lblMake, gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtMake = new JTextField();
		txtMake.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtMake.setColumns(10);
		leftPanel.add(txtMake, gbc);

		JLabel lblModel = new JLabel("Model");
		lblModel.setFont(new Font("Tahoma", Font.PLAIN, 12));

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;

		leftPanel.add(lblModel, gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtModel = new JTextField();
		txtModel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtModel.setColumns(10);
		leftPanel.add(txtModel, gbc);

		JLabel lblColour = new JLabel("Colour");
		lblColour.setFont(new Font("Tahoma", Font.PLAIN, 12));

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0;

		leftPanel.add(lblColour, gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtColour = new JTextField();
		txtColour.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtColour.setColumns(10);
		leftPanel.add(txtColour, gbc);

		JLabel lblType = new JLabel("Type");
		lblType.setFont(new Font("Tahoma", Font.PLAIN, 12));

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0;

		leftPanel.add(lblType, gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtType = new JTextField();
		txtType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtType.setColumns(10);
		leftPanel.add(txtType, gbc);

		JLabel lblPrice = new JLabel("Price / day");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0;

		leftPanel.add(lblPrice, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtPricePerDay = new JTextField();
		txtPricePerDay.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtPricePerDay.setColumns(10);
		leftPanel.add(txtPricePerDay, gbc);

		JLabel lblAvailable = new JLabel("Available");
		lblAvailable.setFont(new Font("Tahoma", Font.PLAIN, 12));

		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		leftPanel.add(lblAvailable, gbc);

		cBAvailable = new JComboBox<>();
		cBAvailable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cBAvailable.setModel(new DefaultComboBoxModel(new String[] { "Yes", "No" }));

		gbc.gridx = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		leftPanel.add(cBAvailable, gbc);
		
		JPanel uploadButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton btnUpload = new JButton("Upload Image");
		btnUpload.setPreferredSize(new Dimension(150 , 35));
		btnUpload.setFont(new Font("Tahoma" ,Font.BOLD , 12));
		uploadButtonPanel.add(btnUpload);
		bottomPanel.add(uploadButtonPanel , BorderLayout.NORTH);

		btnUpload.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			int result = chooser.showOpenDialog(null);

			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();

				// Creates folder image if it doesn't exist
				File imagesDir = new File("images");
				if (!imagesDir.exists()) {
					imagesDir.mkdirs();
				}

				// saves image path in project
				imagePath = "images/" + selectedFile.getName();

				try {
					Files.copy(selectedFile.toPath(), Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);

					ImageIcon icon = new ImageIcon(imagePath);
					icon.setDescription(imagePath);

					System.out.println("Imaginea a fost încărcată: " + imagePath);
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(this, "Error to upload the image: " + ex.getMessage());
				}
			}
		});

		tblCars = new JTable();
		JScrollPane scrollPane = new JScrollPane(tblCars);
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		DefaultTableModel model = new DefaultTableModel(
				new Object[] { "Photo", "Reg No", "Make", "Model", "Colour", "Type", "Price / day", "Available" }, 0) {
			@Override
			public Class<?> getColumnClass(int column) {
				if (column == 0)
					return ImageIcon.class;
				return String.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblCars.setModel(model);
		tblCars.setRowHeight(80);
		tblCars.getColumnModel().getColumn(0).setPreferredWidth(80);
		tblCars.setFont(new Font("Tahoma", Font.PLAIN, 14));

		tblCars.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {

				int row = tblCars.getSelectedRow();
				if (row == -1)
					return;

				txtReg.setText(model.getValueAt(row, 1).toString());
				txtMake.setText(model.getValueAt(row, 2).toString());
				txtModel.setText(model.getValueAt(row, 3).toString());
				txtColour.setText(model.getValueAt(row, 4).toString());
				txtType.setText(model.getValueAt(row, 5).toString());
				txtPricePerDay.setText(model.getValueAt(row, 6).toString());
				cBAvailable.setSelectedItem(model.getValueAt(row, 7).toString());

				// image
				ImageIcon icon = (ImageIcon) model.getValueAt(row, 0);
				imagePath = icon != null ? icon.getDescription() : null;
			}
		});

		tblCars.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = tblCars.rowAtPoint(evt.getPoint());
				if (row == -1)
					return;
				int col = tblCars.columnAtPoint(evt.getPoint());
				if (col == 0) { // click on picture
					ImageIcon icon = (ImageIcon) tblCars.getValueAt(row, col);
					JFrame f = new JFrame();
					f.setSize(400, 400);
					JLabel lbl = new JLabel(icon);
					f.getContentPane().add(lbl);
					f.setVisible(true);

					icon = (ImageIcon) model.getValueAt(row, 0);
					imagePath = icon != null ? icon.getDescription() : null;
				}
			}
		});

		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				String regno = txtReg.getText();
				String make = txtMake.getText();
				String carModel = txtModel.getText();
				String colour = txtColour.getText();
				String type = txtType.getText();
				String pricePerDay = txtPricePerDay.getText();
				String available = cBAvailable.getSelectedItem().toString();
				
				
					byte[] imageData = null;
					if(imagePath != null) {
						File file = new File(imagePath);
						if(file.exists()) {
							imageData = Files.readAllBytes(Paths.get(imagePath));
						}
					}
					DBManager.addCar(regno, make, carModel, colour, type, pricePerDay, available, imageData);
					
					JOptionPane.showMessageDialog(CarRegistrationPage.this, "Car has been added successfully");
					
					txtMake.setText("");
					txtModel.setText("");
					txtColour.setText("");
					txtType.setText("");
					txtPricePerDay.setText("");
					imagePath = null;
					
					loadCars();
					autoID();
					
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(CarRegistrationPage.this, "Error adding car: " + e1.getMessage());
				}
			}
		});

		JButton btnEdit = new JButton("Edit");
		btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnEdit.addActionListener(e -> {
			int row = tblCars.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "Select a car first!");
				return;
			}

			try {
				byte[] imageData = null;
			    if (imagePath != null) {
			    	imageData = Files.readAllBytes(Paths.get(imagePath));
			    }
			    DBManager.updateCar(
			            txtReg.getText(), 
			            txtMake.getText(), 
			            txtModel.getText(), 
			            txtColour.getText(), 
			            txtType.getText(), 
			            txtPricePerDay.getText(), 
			            cBAvailable.getSelectedItem().toString(), 
			            imageData
			        );
				JOptionPane.showMessageDialog(this, "Car updated successfully");
				imagePath = null;
				loadCars();
				
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
			}
		});

		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnDelete.addActionListener(e -> {
			int row = tblCars.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "Select a car first!");
				return;
			}

			// This is for the confiramtion of deleting
			int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this car?",
					"Confirm Delete", JOptionPane.YES_NO_OPTION);
			if (confirm != JOptionPane.YES_OPTION)
				return;

			String regNo = model.getValueAt(row, 1).toString(); // Car_no

			try{
				DBManager.deleteCar(regNo);

				JOptionPane.showMessageDialog(this, "Car deleted successfully");

			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error deleting car: " + ex.getMessage());
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.addActionListener(e -> {
			// Hides the current window
			this.dispose();
		});

		leftButtons.add(btnAdd);
		leftButtons.add(btnEdit);
		leftButtons.add(btnDelete);
		rightButtons.add(btnCancel);
		bottomPanel.add(leftButtons, BorderLayout.WEST);
		bottomPanel.add(rightButtons, BorderLayout.EAST);

		autoID();
		loadCars();

	}
}
