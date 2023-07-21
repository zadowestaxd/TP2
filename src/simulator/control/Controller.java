package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;

import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator trafficSimulator;
	private Factory<Event> eventsFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
	if(sim == null || eventsFactory == null)
		throw new IllegalArgumentException("ERROR; Invalid Arguments");
	trafficSimulator = sim;
	this.eventsFactory = eventsFactory;
	}
	
	public void loadEvents(InputStream in){
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray e = jo.getJSONArray("events");
		for (int i = 0; i < e.length(); i++) {
			trafficSimulator.addEvent(eventsFactory.createInstance(e.getJSONObject(i)));
		}
	}
	
	public void run(int n) {
		for (int i = 0; i < n; i++)
			trafficSimulator.advance();
	}
	
	public void run(int n, OutputStream out) {
		//for con los tick y llamar al report de advance
		JSONArray states = new JSONArray();
		PrintStream print = new PrintStream(out);
		JSONObject aux = new JSONObject();
		
		for(int i = 0; i < n; i++) {
			if(i == 125) 
				trafficSimulator.advance();
			else 
				trafficSimulator.advance();

			states.put(trafficSimulator.report());	
		}
		aux.put("states",states);
		print.println(aux.toString(3));
	}
	
	public void reset() {
		trafficSimulator.reset();
	}
	
	public void addObserver(TrafficSimObserver o){
		trafficSimulator.addObserver(o);
	}
	
	private void removeObserver(TrafficSimObserver o) {
		trafficSimulator.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		trafficSimulator.addEvent(e);
	}
}