package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;


public class TrafficSimulator implements Observable <TrafficSimObserver>{
	private RoadMap map;
	private List<Event> eventList;
	private int time;
	private List<TrafficSimObserver> observer;
	public TrafficSimulator() {
		map = new RoadMap();
		eventList = new SortedArrayList<Event>();
		time = 0;
		observer = new ArrayList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		this.eventList.add(e);
		for (TrafficSimObserver s : observer) {
			s.onEventAdded(map, eventList, e, time);	
		}
	}
	
	public void advance() {
		try {
			time++;
			for (TrafficSimObserver s : observer) {
				s.onAdvanceStart(map, eventList, time);	
			}
			List<Event> aux = new ArrayList<>();
			
			for (Event e : eventList) {
				if (time == e.getTime()) {
					aux.add(e);
					e.execute(map);
				}
			}
			eventList.removeAll(aux);
	
			for (Junction junction : map.getJunctions())
				junction.advance(time);
			for (Road road : map.getRoads())
				road.advance(time);
			
			for (TrafficSimObserver s : observer) {
				s.onAdvanceEnd(map, eventList, time);	
			}
		}catch(Exception e) {
			for (TrafficSimObserver s : observer) {
				s.onError(e.getMessage());
				throw e;
			}	
		}
	}
	
	public void reset() {
		map.reset();
		eventList.clear();
		time = 0;
		for (TrafficSimObserver s : observer) {
			s.onReset(map, eventList, time);	
		}
	}
	
	public JSONObject report() {
		JSONObject reportJSON = new JSONObject();
		
		reportJSON.put("time", time);
		reportJSON.put("state", map.report());

		return reportJSON;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		
		if (!observer.contains(o))
		{
			observer.add(o);
		}
		
		for (TrafficSimObserver s : observer) {
			s.onRegister(map, eventList, time);	
		}
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {		
	}
}