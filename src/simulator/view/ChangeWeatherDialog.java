package simulator.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import simulator.model.*;

@SuppressWarnings("serial")
public class ChangeWeatherDialog extends JDialog {
	
	private JPanel emerge;
	private JLabel text;
	private JLabel roadText;
	private JLabel ticksText;
	private JLabel weatherText;
	private JComboBox<Road> road;
	private JComboBox<Weather> weather;
	private JSpinner ticks;
		
	private JPanel buttons;
	private JPanel options;
	private JButton ok;
	private JButton cancel;

	private int status = 0;
	private DefaultComboBoxModel<Road> roadModel;
	private DefaultComboBoxModel<Weather> weatherModel;

	public ChangeWeatherDialog(Frame frame) {
		super(frame, true);
		initGUI();
	}

	private void initGUI() {
		setTitle("Change Road Weather");
		emerge = new JPanel();
		emerge.setLayout(new BoxLayout(emerge, BoxLayout.Y_AXIS));
		setContentPane(emerge);
		
		text = new JLabel("<html>Order an event to change the weather class of a road after a given number of ticks from now.</html>");
		text.setAlignmentX(CENTER_ALIGNMENT);
		emerge.add(text);
		emerge.add(Box.createRigidArea(new Dimension(0, 20)));		
		
		buttons = new JPanel();
		buttons.setAlignmentX(CENTER_ALIGNMENT);
		emerge.add(buttons);
			
		roadText = new JLabel("Road: ", JLabel.CENTER);
		roadModel = new DefaultComboBoxModel<Road>();
		road = new JComboBox<Road>(roadModel);
		road.setVisible(true);
		buttons.add(roadText);
		buttons.add(road);
			
		weatherText = new JLabel("CO2 Class: ", JLabel.CENTER);
		weatherModel = new DefaultComboBoxModel<Weather>();
		weather = new JComboBox<Weather>(weatherModel);
		weather.setVisible(true);
		buttons.add(weatherText);
		buttons.add(weather);
			
		ticks = new JSpinner();
		ticksText = new JLabel("Ticks: ", JLabel.CENTER);
		ticks = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1)); //value, min, max, step
		ticks.setMaximumSize(new Dimension(70, 23));
		ticks.setMinimumSize(new Dimension(70, 23));
		ticks.setPreferredSize(new Dimension(70, 23));
			
		buttons.add(ticksText);
		buttons.add(ticks);
			
		options = new JPanel();
		options.setAlignmentX(CENTER_ALIGNMENT);
		emerge.add(options);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				status = 0;
				ChangeWeatherDialog.this.setVisible(false);	
			}
		});
		options.add(cancel);

		ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((weatherModel.getSelectedItem() != null) && (roadModel.getSelectedItem() != null))
				{
					status = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
		});
		options.add(ok);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public Weather getWeather() {
		
		return (Weather) weatherModel.getSelectedItem();
	}
	
	public Road getRoad() {
		return (Road) roadModel.getSelectedItem();
	}
	
	public int open(RoadMap mapa) {
		for (Road r : mapa.getRoads())
		
			roadModel.addElement(r);
	
		for (Weather w : Weather.values())
		
			weatherModel.addElement(w);
		
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
			
		return status;
	}

	public Integer getTicks() {
		return (Integer) ticks.getValue();
	}
}