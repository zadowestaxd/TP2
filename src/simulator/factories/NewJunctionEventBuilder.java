package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {

	Factory<LightSwitchingStrategy> lsFactory;
    Factory<DequeuingStrategy> dqFactory;

	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lsFactory, Factory<DequeuingStrategy> dqFactory) {
		super("new_junction");
		this.lsFactory = lsFactory;
		this.dqFactory = dqFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		JSONArray coor = data.getJSONArray("coor");
		LightSwitchingStrategy lsstrat = lsFactory.createInstance(data.getJSONObject("ls_strategy"));
		DequeuingStrategy dqstrat = dqFactory.createInstance(data.getJSONObject("dq_strategy"));
		return new NewJunctionEvent(time, id, lsstrat, dqstrat, coor.getInt(0), coor.getInt(1));
	}

}
