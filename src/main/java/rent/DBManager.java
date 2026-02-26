package rent;

import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DBManager {
	
	public static void initDatabase() {

		String createCarRegistrationTable = """
					CREATE TABLE IF NOT EXISTS carregistration(
						`idcarregistration` int NOT NULL AUTO_INCREMENT,
				  		`car_number` varchar(45) NOT NULL UNIQUE,
				  		`Image` longblob NOT NULL,
				  		`Make` varchar(45) NOT NULL,
				  		`Model` varchar(45) NOT NULL,
				  		`Colour` varchar(45) NOT NULL,
				  		`Type` varchar(45) NOT NULL,
				  		`PricePerDay` varchar(45) NOT NULL,
				  		`Available` varchar(45) NOT NULL DEFAULT 'Yes',
				  		PRIMARY KEY (`idcarregistration`)
					)
					""";
		
		String createRentalTable = """
				CREATE TABLE IF NOT EXISTS rental(
					`idrental` int NOT NULL AUTO_INCREMENT,
					`car_number` varchar(45) NOT NULL,
					`Date_from` date NOT NULL,
					`Date_to` date NOT NULL,
					`Days` int NOT NULL,
					`Total_fee` double NOT NULL,
					PRIMARY KEY (`idrental`)
				) 
				""";
		
		String createUsersTable = """
					CREATE TABLE IF NOT EXISTS users(
						`idlogin` int NOT NULL AUTO_INCREMENT,
						`username` varchar(100) NOT NULL,
						`password` varchar(255) NOT NULL,
						`role` varchar(45) NOT NULL,
						PRIMARY KEY (`idlogin`),
						UNIQUE KEY `username_UNIQUE` (`username`)
					)
					""";
		
		try(Connection con = DBConfig.getConnection();
				Statement stmt = con.createStatement()){
			stmt.executeUpdate(createCarRegistrationTable);
			stmt.executeUpdate(createRentalTable);
			stmt.executeUpdate(createUsersTable);
			
			System.out.println("Table system was verified or created successfully");
		}catch(Exception e) {
			System.err.println("Error to initialize database: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static List<RentalReport> getRents()throws Exception{
		List <RentalReport> rentList = new ArrayList<>();
		Set<String> seen = new HashSet<>();
		
		String sql = """
				SELECT r.car_number, c.make, c.model,
				       CONCAT(r.date_from, ' -> ', r.date_to) AS period,
				       r.total_fee
				FROM rental r
				JOIN carregistration c ON r.car_number = c.car_number
				""";

		try(Connection con = DBConfig.getConnection();
			PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()){
			while(rs.next()) {
				String carNo = rs.getString("car_number");
				String period = rs.getString("period");
				double fee = rs.getDouble("total_fee");
				
				String key = carNo + "|" + period + "|" + fee;
				
				if(!seen.contains(key)) {
					rentList.add(new RentalReport(carNo, rs.getString("make") , rs.getString("model") , period , fee));
					seen.add(key);
				}
			}
		}
		return rentList;
	}
	
	public static byte[] getCarImage(String regNo) throws Exception{
		String sql = "SELECT Image FROM carregistration WHERE car_number = ?";
		try(Connection con = DBConfig.getConnection();
			PreparedStatement pst = con.prepareStatement(sql)){
			pst.setString(1, regNo);
			try(ResultSet rs = pst.executeQuery()){
				if(rs.next()) {
					return rs.getBytes("Image");
				}
			}
		}
		return null;
	}

	public static void processRental(String regNo, java.util.Date dateFrom, java.util.Date dateTo, int days,
			double totalFee) throws Exception {
		String insertSql = "INSERT INTO rental (car_number, date_from, date_to, days, total_fee) VALUES (?, ?, ?, ?, ?)";
		String updateSql = "UPDATE carregistration SET available = 'no' WHERE car_number = ?";

		try (Connection con = DBConfig.getConnection()) {
			try (PreparedStatement pstInsert = con.prepareStatement(insertSql)) {
				pstInsert.setString(1, regNo);
				pstInsert.setDate(2, new java.sql.Date(dateFrom.getTime()));
				pstInsert.setDate(3, new java.sql.Date(dateTo.getTime()));
				pstInsert.setInt(4, days);
				pstInsert.setDouble(5, totalFee);
				pstInsert.executeUpdate();
			}

			try (PreparedStatement pstUpdate = con.prepareStatement(updateSql)) {
				pstUpdate.setString(1, regNo);
				pstUpdate.executeUpdate();
			}
		}
	}

	public static List<Cars> getAllCars() throws Exception {
		List<Cars> carList = new ArrayList();
		String sql = "SELECT * FROM carregistration";

		try (Connection con = DBConfig.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery();) {
			while (rs.next()) {
				carList.add(new Cars(rs.getString("car_number"), rs.getString("Make"), rs.getString("Model"),
						rs.getString("Colour"), rs.getString("Type"), rs.getString("PricePerDay"),
						rs.getString("Available"), rs.getBytes("Image")));
			}
		}
		return carList;
	}

	public static String IDAuto() throws Exception {

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

	public static void addCar(String reg, String make, String model, String colour, String type, String price,
			String available, byte[] image) throws Exception {

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

	public static void deleteCar(String regNo) throws Exception {
		String sql = "DELETE FROM carregistration WHERE car_number=?";
		try (Connection con = DBConfig.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, regNo);
			pst.executeUpdate();
		}
	}

	public static void updateCar(String reg, String make, String model, String colour, String type, String price,
			String available, byte[] image) throws Exception {
		String sql = (image != null)
				? "UPDATE carregistration SET Make=?, Model=?, Colour=?, Type=?, PricePerDay=?, Available=?, Image=? WHERE car_number=?"
				: "UPDATE carregistration SET Make=?, Model=?, Colour=?, Type=?, PricePerDay=?, Available=? WHERE car_number=?";

		try (Connection con = DBConfig.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, make);
			pst.setString(2, model);
			pst.setString(3, colour);
			pst.setString(4, type);
			pst.setString(5, price);
			pst.setString(6, available);
			if (image != null) {
				pst.setBytes(7, image);
				pst.setString(8, reg);
			} else {
				pst.setString(7, reg);
			}
			pst.executeUpdate();
		}
	}

	public static void registerUser(String username, String hashedPassword) throws Exception {
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
