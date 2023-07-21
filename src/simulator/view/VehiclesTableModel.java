package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	List<Vehicle> vehicles;
	
	private static final String columnNames[] = {"Id", "Location", "Iterinary", "CO2 Class", "Max. Speed", "Speed", "Total CO2", "Distance"}; 
	
	VehiclesTableModel(Controller _ctrl){
		vehicles = new ArrayList<>();
		_ctrl.addObserver(this);
	}
	
	public String getColumnName(int i) {
		return columnNames[i];
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return vehicles.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;

		switch (columnIndex)
		{
			case 0:
				s = vehicles.get(rowIndex).getId();
				break;
			case 1:
				VehicleStatus status = vehicles.get(rowIndex).getStatus();
				StringBuilder text = new StringBuilder();
				switch (status)
				{
					case PENDING:
						text.append("Pending");
						break;
					case WAITING:    
						text.append("Waiting:"+ vehicles.get(rowIndex).getItinerary());
						break;
					case TRAVELING:  
						text.append(vehicles.get(rowIndex).getRoad() + ": " + vehicles.get(rowIndex).getLocation());
						break;
					case ARRIVED:    
						text.append("Arrived");
						break;
				}
				s = text.toString();
				break;
			case 2:
				s = vehicles.get(rowIndex).getItinerary();
				break;
			case 3:
				s = vehicles.get(rowIndex).getContClass();
				break;
			case 4:
				s = vehicles.get(rowIndex).getMaxSpeed();
				break;
			case 5:
				s = vehicles.get(rowIndex).getSpeed();
				break;
			case 6:
				s = vehicles.get(rowIndex).getTotalCO2();
				break;
			case 7:
				s = vehicles.get(rowIndex).getTotalDistance();
				break;
			default:
				break;
		}
		
		return s;
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}
	
	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				vehicles = map.getVehicles();
				fireTableDataChanged();	}
		});}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				vehicles = map.getVehicles();
				fireTableDataChanged();	}
		});}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {	
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				vehicles = map.getVehicles();
				fireTableDataChanged();	}
		});}

	@Override
	public void onError(String err) {
		
	}
}