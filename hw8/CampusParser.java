package hw8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import hw5.*;

/**
 * Parser utility to load the campus path data files.
 */
public class CampusParser {

	/**
	 * A checked exception class for bad data files
	 */
	@SuppressWarnings("serial")
	public static class MalformedDataException extends Exception {
		public MalformedDataException() {
		}

		public MalformedDataException(String message) {
			super(message);
		}

		public MalformedDataException(Throwable cause) {
			super(cause);
		}

		public MalformedDataException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	/**
	 * Reads building data files to parse it accordingly.
	 * 
	 * @requires filename is a well-formed valid file path (proper format)
	 * @param filename
	 *            the file that will be read, map of building points
	 * @param bNames
	 *            map of building names (short and long)
	 * @param bCoords
	 *            map of abbreviated names to building points
	 * @modifies bCoords && bNames
	 * @throws MalformedDataException
	 *             if the file is not well-formed: each line contains exactly two
	 *             tokens separated by a tab, or else starting with a # symbol to
	 *             indicate a comment line.
	 */
	public static void parseBuildings(String filename, Map<String, String> bNames, Map<String, CampusCoord> bCoords)
			throws MalformedDataException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));

			String inputLine;
			while ((inputLine = reader.readLine()) != null) {

				// Ignore comment lines.
				if (inputLine.startsWith("#")) {
					continue;
				}

				// Parse the data, stripping out quotation marks and throwing
				// an exception for malformed lines.
				inputLine = inputLine.replace("\"", "");
				String[] tokens = inputLine.split("\t");
				/*
				for (int i = 0; i < tokens.length; i++) {
					System.out.println(tokens[i]);
				}
				*/
				if (tokens.length != 4) {
					throw new MalformedDataException("Line should contain exactly one tab: " + inputLine);
				}

				String abb = tokens[0];
				String full = tokens[1];

				double x = Double.parseDouble(tokens[2]);
				double y = Double.parseDouble(tokens[3]);
				//System.out.print(abb + " " + full + " " + x + " " + y);

				CampusCoord c = new CampusCoord(x, y);

				// key-value of abbreviated tag to full name (best way to do it?)
				bNames.put(abb, full);
				// key-value of abbreviated tag to coordinate sPoint
				bCoords.put(abb, c);
			}
		} catch (IOException e) {
			System.err.println(e.toString());
			e.printStackTrace(System.err);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println(e.toString());
					e.printStackTrace(System.err);
				}
			}
		}
	}

	/**
	 * Reads path data files to parse it accordingly.
	 * 
	 * @requires filename is a well-formed valid file path (proper format)
	 * @param filename
	 *            the file that will be read, map of paths and connected segments
	 * @param cPaths
	 *            the graph to be filled with coordinates and respective paths
	 * @throws Exception
	 *             if the file is not the proper format
	 * @modifies cPaths
	 */
	public static void parsePaths(String filename, Graph<Double, CampusCoord> cPaths) throws Exception {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));

			String inputLine;
			CampusCoord initialC = null;
			Node<CampusCoord> segStart;
			while ((inputLine = reader.readLine()) != null) {

				// Ignore comment lines.
				if (inputLine.startsWith("#")) {
					continue;
				}
				inputLine = inputLine.replace("\"", "");
				inputLine = inputLine.replace("\t", "");
				// split coordinate and distance
				String[] coordDist = inputLine.split(": ");
				// split x and y component
				String[] xY = coordDist[0].split(",");
				
				// create coordinate for path segment start point
				CampusCoord c = new CampusCoord(Double.parseDouble(xY[0]), Double.parseDouble(xY[1]));
				//node adding correctly
				//System.out.println(c.getX() + " " + c.getY());
				if (c != null && coordDist.length > 0 && coordDist.length < 3) {
					Node<CampusCoord> eNode = new Node<CampusCoord>(c);
					if (!cPaths.hasNode(eNode)) {
						cPaths.placeNode(eNode);
					}
					// if the line is a start point and not a connecting point
					if (coordDist.length == 1) {
						initialC = c;
					} else if (coordDist.length == 2) { // and otherwise a connecting point
						if (initialC == null) {
							throw new Exception("improper line file ordering");
						}
						segStart = new Node<CampusCoord>(initialC);
						Edge<Double, CampusCoord> e = new Edge<Double, CampusCoord>(segStart, eNode,
								Double.parseDouble(coordDist[1]));
						//edge adding correctly
						//System.out.println(e.getStart().getValue().getX() + " " + e.getStart().getValue().getY()
								//+ "  " + e.getEnd().getValue().getX() + " " + e.getEnd().getValue().getY() + " "
								//+ e.getLabel());
						cPaths.placeEdge(e);
					}
				} else {
					throw new Exception("File must be properly formatted");
				}
			}
		} catch (IOException e) {
			System.err.println(e.toString());
			e.printStackTrace(System.err);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println(e.toString());
					e.printStackTrace(System.err);
				}
			}
		}
	}

}
