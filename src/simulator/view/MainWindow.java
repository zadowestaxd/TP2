package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import simulator.control.Controller;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	private Controller _ctrl;

	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END);
		
		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);
		
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);
				
		JPanel eventsTable = createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		eventsTable.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsTable);
		
		JPanel vehiclesTable = createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		vehiclesTable.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(vehiclesTable);
		
		JPanel roadsTable = createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		roadsTable.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(roadsTable);
		
		JPanel junctionsTable = createViewPanel(new JTable(new JunctionTableModel(_ctrl)), "Junctions");
		junctionsTable.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(junctionsTable);
		
		JPanel mapTable = createViewPanel(new MapComponent(_ctrl), "Map");
		mapTable.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapTable);
		
		JPanel mapByRoadTable = createViewPanel(new MapByRoadComponent(_ctrl), "Map by Road");
		mapByRoadTable.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapByRoadTable);
			
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		
	}
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout() );
		Border b = BorderFactory.createLineBorder(Color.BLACK, 2);
		p.setBorder(BorderFactory.createTitledBorder(b, title));
		p.add(new JScrollPane(c));
		return p;
	}
}