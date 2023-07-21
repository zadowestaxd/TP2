package simulator.factories;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder{

	public NewCityRoadEventBuilder() {
		super("new_city_road");
	}

	@Override
	Event roadBuilder() {
		return new NewCityRoadEvent(time, id, src, dest, length, co2Limit, maxSpeed, weather2);
	}
}