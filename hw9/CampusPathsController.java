package hw9;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import hw8.CampusModel;

public class CampusPathsController extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel p;
	private JLabel d;

	public CampusPathsController(CampusModel model, CampusPathsView v) {
		Map<String, String> buildingMap = model.abbFull();
		List<String> buildingFull = new ArrayList<String>();
		Collection<String> temp = buildingMap.values();
		for (String s : temp) {
			buildingFull.add(s);
		}
		Collections.sort(buildingFull);

		d = new JLabel();
		p = new JPanel();

		JLabel start = new JLabel("Starting building: ");
		JLabel end = new JLabel("Ending building: ");

		JButton path = new JButton("Find path: ");
		JButton reset = new JButton("Reset");
		

		JComboBox<String> buildingsS = new JComboBox<String>(buildingFull.toArray(new String[buildingFull.size()]));
		JComboBox<String> buildingsE = new JComboBox<String>(buildingFull.toArray(new String[buildingFull.size()]));

		p.add(buildingsS, start);
		p.add(buildingsE, end);
		p.add(path);
		p.add(reset);

		path.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String distance = v.findPath(buildingsS.getSelectedItem().toString(),
						buildingsE.getSelectedItem().toString());
				d.setText(distance + " feet");
			}
		});

		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				d.setText("");
				buildingsS.setSelectedIndex(0);
				buildingsE.setSelectedIndex(0);
				v.clear();
			}
		});
	}

	public JLabel getDistanceLabel() {
		return d;
	}

	public JPanel getControlPanel() {
		return p;
	}
}