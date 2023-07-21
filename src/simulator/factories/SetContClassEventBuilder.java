package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event>{

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		JSONArray a = data.getJSONArray("info");
		List<Pair<String, Integer>> in = new ArrayList<>();
		String temp;
		int temp2;
		for(int i = 0; i < a.length(); i++) {
			 temp = a.getJSONObject(i).getString("vehicle");
			temp2 = Integer.valueOf(a.getJSONObject(i).getInt("class"));
			in.add(new Pair<String, Integer> (temp, temp2));
		}

		return new SetContClassEvent(time, in);
	}

}
