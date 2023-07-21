package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {
	
	private List<String> itinerary;
	private int maxSpeed, contClass;
	private String id;

	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
			super(time);
			if(itinerary.isEmpty())
				throw new IllegalArgumentException("Error: itinerary is empty");
			this.itinerary = itinerary;
			this.maxSpeed = maxSpeed;
			this.contClass = contClass;
			this.id = id;
	}

	@Override
	void execute(RoadMap map) {
		List<Junction> junctions = new ArrayList<>();
		
		for (String itinerario : itinerary)
			junctions.add(map.getJunction(itinerario));
		
		Vehicle newVehicle = new Vehicle(id, maxSpeed, contClass, junctions);
		newVehicle.moveToNextRoad();
		map.addVehicle(newVehicle);
		
	}
	
	@Override
	public String toString() {
	return "New Vehicle '"+id+"'";
	}

}