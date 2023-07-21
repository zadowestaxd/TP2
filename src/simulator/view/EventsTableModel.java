package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{

	private Controller _ctrl;
	private List<Event> _events;
	private static final String[] _columns = {"Time", "Desc."};
	
	EventsTableModel(Controller _ctrl){
		//super();
		this._ctrl = _ctrl;
		_events = new ArrayList<Event>();
		this._ctrl.addObserver(this);
	}
	
	public String getColumnName(int column) {
		return _columns[column];
	}
	
	@Override
	public int getColumnCount() {
		return _columns.length;
	}
	
	@Override
	public int getRowCount() {
		if(_events == null)
			return 0;
		return _events.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnsIndex) {
		Object object = null;
		switch (columnsIndex) {
		case 0:
			object = _events.get(rowIndex).getTime();
			break;
		case 1:
			object = _events.get(rowIndex).toString();
			break;
		default: 
			return null;
		}
		return object;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_events = events;
				fireTableDataChanged();}
		});}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_events = events;
				fireTableDataChanged();}
		});}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_events = events;
				fireTableDataChanged();}
		});}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_events = events;
				fireTableDataChanged();}
		});}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_events = events;
				fireTableDataChanged();}
	});}

	@Override
	public void onError(String err) {
	}
}