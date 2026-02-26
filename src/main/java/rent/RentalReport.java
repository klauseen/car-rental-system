package rent;

public class RentalReport {
	private String carNo;
	private String make;
	private String model;
	private String period;
	private double totalFee;
	
	public RentalReport(String carNo , String make, String model, String period, double totalFee) {
		this.carNo = carNo;
		this.make = make;
		this.model = model;
		this.period = period;
		this.totalFee = totalFee;
	}
	
	public String getCarNo() {
		return carNo;
	}
	public String getMake() {
		return make;
	}
	public String getModel() {
		return model;
	}
	public String getPeriod() {
		return period;
	}
	public double getTotalFee() {
		return totalFee;
	}
}
