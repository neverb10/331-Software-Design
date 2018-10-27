package hw9;

import java.awt.*;

import javax.swing.*;
import hw8.CampusModel;

/**
 * @author luntzel
 * A GUI that combines the view and controller to view the Campus and 
 * find paths between various locations
 */
public class CampusPathsFrame {
	private CampusModel m;
	private JFrame jF;
	private JPanel cP;
	
	public CampusPathsFrame(CampusModel model, int factor) {
		//check if model is in proper conditions
		m = model;
		if (m == null) {
			throw new IllegalArgumentException("Model must be non-null");
		}
		if (factor <= 0) {
			throw new IllegalArgumentException("Factor must be at least 1");
		}
		
		//factor to set the dimensions of the GUI
		//f = factor;
		jF = new JFrame("Campus Path Finder");
		
		//add the view, which determines the map and path display, to the frame
		CampusPathsView v = new CampusPathsView(m, factor);
		jF.add(v);
		
		//add the controller's panel, which determines the buttons and UI to the frame
		CampusPathsController c = new CampusPathsController(m, v); //vP
		cP = c.getControlPanel();
		jF.add(cP, BorderLayout.NORTH);
		
		//distance label at the bottom of the GUI
		JLabel distance = c.getDistanceLabel();
		distance.setHorizontalAlignment(SwingConstants.CENTER);
		jF.add(distance, BorderLayout.SOUTH);
		
		jF.pack();
		jF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jF.setVisible(true);
	}
}
