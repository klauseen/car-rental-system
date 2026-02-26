package rent;

public class Cars {
	private String regNo;
	private String make;
	private String model;
	private String colour;
	private String type;
	private String pricePerDay;
	private String available;
	private byte[] image;
	
	public Cars(String regNo, String make, String model, String colour , String type, String pricePerDay, String available, byte[] image) {
		this.regNo = regNo;
		this.make = make;
		this.model = model;
		this.colour = colour;
		this.type = type;
		this.pricePerDay = pricePerDay;
		this.available = available;
		this.image = image;
	}
	
	public String getRegNo() {
		return regNo;
	}
	public String getMake() {
		return make;
	}
	public String getModel() {
		return model;
	}
	public String getColour() {
		return colour;
	}
	public String getType() {
		return type;
	}
	public String getPricePerDay() {
		return pricePerDay;
	}
	public String getAvailable() {
		return available;
	}
	public byte[] getImage() {
		return image;
	}
}
