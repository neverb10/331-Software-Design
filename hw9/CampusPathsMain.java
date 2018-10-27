package hw9;

import hw8.CampusModel;

/**
 * @author luntzel Builds a campus model using a paths and buildings data file,
 *         allowing information like coordinates, paths, and distances to be
 *         accessed. Launches the Graphical User Interface by passing in the
 *         model to our GUI application.
 */
public class CampusPathsMain {

	/**
	 * runs the GUI application, which managers the view and controller
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		CampusModel model = new CampusModel("src/hw8/data/campus_buildings.dat",
				"src/hw8/data/campus_paths.dat");
		// default factor of 256 for 1024 * 768 resolution
		CampusPathsFrame frame = new CampusPathsFrame(model, 256);
	}
}