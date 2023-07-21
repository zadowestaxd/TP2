package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	
	private static List<Pair<String,Weather>> ws;

	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		
		if(ws.isEmpty())
			throw new IllegalArgumentException("Error: ws is empty");
		this.ws = ws;
		}
	
	@Override
	void execute(RoadMap map) {
		for (Pair<String, Weather> w : ws) {
			if (map.getRoad(w.getFirst()) == null)
				throw new IllegalArgumentException("Road doesn't exist in the RoadMap");
			map.getRoad(w.getFirst()).setWeather(w.getSecond());
		}
	}
	
	public String toString() {
		return ws.toString();
	}
}