package simulator.model;
import java.util.List;
import simulator.misc.Pair;

public class SetContClassEvent extends Event {
	
	private List<Pair<String, Integer>> cs;
	
	public SetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		if (cs.isEmpty())
			throw new IllegalArgumentException("Error: list is empty");
		this.cs = cs;
	}
	
	@Override
	void execute(RoadMap map) {
		for (Pair<String, Integer> vehicle : cs) {
			if(map.getVehicle(vehicle.getFirst()) == null)
				throw new IllegalArgumentException("Vehicle doesn't exist in the RoadMap");
			map.getVehicle(vehicle.getFirst()).setContClass(vehicle.getSecond());
		}		
	}
	
	public String toString() {
		return cs.toString();
	}
	
}