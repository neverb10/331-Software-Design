package hw8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import hw5.Edge;
import hw5.Graph;
import hw5.Node;
import hw7.MarvelPaths2;
/**
 * @author luntzel
 * 
 * A class for finding campus paths, which are routes on a campus.
 * 
 * Specification fields:
 * @specfield cPaths : Graph<Double, CampusCoord> // a graph of path segments and distances
 * @specfield bCoords : Map<String, CampusCoord> // map of abbreviated names to building points 
 * @specfield bNames: Map<String, String> // a map of building names (short and long)
 *
 * Derived specification fields:
 * @derivedfield distance : double // physical distance between two campus coordinates
 *
 * Abstract Invariant: no elements null
 * 
 */
public class CampusModel {
	private static final boolean DEBUG_FLAG = false;
	private Graph<Double, CampusCoord> cPaths;
	private Map<String, CampusCoord> bCoords;
	private Map<String, String> bNames;
	public Map<String, String> bNamesReverse;
	/**
	 * Abstraction Function: AF(this.a, this.b) = a model for finding paths between spatial
	 * coordinates. Can find the shortest path.
	 * collection of nodes and their outgoing edges.
	 * 
	 * Rep Invariant: specfields != null, elements in specfields != null
	 * 
	 */
	
	/**
	 * @param pathFile
	 * @param buildingFile
	 * @throws Exception
	 * @effects constructs a graph
	 */
	public CampusModel(String bFile, String pFile) throws Exception {
		if (bFile == null) {
			throw new IllegalArgumentException("building file must be non-null.");
		}
		if (pFile == null) {
			throw new IllegalArgumentException("path file must be non-null.");
		}
		HashMap<Node<CampusCoord>, HashSet<Edge<Double, CampusCoord>>> graphField = new HashMap<Node<CampusCoord>, 
				HashSet<Edge<Double, CampusCoord>>>();
		
		bCoords = new HashMap<String, CampusCoord>();
		bNames = new HashMap<String, String>();
		cPaths = new Graph<Double, CampusCoord>(graphField);
		bNamesReverse = new HashMap<String, String>();
		
		CampusParser.parseBuildings(bFile, bNames, bCoords);
		CampusParser.parsePaths(pFile, cPaths);
		
		for (Entry<String, String> e : bNames.entrySet()) {
			bNamesReverse.put(e.getValue(), e.getKey());
		}
		
		checkRep();
	}
	
	/**
	 * @param starting coords
	 * @param ending coords
	 * @return shortest path between two coordinates on campus
	 */
	public List<Edge<Double, CampusCoord>> findBestPath(CampusCoord start, CampusCoord end){
		checkRep();
		return MarvelPaths2.findPath(cPaths, start, end);
	}
	
	public Map<Double, String> distances(List<Edge<Double, CampusCoord>> path) {
		//path comes from hw7 find path, so the 0th index should be the total distance.
		//get values in the order you inserted with a LinkedHashMap
		Map<Double, String> segmentDistances = new LinkedHashMap<Double, String>();
		double totalDistance = path.get(0).getLabel();
		double distance;
		CampusCoord start;
		CampusCoord end;
		String direction;
		//missing one value at line 30: distance 43 from (1666, 1656) to (1666, 1677)
		//this causes the distance/direction pairing
		for (int i = 0; i < path.size(); i++) {
			start = path.get(i).getStart().getValue();
			end = path.get(i).getEnd().getValue();
			if (!start.equals(end)) {
				distance = path.get(i).getLabel();
				direction = start.getDirection(end);
				segmentDistances.put(distance, direction);
			}
		}
		segmentDistances.put(totalDistance, "");
		return segmentDistances;
	}
	
	public List<Double> pairList(List<Edge<Double, CampusCoord>> path) {
		List<Double> pL = new ArrayList<Double>();
		for (int i = 2; i < path.size(); i++) {
			//not supposed to print the first index. its the start location.
			//the coordinates are matched up
			pL.add(path.get(i).getStart().getValue().getX());
			pL.add(path.get(i).getStart().getValue().getY());
		}
		pL.add(path.get(path.size() - 1).getEnd().getValue().getX());
		pL.add(path.get(path.size() - 1).getEnd().getValue().getY());
		return pL;
	}
	
	/**
	 * @return a map of building abbreviations and locations
	 */
	public Map<String, CampusCoord> abbCoords() {
		checkRep();
		return new HashMap<String, CampusCoord>(bCoords);
	}
	
	/**
	 * @param bName, abbreviated name of building
	 * @return point at which said building exists
	 */
	public CampusCoord findCoordinates(String bName) {
		checkRep();
		if (bName == null) {
			throw new IllegalArgumentException("Building must be non-null");
		}
		if (!bCoords.containsKey(bName)) {
			throw new IllegalArgumentException("Supplied data must contain building");
		}
		return bCoords.get(bName);
	}
	
	/**
	 * @return a map of building names: abbreviated and full
	 */
	public Map<String, String> abbFull() {
		checkRep();
		return new HashMap<String, String>(bNames);
	}
	
	/**
	 * @param bName, abbreviated name of building
	 * @return full name of building
	 */
	public String getFull(String bName) {
		checkRep();
		if (bName == null) {
			throw new IllegalArgumentException("Building must be non-null");
		}
		if (!bNames.containsKey(bName)) {
			throw new IllegalArgumentException("Supplied data must contain building");
		}
		return bNames.get(bName);
	}
	
	/**
	 * @effects determines if the model rep invariant is upheld
	 */
	private void checkRep() {
		if (DEBUG_FLAG) {
			assert (cPaths != null): "Graph must be non-null";
			assert (bCoords != null): "building-coordinate map must be non-null";
			assert (bNames != null): "name-name map must be non-null";
			
			Iterator<Map.Entry<String, CampusCoord>> itrCoords = bCoords.entrySet().iterator();
			Iterator<Entry<String, String>> itrNames = bNames.entrySet().iterator();
			
			while (itrCoords.hasNext()) {
				String abbC = itrCoords.next().getKey();
				assert bCoords.get(abbC) != null: "Building coordinates must be non-null";
			    assert abbC != null : "building-coordinate name must be non-null.";
			}
			
			while (itrNames.hasNext()) {
				String abbN = itrNames.next().getKey();
				assert bNames.get(abbN) != null: "Building name value cannot be null";
			    assert abbN != null : "Building name key cannot be null.";
			}
		}
	}
}