package simulator.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import org.json.JSONObject;


public class Vehicle extends SimulatedObject{
	private List<Junction> itinerary;
	private int maxSpeed, currentSpeed, location, pollution, totalPollution, totalDistance, index = 0;
	private VehicleStatus status;
	private Road road;

	Vehicle(String id, int maxSpeed, int pollution, List<Junction> itinerary) {
		super(id); 
		if (maxSpeed <= 0 || pollution < 0 || pollution > 10 || itinerary.size() < 2)
			throw new IllegalArgumentException("Error: invalid arguments");
		
		this.maxSpeed = maxSpeed;
		this.pollution = pollution;
		this.currentSpeed = 0;
		this.location = 0;
		this.totalPollution = 0;
		this.totalDistance = 0;
		this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		this.status = VehicleStatus.PENDING;
		
	}

	@Override
	void advance(int time) {
		if(status.equals(VehicleStatus.TRAVELING)) {
			int previousLocation = location;
			
			location = Math.min(location + currentSpeed, road.getLength());
			totalDistance += (location - previousLocation);
			
			totalPollution += ((location - previousLocation) * pollution);
			road.addContamination((location - previousLocation) * pollution);
			
			if(location >= road.getLength()) {
				status = VehicleStatus.WAITING;
				currentSpeed = 0;
				road.getDest().enter(this);
			}
		}
	}
	
	@Override
	public JSONObject report() {
		JSONObject reportJSON = new JSONObject();
		
		reportJSON.put("id", _id);
		reportJSON.put("speed", currentSpeed);
		reportJSON.put("distance", totalDistance);
		reportJSON.put("co2", totalPollution);
		reportJSON.put("class", pollution);
		reportJSON.put("status", status.toString());
		
		if (status != VehicleStatus.PENDING && status != VehicleStatus.ARRIVED) {
			reportJSON.put("road", road.getId());
			reportJSON.put("location", location);
		}
		return reportJSON;
	}
	
	void setSpeed(int s) {
		if(s < 0) 
			throw new IllegalArgumentException("Error: invalid argument");
		if (status == VehicleStatus.TRAVELING)
			currentSpeed = Math.min(maxSpeed, s);
	}
	
	void setContClass(int c) {
		if(c < 0 || c > 10)
			throw new IllegalArgumentException("Error: invalid argument");
		pollution = c;
	}
	
	void moveToNextRoad() {
		currentSpeed = 0;
		location = 0;
		
		if (status != VehicleStatus.PENDING && status != VehicleStatus.WAITING)
			throw new IllegalArgumentException("Error: invalid Vehicle status");
		
		if(status == VehicleStatus.PENDING) { //no ha empezado a conducir
			road = itinerary.get(index).roadTo(itinerary.get(index + 1)); //TODO mirar si podemos poner index y si hay que poner un index + 1 en el segundo get()
			status = VehicleStatus.TRAVELING;
			index++;
			road.enter(this);
		}else {
			if (index + 1 == itinerary.size()) { //ha acabado el itinerario
				status = VehicleStatus.ARRIVED;
				road.exit(this);
			} else { //está conduciendo
				road.exit(this);
				road = road.getDest().roadTo(itinerary.get(index + 1)); //mirar a ver si tengo que poner esto: road = road.getDest().roadTo(road.getDest());
				status = VehicleStatus.TRAVELING;
				index++;
				road.enter(this);
			}
		}
	}
	
	private void setStatus() {
		if (status != VehicleStatus.TRAVELING)
			currentSpeed = 0;
	}
	
	public int getLocation() {
		return location;
	}

	public int getSpeed() {
		return currentSpeed;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public int getContClass() {
		return pollution;
	}
	
	public VehicleStatus getStatus() {
		return status;
	}
	
	public int getTotalCO2() {
		return totalPollution;
	}
	
	public int getTotalDistance() { return totalDistance; }
	
	public List<Junction> getItinerary() {
		return itinerary;
	}
	
	public Road getRoad() {
		return road;
	}
	
	public static class VehicleComparator implements Comparator<Vehicle> {
		@Override
		public int compare(Vehicle o1, Vehicle o2) {
			return o2.getLocation() - o1.getLocation();
		}
	}
}