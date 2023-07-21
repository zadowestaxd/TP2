package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy{

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> g = new ArrayList<>();
		for (Vehicle vehicle : q) {
			g.add(vehicle);
		}
		return g;
	}
}