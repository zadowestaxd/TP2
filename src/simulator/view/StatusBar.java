package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver{

	private JLabel timeL;
	private JLabel eventL;
	
	public StatusBar(Controller _ctrl) {
		initGui();
		_ctrl.addObserver(this);
	}

	private void initGui() {
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		setBorder(BorderFactory.createBevelBorder(1));
        JPanel timeP = new JPanel();
        this.add(timeP);
        timeL = new JLabel("Time: 0");
        
        timeP.add(timeL);
        // timeP.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.LINE_END);
        
        //this.setLayout(new BorderLayout());
        JPanel eventsP = new JPanel();
        this.add(eventsP);
        eventL = new JLabel("Welcome!");    
        eventsP.add(eventL);
        
        setVisible(true);
        
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				timeL.setText("Time: " + time);
				eventL.setText("");} // TODO meter la info
		});}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				eventL.setText("Event added: " + e.toString());}
		});}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				timeL.setText("Time: " + time);
				eventL.setText("Welcome!");}
		});}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
	}
}