package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;



public class RoadMap {
	List<Junction> crossRoad;
	List<Road> roads;
	List<Vehicle> vehicles;
	Map<String,Junction> crossMap;
	Map<String,Road> roadMap;
	Map<String,Vehicle> vehiclesMap;
	
	RoadMap(){
		reset();
	}
	void addJunction(Junction j) {
		if(crossMap.containsKey(j.getId()))
			throw new IllegalArgumentException("Error: duplicated juntion");
		
		crossRoad.add(j);
		crossMap.put(j.getId(), j);
	}
	
	void addRoad(Road r){
		if(roadMap.containsKey(r.getId()))
			throw new IllegalArgumentException("Error: duplicated road");
		else if (roadMap.containsKey(r.getDest().getId()) && roadMap.containsKey(r.getSrc().getId()))
			throw new IllegalArgumentException("Error: junctions doesn't exist");
		
		roads.add(r);
		roadMap.put(r.getId(), r);
	}
	
	void addVehicle(Vehicle v) {
		if(vehiclesMap.containsKey(v.getId()))
			throw new IllegalArgumentException("Error: duplicated vehicle");
		else if (!itineraryIsValid(v.getItinerary()))
			throw new IllegalArgumentException("Error: itinerary not valid");
		
		vehicles.add(v);
		vehiclesMap.put(v.getId(), v);
	}
	
	private boolean itineraryIsValid(List<Junction> itinerary) {
        for (Junction j : itinerary)
            if(!crossMap.containsValue(j))
                return false;

        for (Road r : roads) {
            if(r.originCross == itinerary.get(0) && r.destinyCross == itinerary.get(1))
                    return true;
        }
        return false;
    }
	
	public Junction getJunction(String id) {
		if(crossMap.containsKey(id))
			return crossMap.get(id);
		return null;
	}
	
	public Road getRoad(String id) {
		if(roadMap.containsKey(id))
			return roadMap.get(id);
		return null;
	}
	
	public Vehicle getVehicle(String id) {
		if(vehiclesMap.containsKey(id))
			return vehiclesMap.get(id);
		return null;
	}
	
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(crossRoad);
	}
	
	public List<Road> getRoads(){
		return Collections.unmodifiableList(roads);
	}
	
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(vehicles);
	}
	
	void reset() {
		crossRoad = new ArrayList<>();
		roads = new ArrayList<>();
		vehicles = new ArrayList<>();
		crossMap = new HashMap<>();
		roadMap = new HashMap<>();
		vehiclesMap = new HashMap<>();
		
	}
	
	public JSONObject report() {
		JSONObject reportJSON = new JSONObject();
		
		JSONArray arr1 = new JSONArray();
		for (Junction j : crossRoad)
			arr1.put(j.report());
		reportJSON.put("junctions", arr1);
		
		JSONArray arr2 = new JSONArray();
		for (Road j : roads)
			arr2.put(j.report());
		reportJSON.put("roads", arr2);
		
		JSONArray arr3 = new JSONArray();
		for (Vehicle j : vehicles)
			arr3.put(j.report());
		reportJSON.put("vehicles", arr3);

		return reportJSON;
	}
	public List<Vehicle> getVehilces() {
		return vehicles;
	}
	
}