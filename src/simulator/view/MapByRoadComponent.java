package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;


public class MapByRoadComponent extends JComponent implements TrafficSimObserver{

	private static final Color _BackG_COLOR = Color.white;
	private static final Color _JUNCT_COLOR = Color.blue;
	private static final Color _JUNCT_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.green;
	private static final Color _RED_LIGHT_COLOR = Color.red;
	private static final int _JRADIUS = 10;
	
	private RoadMap map;
	private static Image car;
	private Graphics2D s;
	MapByRoadComponent(Controller _ctrl){
		initGui();
		_ctrl.addObserver(this);
	}
	
	private void initGui() {
		car = loadImage("car.png");
		
		setPreferredSize(new Dimension(350, 190));
	}
	
	private Image loadImage(String string) {
		Image i = null;
		try {
			i = ImageIO.read(new File("resources/icons/" + string));
		} catch (IOException e) {
			
		}
		return i;
	}

	public void paintComponent(Graphics graphics) {
	super.paintComponent(graphics);	
	 s = (Graphics2D) graphics;
	 s.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	 s.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	s.setColor(_BackG_COLOR);
	s.clearRect(0, 0, getWidth(), getHeight());
	
	if (map == null || map.getJunctions().size() == 0) {
		s.setColor(Color.red);
		s.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
	} else {
		updatePrefferedSize();
		drawMap(s);
	}
	}
	
	private void drawMap(Graphics g) {
		drawRoads(g);
		drawVehicles(g);
		drawJunctions(g);
		drawWeather(g);
		drawCO2(g);
		drawRoadsId(g);
	}
	
	private void drawRoadsId(Graphics g) {
		int j = 0;
		for (Road r : map.getRoads()) {
			g.drawString(r.getId(), 25, ((j+1) * 50) + 4);
		j++;
		}
	}

	private void drawCO2(Graphics g) {
		int i = 0;
		
		for (Road r : map.getRoads()) {
			int a = (int) Math.floor(Math.min((double) r.getTotalCO2() / (1.0 + (double) r.getContLimit()), 1.0) / 0.19);
			switch(a) {
				case 0:
					
					g.drawImage(loadImage("cont_0.png"), getWidth() - 48,((i + 1) * 50) - 16, 32, 32, this);
					break;
				case 1:
					
					g.drawImage(loadImage("cont_1.png"), getWidth() - 48,((i + 1) * 50) - 16, 32, 32, this);
					break;
				case 2:
					
					g.drawImage(loadImage("cont_2.png"), getWidth()- 48,((i + 1) * 50) - 16, 32, 32, this);
					break;
				case 3:
					
					g.drawImage(loadImage("cont_3.png"), getWidth()- 48,((i + 1) * 50) - 16, 32, 32, this);
					break;
				case 4:
					
					g.drawImage(loadImage("cont_4.png"), getWidth()- 48,((i + 1) * 50) - 16, 32, 32, this);
					break;
				default:
					
					g.drawImage(loadImage("cont_5.png"), getWidth()- 48,((i + 1) * 50) - 16, 32, 32, this);
					break;
			}
			i++;
		}
	}

	private void drawWeather(Graphics g) {
		int i = 0;
		for (Road r : map.getRoads()) {
			switch(r.getWeather()) {
				case CLOUDY:
					
					g.drawImage(loadImage("cloud.png"),  getWidth()- 85, ((i + 1) * 50) - 16, 32, 32, this);
					break;
				case RAINY:
					
					g.drawImage(loadImage("rain.png"),  getWidth()- 85, ((i + 1) * 50) - 16, 32, 32, this);
					break;
				case STORM:
					
					g.drawImage(loadImage("storm.png"),  getWidth()- 85, ((i + 1) * 50) - 16, 32, 32, this);
					break;
				case SUNNY:
					
					g.drawImage(loadImage("sun.png"),  getWidth()- 85, ((i + 1) * 50) - 16, 32, 32, this);
					break;
				case WINDY:
					
					g.drawImage(loadImage("wind.png"),  getWidth()- 85, ((i + 1) * 50) - 16, 32, 32, this);
					break;
			}
			i++;
		}
	}

	private void drawJunctions(Graphics g) {
		int i = 0;
		
		for (Road r : map.getRoads()) {
			Junction origin = r.getSrc();
			Junction dest = r.getDest();
			
			int x = 50;
			int y = (i + 1) * 50;
			int x1 = getWidth() - 100;
			int y1 = (i + 1) * 50;
			
			Color junctionColor = _RED_LIGHT_COLOR;
			int index = dest.getGreenLightIndex();
			if (index != -1 && r.equals(dest.getInRoads().get(index))) 
				junctionColor = _GREEN_LIGHT_COLOR;
			
			g.setColor(_JUNCT_COLOR);
			g.fillOval(x - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
		
			g.setColor(junctionColor);
			g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			
			g.setColor(_JUNCT_COLOR);
			g.drawString(origin.getId(), x1 - 2, y1 - 7);
			g.setColor(_JUNCT_LABEL_COLOR);
			i++;
	}
	}

	private void drawVehicles(Graphics g) {
		for (Vehicle v : map.getVehilces()) {
			
			int cont = 0;
			int cont1 = 0;
			
			if (v.getStatus() != VehicleStatus.ARRIVED) {

				Road r = v.getRoad();
				int x = 50;
				int x1 = getWidth() - 120;
				int vX = (x + (int)((x1 - x) * ((double) v.getLocation() / (double) r.getLength())));
			
				for (Road roadAux : map.getRoads()) {
					if (roadAux.getId() == r.getId())
						cont1 = cont;
					else
						cont++;
				}
				int vY = (cont1 + 1) * 50;
				
				// Choose a color for the vehicle's label and background, depending on it's 
				// contamination class
				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));
				
				// Draw an image of a car and it's identifier
				g.drawImage(car, vX, vY - 10, 16, 16, this);
				g.drawString(v.getId(), vX, vY - 10);
			}
		}
	}
	private void drawRoads(Graphics g) {
		int cont = 0;
		
		for (int j = 0; j < map.getRoads().size(); j++) {
			
			int x1 = 50;
			int y1 = (cont + 1) * 50;
			int x2 = getWidth() - 100;
			int y2 = (cont + 1) * 50;
			
			g.setColor(Color.black);
			g.drawLine(x1, y1, x2, y2);
			cont++;
		}
	}

	private void updatePrefferedSize() {
		int maxW = 450;
		int maxH = 345;
		for (Junction j : map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
			setPreferredSize(new Dimension(maxW, maxH));
			setSize(new Dimension(maxW, maxH));
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				update(map);}
		});}

	private void update(RoadMap map2) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				map = map2;
				repaint();}
		});}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				update(map);}
		});}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				update(map);}
		});}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				update(map);}
		});}

	@Override
	public void onError(String err) {
	}
}