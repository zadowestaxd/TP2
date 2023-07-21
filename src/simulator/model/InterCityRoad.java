package simulator.model;

public class InterCityRoad extends Road{

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int length, int contLimit, int maxSpeed,
			Weather weather) {
		super(id, srcJunc, destJunc, length, contLimit, maxSpeed, weather);
	}

	@Override
	void reduceTotalContamination() {
		int weather = 0;
		switch (enviCondition) {
		case SUNNY:
			weather = 2;
			break;
		case CLOUDY:
			weather = 3;
			break;
		case RAINY:
			weather = 10;
			break;
		case WINDY:
			weather = 15;
			break;
		case STORM:
			weather = 20;
			break;
		default:
			break;
		}
		totalPollution = (totalPollution * ( 100 - weather))/100;
	}

	@Override
	void updateSpeedLimit() {
		if(totalPollution > pollutionAlert)
			speedLimit = maxVelocity/2;
		else 
		speedLimit = maxVelocity;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if(enviCondition == Weather.STORM)
		v.setSpeed((speedLimit*8)/10);
			else
		v.setSpeed(speedLimit);
		return 0;
	}

}
