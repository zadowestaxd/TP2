package simulator.model;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject{
	private List<Road> roadInList;
	private Map<Junction, Road> roadOutList;
	private Map<Road,List<Vehicle>> roadQueue;
	private List<List<Vehicle>> queue;
	private int greenLight, lastLightSwitch, x, y;
	private LightSwitchingStrategy lightChangeStrategy;
	private DequeuingStrategy extractElements;

	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);
		
		if(lsStrategy == null || dqStrategy == null || xCoor < 0 || yCoor < 0)
				throw new IllegalArgumentException("Error: invalid Argument");
		
		x = xCoor;
		y = yCoor;
		greenLight = -1;
		lastLightSwitch = 0;
		lightChangeStrategy = lsStrategy;
		extractElements = dqStrategy;
		roadOutList = new HashMap<>();
		roadQueue = new HashMap<>();
		queue = new ArrayList<>();
		roadInList = new ArrayList<>();
		
	}
	
	@Override
	void advance(int time) {
		//Strategy
		if(greenLight != -1 && !queue.isEmpty()) {
			List<Vehicle> vehicles2 = queue.get(greenLight);
			if(!vehicles2.isEmpty()) {
				List<Vehicle> movedvehicles = extractElements.dequeue(vehicles2);
				for (Vehicle vehicle : movedvehicles) {
					vehicle.moveToNextRoad();
					vehicles2.remove(vehicle);
				}
			}
		}
		
		int index = lightChangeStrategy.chooseNextGreen(roadInList, queue, greenLight, lastLightSwitch, time);
		if(index != greenLight) {
			greenLight = index;
			lastLightSwitch = time;
			}
		}
		//Light

	void addIncommingRoad(Road r) {
		if(r.destinyCross != this)
			throw new IllegalArgumentException("Error: This road is invalid");
		
		roadInList.add(r);
		ArrayList<Vehicle> l = new ArrayList<>();
		queue.add(l);
		roadQueue.put(r, l);
		
	}
	
	void addOutGoingRoad(Road r) {
		
		for (Road roads : roadInList) {
			if(r.originCross != this || (roads != r && roads.originCross == this && roads.destinyCross == r.destinyCross))
				throw new IllegalArgumentException("Error: invalids Roads");
		}
		roadOutList.put(r.destinyCross, r);
	}
	
	void enter(Vehicle v) {
		roadQueue.get(v.getRoad()).add(v);	
	}
	
	Road roadTo(Junction j) {
		return roadOutList.get(j);
	}
	
	@Override
	public JSONObject report() {
		JSONObject reportJSON = new JSONObject();
		JSONArray reportQueueJSON = new JSONArray();
		
		reportJSON.put("id", _id);
		if (greenLight == -1)
			reportJSON.put("green",  "none");
		else
			reportJSON.put("green", roadInList.get(greenLight).getId());
		for (Road road : roadInList) 
			reportQueueJSON.put(reportRoad(road));
		reportJSON.put("queues", reportQueueJSON);
		return reportJSON;	
	}
	
	private JSONObject reportRoad(Road road) {
		JSONObject reportRoadJSON = new JSONObject();
		JSONArray vehicles = new JSONArray();

		reportRoadJSON.put("road", road.getId());
		for (Vehicle vehicle : roadQueue.get(road)) 
			vehicles.put(vehicle.toString());
		reportRoadJSON.put("vehicles", vehicles);
		
		return reportRoadJSON;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public int getGreenLightIndex() {
		return greenLight;
	}

	public List<Road> getInRoads() {
		return roadInList;
	}
}
