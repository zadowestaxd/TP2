package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.RoadMap;
import simulator.model.Vehicle;

@SuppressWarnings("serial")
public class ChangeCO2ClassDialog extends JDialog {
	
	private JPanel emerge;
	private JLabel text;
	private JLabel vehicleText;
	private JLabel ticksText;
	private JLabel co2Text;
	private JComboBox<Vehicle> vehicle;
	private JComboBox<Integer> co2;
	private JSpinner ticks;
	
	private JPanel buttons;
	private JPanel options;
	private JButton ok;
	private JButton cancel;

	private int status = 0;
	private DefaultComboBoxModel<Vehicle> vehicleModel;
	private DefaultComboBoxModel<Integer> co2Model;

	public ChangeCO2ClassDialog(Frame frame) {
		super(frame, true);
		initGUI();
	}

	private void initGUI() {
		setTitle("Change CO2 Class");
		emerge = new JPanel();
		emerge.setLayout(new BoxLayout(emerge, BoxLayout.Y_AXIS));
		setContentPane(emerge);
		
		text = new JLabel("<html>Schedule an event to change the CO2 class of a selected vehicle after a given number of ticks from now.</html>");
		text.setAlignmentX(CENTER_ALIGNMENT);
		emerge.add(text);
		emerge.add(Box.createRigidArea(new Dimension(0, 20)));		
	
		buttons = new JPanel();
		buttons.setAlignmentX(CENTER_ALIGNMENT);
		emerge.add(buttons);
		
		vehicleText = new JLabel("Vehicle: ", JLabel.CENTER);
		vehicleModel = new DefaultComboBoxModel<Vehicle>();
		vehicle = new JComboBox<Vehicle>(vehicleModel);
		vehicle.setVisible(true);
		buttons.add(vehicleText);
		buttons.add(vehicle);
		
		co2Text = new JLabel("CO2 Class: ", JLabel.CENTER);
		co2Model = new DefaultComboBoxModel<Integer>();
		co2 = new JComboBox<Integer>(co2Model);
		co2.setVisible(true);
		buttons.add(co2Text);
		buttons.add(co2);
		
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
				ChangeCO2ClassDialog.this.setVisible(false);
			}
		});
		options.add(cancel);

		ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((vehicleModel.getSelectedItem() != null) && (co2Model.getSelectedItem() != null))
				{
					status = 1;
					ChangeCO2ClassDialog.this.setVisible(false);
				}
			}
		});
		options.add(ok);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public Integer getCO2Class() {
		
		return (Integer) co2Model.getSelectedItem();
	}
	public int open(RoadMap map) {
		
		for (Vehicle v : map.getVehicles())
			vehicleModel.addElement(v);
		
		for (int i = 0; i < 11; i++)
			co2Model.addElement(i);
		
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
		
		return status;
	}

	
	public Vehicle getVehicle() {
		return (Vehicle) vehicleModel.getSelectedItem();
	}

	public Integer getTicks() {
		return (Integer) ticks.getValue();
	}
}