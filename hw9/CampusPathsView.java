package hw9;

import java.awt.*;
import java.io.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import hw5.Edge;
import hw8.CampusCoord;
import hw8.CampusModel;

/**
 * @author luntzel 
 * Constructs a view object that depicts the map and path
 */
public class CampusPathsView extends JComponent {
	private static final long serialVersionUID = 1L; 
	private Image pic; 
	public static CampusModel m;
	private List<Edge<Double, CampusCoord>> path;
	private int xImage;
	private int yImage;
	private int xDisplay;
	private int yDisplay;

	/**
	 * A CampusPathsView constructor that builds the map from a data file
	 * @param model
	 * @param factor
	 */
	public CampusPathsView(CampusModel model, int factor) {
		m = model;

		xDisplay = 4 * factor;
		yDisplay = 3 * factor;
		//a content pane that contains the campus map with scalable dimensions 1024x768
		this.setPreferredSize(new Dimension(xDisplay, yDisplay));

		try {
			pic = ImageIO.read(new File("src/hw8/data/campus_map.jpg"));
			xImage = pic.getWidth(null);
			yImage = pic.getHeight(null);
			pic = pic.getScaledInstance(xDisplay, yDisplay, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			// helps catch problems by printing the stack trace of the exception
			e.printStackTrace();
		}
	}

	/**
	 * removes the path from the GUI
	 */
	public void clear() {
		path = null;
		repaint();
	}
	
	/**
	 * Finds the shortest path between two building names given by user from drop-down menus
	 * @param start
	 * @param end
	 * @return distance the total distance of the path
	 */
	public String findPath(String start, String end) {
		String abbStart = m.bNamesReverse.get(start);
		String abbEnd = m.bNamesReverse.get(end);
		
		CampusCoord coordStart = m.findCoordinates(abbStart);
		CampusCoord coordEnd = m.findCoordinates(abbEnd);
		
		path = m.findBestPath(coordStart, coordEnd);
		
		double dDistance = path.get(0).getLabel();
		String sDistance = String.format("%.0f", dDistance);
		this.repaint();
		return sDistance;
	}
	
	/**
	 * Paint the campus map whenever this is called by the window manager.
	 * The path is displayed if the user presses 'Find Path:'
	 * @param graphics g
	 */
	// @Override
	public void paintComponent(Graphics g) {
		// cast g to its actual class to make graphics2d methods available
		// (even though we don't use them here, it is common to use them)
		Graphics2D g2 = (Graphics2D) g;

		double xRatio = 1.0 * xDisplay / xImage;
		double yRatio = 1.0 * yDisplay / yImage;
		
		g2.drawImage(pic, 0, 0, xDisplay, yDisplay, null);

		// deals with path-finding behavior
		if (path != null) {
			CampusCoord coordStart = path.get(0).getStart().getValue();
			
			int xStart = (int) Math.round(coordStart.getX() * xRatio);
			int yStart = (int) Math.round(coordStart.getY() * yRatio);
			int xEnd;
			int yEnd;
			g2.setColor(Color.RED);
			g2.fillOval(xStart - 6, yStart - 6, 12, 12);
			g2.setStroke(new BasicStroke(2));
			for (Edge<Double, CampusCoord> e : path) {
				xEnd = (int) Math.round(e.getEnd().getValue().getX() * xRatio);
				yEnd = (int) Math.round(e.getEnd().getValue().getY() * yRatio);
				g2.drawLine(xStart, yStart, xEnd, yEnd);
				xStart = xEnd;
				yStart = yEnd;
			}
			g2.fillOval(xStart - 6, yStart - 6, 12, 12);
		}
	}
}