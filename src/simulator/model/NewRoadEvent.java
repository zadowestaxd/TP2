package simulator.model;

abstract class NewRoadEvent extends Event{
	
	String id;
	String srcJun, destJunc;
	Junction junSrc, junDest;
	int length, co2Limit, maxSpeed;
	Weather weather;

	NewRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
		this.id = id;
		this.srcJun = srcJun;
		this.destJunc = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}

	@Override
	void execute(RoadMap map) {
		junSrc = map.getJunction(srcJun);
		junDest = map.getJunction(destJunc);
		map.addRoad(cityRoad());
	}

	abstract protected Road cityRoad();
	
	@Override
	public String toString() {
	return "New Road '"+id+"'";
	}

	
}