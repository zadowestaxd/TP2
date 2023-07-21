package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{

	private static final String[] columnNames = { "Id", "Length", "Weather", "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit" };

	private List<Road> roads;
	
	public RoadsTableModel(Controller _ctrl) {
		roads = new ArrayList<Road>();
		_ctrl.addObserver(this);
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public String getColumnName(int i) {
		return columnNames[i];
	}
	
	@Override
	public int getRowCount() {
		return roads.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Object object = null;
		switch(columnIndex) {
		case 0:
			object = roads.get(rowIndex).getId();
			break;
		case 1:
			object = roads.get(rowIndex).getLength();
			break;
		case 2:
			object = roads.get(rowIndex).getWeather();
			break;
		case 3:
			object = roads.get(rowIndex).getMaxSpeed();
			break;
		case 4:
			object = roads.get(rowIndex).getSpeedLimit();
			break;
		case 5:
			object = roads.get(rowIndex).getTotalCO2();
			break;
		case 6:
			object = roads.get(rowIndex).getContLimit();
			break;
		}
		return object;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				roads = map.getRoads();
				fireTableDataChanged();}
		});}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				roads = map.getRoads();
				fireTableDataChanged();}
		});}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				roads = map.getRoads();
				fireTableDataChanged();}
		});}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				roads = map.getRoads();
				fireTableDataChanged();}
		});}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				roads = map.getRoads();
				fireTableDataChanged();}
		});}

	@Override
	public void onError(String err) {
	}
}