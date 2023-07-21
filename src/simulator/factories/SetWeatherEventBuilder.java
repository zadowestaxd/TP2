package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		JSONArray a = data.getJSONArray("info");
		List<Pair<String, Weather>> in = new ArrayList<>();
		String temp;
		Weather temp2;
		for(int i = 0; i < a.length(); i++) {
			 temp = a.getJSONObject(i).getString("road");
			temp2 = Weather.valueOf(a.getJSONObject(i).getString("weather").toUpperCase());
			in.add(new Pair<String, Weather> (temp, temp2));
		}

		return new SetWeatherEvent(time, in);
	}

}
