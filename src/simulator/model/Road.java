package simulator.model;
import java.util.ArrayList;
import java.util.Collections;
import simulator.model.Vehicle.VehicleComparator;
import java.util.List;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject{
	Junction originCross, destinyCross;
	int length, maxVelocity, currentVelocity, pollutionAlert, totalPollution, speedLimit;
	Weather enviCondition;
	List<Vehicle> vehicles;

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);
		if(maxSpeed <= 0 || contLimit < 0 || length <= 0 || srcJunc == null || destJunc == null || weather == null)
			throw new IllegalArgumentException("Argument(s) invalids");
		destinyCross = destJunc;
		vehicles = new ArrayList<>();
		originCross = srcJunc;
		maxVelocity = maxSpeed;
		speedLimit = maxSpeed;
		pollutionAlert = contLimit; 
		this.length = length;
		enviCondition = weather;
		originCross.addOutGoingRoad(this);
		destinyCross.addIncommingRoad(this);
		
	}

	@Override	
	public void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		for (Vehicle vehicle : vehicles) {
			calculateVehicleSpeed(vehicle);
			vehicle.advance(time);
		}
		Collections.sort(vehicles, new VehicleComparator());
	}

	@Override
	public JSONObject report() {
		JSONObject reportJSON = new JSONObject();
		List<String> test = new ArrayList<>();
		reportJSON.put("id", _id);
		reportJSON.put("speedlimit", speedLimit);
		reportJSON.put("weather", enviCondition);
		reportJSON.put("co2", totalPollution);
		for (Vehicle vehicle : vehicles) {
			test.add(vehicle.getId());
		}
		reportJSON.put("vehicles", test);

		return reportJSON;
	}
	
	public void enter(Vehicle v) {
		if(v.getLocation() != 0 || v.getSpeed() != 0)
			throw new IllegalArgumentException("Error: invalid Vehicle status");
		vehicles.add(v);
	}
	
	public void exit(Vehicle v) {
		
		vehicles.remove(v);
	}
	
	 public void setWeather(Weather w) {
		if(w == null)
			throw new IllegalArgumentException("Error: invalid Vehicle status");
		enviCondition = w;	
	}
	
	public void addContamination(int c) {
		if(c < 0)
		throw new IllegalArgumentException("Error: invalid Vehicle status");
		totalPollution += c;
	}
	
	abstract void reduceTotalContamination();
	
	abstract  void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);
	
	public int getLength() {
		return length;
	}
	
	public Junction getDest() {
		return destinyCross;
	}
	
	public Junction getSrc() {
		return originCross;
	}
	
	public Weather getWeather() {
		return enviCondition;
	}
	
	public int getContLimit() {
		return pollutionAlert;
	}
	
	public int getMaxSpeed() {
		return maxVelocity;
	}
	
	public int getTotalCO2() {
		return totalPollution;
	}
	
	public int getSpeedLimit() {
		return speedLimit;
	}
	
	public List <Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}

}
