package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy{
	private int timeslot;
	
	public MostCrowdedStrategy(int timeslot) {
		this.timeslot = timeslot;
	}
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if(roads.isEmpty())
			return -1;
		else if(currGreen == -1) {
			return nextGreen(qs, 0);
		}
		else if(currTime-lastSwitchingTime < timeslot)
			return currGreen;
		return nextGreen(qs, currGreen +1);
	}
	private int nextGreen(List<List<Vehicle>> qs, int beginning ) {
		int max = 0, index = 0;
					
		//no empieza desde el principio
	for(int i = 0; i < qs.size(); i++){
			
			//va cogiendo la cola m�s larga
			if(qs.get((beginning+i)%qs.size()).size() > max) {
				max = qs.get((beginning+i)%qs.size()).size();
				index = (beginning+i)%qs.size();
			}
			
		}
		return index;
	}

}
