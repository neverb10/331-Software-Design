package hw8;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import java.util.List;
import java.util.ArrayList;

public class CampusVC {

	/**
	 * @author luntzel
	 * 
	 * A class for displaying the user and displaying routes.
	 * 
	 */
	public static void main(String[] args) {
		Scanner control = new Scanner(System.in);
		try {
			CampusModel cM = new CampusModel("src/hw8/data/campus_buildings.dat", "src/hw8/data/campus_paths.dat");

			String m = "Menu:\n" + "	r to find a route\n" + "" + "	b to see a list of all buildings\n"
					+ "	q to quit";
			String response = "";
			System.out.println(m + "\n");
			System.out.print("Enter an option ('m' to see the menu): ");

			while (true) {
				response = control.nextLine();
				while (response.startsWith("#") || response.length() == 0) {
					System.out.println(response);
					response = control.nextLine();
				}

				if (response.equals("b")) {
					outputBuildings(cM);
				} else if (response.equals("m")) {
					System.out.println(m + "\n");
				} else if (response.equals("q")) {
					control.close();
					return;
				} else if (response.equals("r")) {
					System.out.print("Abbreviated name of starting building: ");
					String start = control.nextLine();
					System.out.print("Abbreviated name of ending building: ");
					String end = control.nextLine();
					outputBestPath(start, end, cM);
				} else {
					System.out.println("Unknown option\n");
				}
				System.out.print("Enter an option ('m' to see the menu): ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @param a campus model
	 * @requires model != null
	 * @effects prints the user a list of buildings alphabetically
	 */
	public static void outputBuildings(CampusModel model) {
		String output = "Buildings:\n";

		Map<String, String> buildingNames = model.abbFull();
		Set<String> abbs = new TreeSet<String>(buildingNames.keySet());

		for (String abb : abbs) {
			output += "\t" + abb + ": " + buildingNames.get(abb) + "\n";
		}

		System.out.println(output);
	}

	/**
	 * @param start abbreviated name of starting building
	 * @param end abbreviated name of ending building
	 * @param model of the campus path and buildings
	 * @effects prints the user a list of path segments to take from start to end
	 */
	public static void outputBestPath(String start, String end, CampusModel model) {
		if (start.equals(null)) {
			throw new IllegalArgumentException("start must be non-null.");
		}
		if (end.equals(null)) {
			throw new IllegalArgumentException("end location must be non-null.");
		}
		if (model == null) {
			throw new IllegalArgumentException("model be non-null.");
		}
		
		Map<String, String> buildingNames = model.abbFull();
		
		if (!buildingNames.containsKey(start) && !buildingNames.containsKey(end)) {
			System.out.println("Unknown building: " + start);
			System.out.println("Unknown building: " + end + "\n");
			return;
		} else if (!buildingNames.containsKey(end)) {
			System.out.println("Unknown building: " + end + "\n");
			return;
		} else if (!buildingNames.containsKey(start)) {
			System.out.println("Unknown building: " + start + "\n");
			return;
		}
		
		CampusCoord startCoord = model.findCoordinates(start);
		CampusCoord endCoord = model.findCoordinates(end);
		
		String output = "";
		String startFull = model.getFull(start);
		String endFull = model.getFull(end);
		
		output += "Path from " + startFull + " to " + endFull + ":\n";
		
		Map<Double, String> segDist = model.distances(model.findBestPath(startCoord, endCoord));
		List<Double> coordPairs = model.pairList(model.findBestPath(startCoord, endCoord));
		List<Double> list = new ArrayList<Double>(segDist.keySet());

		for (int i = 0; i < segDist.size() - 1; i++) {
			output += "\tWalk " + String.format("%.0f", list.get(i)) + " feet " + 
					segDist.get(list.get(i)) + " to (" + String.format("%.0f", coordPairs.get(i * 2))
					+ ", " + String.format("%.0f", coordPairs.get(i * 2 + 1)) + ")\n";
		}
		//the value at list's last index changes when i change the way distance is calculated in the model
		output += "Total distance: " + String.format("%.0f", list.get(segDist.size() - 1)) + 
				" feet";
		System.out.println(output + "\n");
	}
}