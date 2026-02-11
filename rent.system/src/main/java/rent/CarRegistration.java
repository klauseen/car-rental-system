package rent;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

public class CarRegistration extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMake;
	private JTextField txtModel;
	private JTextField txtColour;
	private JTextField txtType;
	private JTextField txtPricePerDay;
	private JTable table_2;
	String imagePath = null;
	
	 private static final String UPDATE_SQL =
		        "UPDATE carregistration SET Make=?, Model=?, Colour=?, Type=?, PricePerDay=?, Available=?, Image=? " +
		        "WHERE Car_no=?";
	 
	 private static final String INSERT_SQL =
			 "INSERT INTO carregistration(Car_no, Make, Model, Colour, Type, PricePerDay, Available, Image) "
		             + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	Connection con; //it's used for sql database, makes the connection
	PreparedStatement pst;
	private JTextField txtReg;
	/**
	 * Launch the application.
	 */
	
	private JComboBox<String> cBAvailable;
	
	private void loadCars() {

	    DefaultTableModel model = (DefaultTableModel) table_2.getModel();
	    model.setRowCount(0); // cleans the table

	    String sql = "SELECT * FROM carregistration";

	    try (
	    	    Connection con = DriverManager.getConnection(
	    	        DBConfig.URL,
	    	        DBConfig.USER,
	    	        DBConfig.PASSWORD
	    	    );
	         PreparedStatement pst = con.prepareStatement(sql);
	         ResultSet rs = pst.executeQuery()) {

	        while (rs.next()) {

	            String reg = rs.getString("Car_no");
	            String make = rs.getString("Make");
	            String modelCar = rs.getString("Model");
	            String colour = rs.getString("Colour");
	            String type = rs.getString("Type");
	            String price = rs.getString("PricePerDay");
	            String available = rs.getString("Available");
	            String imgPath = rs.getString("Image");
	            
	            if (available != null) {
	                available = available.substring(0, 1).toUpperCase() + available.substring(1).toLowerCase();
	            }
	            
	            ImageIcon icon = null;
	            if (imgPath != null && !imgPath.isEmpty()) {
	            	File f = new File(imgPath);
	                if(f.exists()) { // verifies if file exists
	                    icon = new ImageIcon(imgPath);
	                    icon.setDescription(imgPath);
	                } else {
	                    System.out.println("Imaginea nu există: " + imgPath);
	                }
	            }

	            model.addRow(new Object[]{
	                icon, reg, make, modelCar, colour, type, price, available
	            });
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void autoID() {
		String sql = "SELECT MAX(Car_no) AS maxCar FROM carregistration";

	    try (Connection con = DriverManager.getConnection(
	            DBConfig.URL,
	            DBConfig.USER,
	            DBConfig.PASSWORD);
	         PreparedStatement pst = con.prepareStatement(sql);
	         ResultSet rs = pst.executeQuery()) {

	        if (rs.next()) {
	            String maxCar = rs.getString("maxCar"); // will return null if it is empty

	            if (maxCar == null) {
	                
	                txtReg.setText("C001");
	            } else {
	                // Extract numeric part of C
	                long id = Long.parseLong(maxCar.substring(1));
	                id++; 
	                txtReg.setText("C" + String.format("%03d", id)); // 3 digit format
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public CarRegistration() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1279, 707);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		JPanel panel = new JPanel();
		panel.setToolTipText("");
		panel.setForeground(new Color(0, 0, 0));
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Car Registration", TitledBorder.CENTER, TitledBorder.BELOW_TOP, null, SystemColor.desktop));
		panel.setBounds(10, 10, 1245, 623);
		contentPane.add(panel);
		panel.setLayout(null);
		
		
		
		
		JLabel lblRegNo = new JLabel("Registration No");
		lblRegNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRegNo.setBounds(88, 154, 104, 19);
		panel.add(lblRegNo);
		
		JLabel lblMake = new JLabel("Make");
		lblMake.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMake.setBounds(88, 183, 96, 35);
		panel.add(lblMake);
		
		JLabel lblModel = new JLabel("Model");
		lblModel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblModel.setBounds(88, 228, 96, 35);
		panel.add(lblModel);
		
		JLabel lblColour = new JLabel("Colour");
		lblColour.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblColour.setBounds(88, 273, 96, 35);
		panel.add(lblColour);
		
		JLabel lblType = new JLabel("Type");
		lblType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblType.setBounds(88, 318, 96, 35);
		panel.add(lblType);
		
		JLabel lblPrice = new JLabel("Price / day");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPrice.setBounds(88, 363, 96, 35);
		panel.add(lblPrice);
		
		JLabel lblAvailable = new JLabel("Available");
		lblAvailable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAvailable.setBounds(88, 408, 96, 16);
		panel.add(lblAvailable);
		
		txtReg = new JTextField();
		txtReg.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtReg.setBounds(236, 155, 96, 19);
		panel.add(txtReg);
		txtReg.setColumns(10);
		
		txtMake = new JTextField();
		txtMake.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtMake.setBounds(236, 191, 96, 19);
		panel.add(txtMake);
		txtMake.setColumns(10);
		
		txtModel = new JTextField();
		txtModel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtModel.setBounds(236, 236, 96, 19);
		panel.add(txtModel);
		txtModel.setColumns(10);
		
		txtColour = new JTextField();
		txtColour.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtColour.setBounds(236, 281, 96, 19);
		panel.add(txtColour);
		txtColour.setColumns(10);
		
		txtType = new JTextField();
		txtType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtType.setBounds(236, 326, 96, 19);
		panel.add(txtType);
		txtType.setColumns(10);
		
		txtPricePerDay = new JTextField();
		txtPricePerDay.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtPricePerDay.setBounds(236, 371, 96, 19);
		panel.add(txtPricePerDay);
		txtPricePerDay.setColumns(10);
		

		JButton btnUpload = new JButton("Upload Image");
		btnUpload.setBounds(88, 440, 150, 30);
		panel.add(btnUpload);

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
		            Files.copy(
		                selectedFile.toPath(),
		                Paths.get(imagePath),
		                StandardCopyOption.REPLACE_EXISTING
		            );
		            
		            ImageIcon icon = new ImageIcon(imagePath);
		            icon.setDescription(imagePath);
		            
		            
		            System.out.println("Imaginea a fost încărcată: " + imagePath);
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
		    }
		});
		
		 table_2 = new JTable();
		 DefaultTableModel model = new DefaultTableModel(
		       new Object[]{"Photo", "Reg No", "Make", "Model", "Colour", "Type", "Price / day", "Available"}, 0
		    ) {
		        @Override
		        public Class<?> getColumnClass(int column) {
		            if (column == 0) return ImageIcon.class;
		            return String.class;
		        }

		        @Override
		        public boolean isCellEditable(int row, int column) {
		            return false;
		        }
		    };
		    table_2.setModel(model);
		    table_2.setRowHeight(80);
		    table_2.getColumnModel().getColumn(0).setPreferredWidth(80);
		    table_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		    
		    table_2.addMouseListener(new java.awt.event.MouseAdapter() {
		        @Override
		        public void mouseClicked(java.awt.event.MouseEvent evt) {

		            int row = table_2.getSelectedRow();
		            if (row == -1) return;

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
		    
		    
		    table_2.addMouseListener(new java.awt.event.MouseAdapter() {
		        public void mouseClicked(java.awt.event.MouseEvent evt) {
		            int row = table_2.rowAtPoint(evt.getPoint());
		            if (row == -1) return;
		            int col = table_2.columnAtPoint(evt.getPoint());
		            if (col == 0) { // click on picture
		                ImageIcon icon = (ImageIcon) table_2.getValueAt(row, col);
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

		    JScrollPane scrollPane = new JScrollPane(table_2);
		    scrollPane.setBounds(395, 99, 742, 472);
		    panel.add(scrollPane);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String regno = txtReg.getText();
				String make = txtMake.getText();
				String carModel = txtModel.getText();
				String colour = txtColour.getText();
				String type = txtType.getText();
				String pricePerDay = txtPricePerDay.getText();
				String available = cBAvailable.getSelectedItem().toString();
				
				
				String insertSQL = "INSERT INTO carregistration(Car_no, Make, Model, Colour, Type, PricePerDay, Available, Image) "
		                 + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				
				

				
				Connection con;
				try {
					con = DriverManager.getConnection(
						    DBConfig.URL,
						    DBConfig.USER,
						    DBConfig.PASSWORD
						);
					pst = con.prepareStatement(insertSQL);
					
					pst.setString(1, regno);
				    pst.setString(2, make);
				    pst.setString(3, carModel);
				    pst.setString(4, colour);
				    pst.setString(5, type);
				    pst.setString(6, pricePerDay);
				    pst.setString(7, available);
				    pst.setString(8, imagePath);
				    
				    pst.executeUpdate();
				    JOptionPane.showMessageDialog(CarRegistration.this, "Car has been added successfully");

				    
				    
				    ImageIcon icon = new ImageIcon(imagePath);
			        icon.setDescription(imagePath); 
			        DefaultTableModel model = (DefaultTableModel) table_2.getModel();
			        model.addRow(new Object[]{icon, regno, make, carModel, colour, type, pricePerDay, available});

				    
				} catch (SQLException e1) {
					e1.printStackTrace();
				}	         
			}
		});
		
		
		btnAdd.setBounds(88, 478, 104, 35);
		panel.add(btnAdd);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		btnEdit.addActionListener(e -> {
		    int row = table_2.getSelectedRow();
		    if (row == -1) {
		        JOptionPane.showMessageDialog(this, "Select a car first!");
		        return;
		    }

		    try (Connection con = DriverManager.getConnection(
		    	    DBConfig.URL,
		    	    DBConfig.USER,
		    	    DBConfig.PASSWORD
		    	);
		        PreparedStatement pst = con.prepareStatement(UPDATE_SQL)) {

		        pst.setString(1, txtMake.getText());
		        pst.setString(2, txtModel.getText());
		        pst.setString(3, txtColour.getText());
		        pst.setString(4, txtType.getText());
		        pst.setString(5, txtPricePerDay.getText());
		        pst.setString(6, cBAvailable.getSelectedItem().toString());
		        pst.setString(7, imagePath);
		        pst.setString(8, txtReg.getText()); 

		        pst.executeUpdate();
		        
		        ImageIcon icon = new ImageIcon(imagePath);
		        icon.setDescription(imagePath);
		        model.setValueAt(icon, row, 0);

		        
		        model.setValueAt(txtMake.getText(), row, 2);
		        model.setValueAt(txtModel.getText(), row, 3);
		        model.setValueAt(txtColour.getText(), row, 4);
		        model.setValueAt(txtType.getText(), row, 5);
		        model.setValueAt(txtPricePerDay.getText(), row, 6);
		        model.setValueAt(cBAvailable.getSelectedItem(), row, 7);
		        
		        

		        JOptionPane.showMessageDialog(this, "Car updated successfully");


		    } catch (Exception ex) {
		        JOptionPane.showMessageDialog(this, ex.getMessage());
		    }
		});
		btnEdit.setBounds(236, 478, 96, 35);
		panel.add(btnEdit);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		btnDelete.addActionListener(e -> {
		    int row = table_2.getSelectedRow();
		    if (row == -1) {
		        JOptionPane.showMessageDialog(this, "Select a car first!");
		        return;
		    }

		    // This is for the confiramtion of deleting
		    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this car?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
		    if (confirm != JOptionPane.YES_OPTION) return;

		    String regNo = model.getValueAt(row, 1).toString(); // Car_no

		    try (Connection con = DriverManager.getConnection(
		    	    DBConfig.URL,
		    	    DBConfig.USER,
		    	    DBConfig.PASSWORD
		    	);
		         PreparedStatement pst = con.prepareStatement("DELETE FROM carregistration WHERE Car_no=?")) {

		        pst.setString(1, regNo);
		        pst.executeUpdate();

		        // Delete it from Jtable JTable
		        ImageIcon icon = (ImageIcon) model.getValueAt(row, 0); 
		        model.removeRow(row);
		        
		        if(icon != null) {
		            String path = icon.getDescription();
		            File f = new File(path);
		            if(f.exists()) {
		                f.delete(); // Delete file image
		            }
		        }

		        JOptionPane.showMessageDialog(this, "Car deleted successfully");

		    } catch (SQLException ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(this, "Error deleting car: " + ex.getMessage());
		    }
		});
		btnDelete.setBounds(88, 536, 104, 35);
		panel.add(btnDelete);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.addActionListener(e -> {
		    // Hides the current window
		    this.dispose();
		});
		btnCancel.setBounds(236, 539, 96, 32);
		panel.add(btnCancel);
		
		cBAvailable = new JComboBox<>();
		cBAvailable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cBAvailable.setModel(new DefaultComboBoxModel(new String[] {"Yes", "No"}));
		cBAvailable.setBounds(236, 407, 96, 21);
		panel.add(cBAvailable);
		panel.setFocusTraversalPolicy(new ContainerOrderFocusTraversalPolicy());
		
		
		
		autoID();
		loadCars();
		
	}
}
