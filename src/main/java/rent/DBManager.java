package rent;

import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DBManager {
	
	public static List<Cars> getAllCars()throws Exception{
		List<Cars> carList = new ArrayList();
		String sql = "SELECT * FROM carregistration";
		
		try(Connection con = DBConfig.getConnection();PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery();){
			while(rs.next()) {
				carList.add(new Cars(
						rs.getString("car_number"),
						rs.getString("Make"),
						rs.getString("Model"),
						rs.getString("Colour"),
						rs.getString("Type"),
						rs.getString("PricePerDay"),
						rs.getString("Available"),
						rs.getBytes("Image")
						));
			}
		}
		return carList;
	}
	
	public static String IDAuto()throws Exception {
		
		String sql = "SELECT MAX(car_number) AS maxCar FROM carregistration";

		try (Connection con = DBConfig.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {
			if (rs.next()) {
				String maxCar = rs.getString("maxCar"); // will return null if it is empty

				if (maxCar == null) {
					return "C001";
				} else {
					// Extract numeric part of C
					long id = Long.parseLong(maxCar.substring(1));
					id++;
					return "C" + String.format("%03d", id);
				}
			}
		}
		
		return "C001";
	}
	
	public static void addCar(String reg , String make, String model , String colour , String type , String price , String available , byte[] image )throws Exception {
		
		String sql = "INSERT INTO carregistration(car_number, Make, Model, Colour, Type, PricePerDay, Available, Image) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		

		try (Connection con = DBConfig.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, reg);
			pst.setString(2, make);
			pst.setString(3, model);
			pst.setString(4, colour);
			pst.setString(5, type);
			pst.setString(6, price);
			pst.setString(7, available);
			pst.setBytes(8, image);
			pst.executeUpdate();
		}
	}
	
	public static void deleteCar(String regNo)throws Exception {
		String sql = "DELETE FROM carregistration WHERE car_number=?";
		try (Connection con = DBConfig.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, regNo);
			pst.executeUpdate();
		}
	}
	
	public static void updateCar(String reg , String make , String model, String colour , String type , String price , String available , byte[] image)throws Exception{
		String sql = (image != null) ? "UPDATE carregistration SET Make=?, Model=?, Colour=?, Type=?, PricePerDay=?, Available=?, Image=? WHERE car_number=?"
									 : "UPDATE carregistration SET Make=?, Model=?, Colour=?, Type=?, PricePerDay=?, Available=? WHERE car_number=?";
		
		try(Connection con = DBConfig.getConnection(); PreparedStatement pst = con.prepareStatement(sql)){
			pst.setString(1, make);
			pst.setString(2, model);
			pst.setString(3, colour);
			pst.setString(4, type);
			pst.setString(5, price);
			pst.setString(6, available);
			if(image != null) {
				pst.setBytes(7, image);
				pst.setString(8,reg);
			}else {
				pst.setString(7, reg);
			}
			pst.executeUpdate();
		}
	}
	
	public static void registerUser(String username , String hashedPassword)throws Exception {
		String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'customer')";
		try (Connection con = DBConfig.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setString(1, username);
			pst.setString(2, hashedPassword);
			pst.executeUpdate();
		}
	}
	
	public static String loginUser(String username, String password) throws Exception {
		String sql = "SELECT password, role FROM users WHERE username = ?";
		try (Connection conn = DBConfig.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, username);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {

				String storedHash = rs.getString("password");
				String role = rs.getString("role");

				if (org.mindrot.jbcrypt.BCrypt.checkpw(password, storedHash)) {
					return role;
				}
			}
		}
		return null;
	}
}
