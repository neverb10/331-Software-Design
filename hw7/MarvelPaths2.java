package hw7;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import hw5.Edge;
import hw5.Graph;
import hw5.Node;

public class MarvelPaths2 {

	public static Graph<Double, String> loadGraph(String filename) throws Exception {
		if (filename == null)
			throw new IllegalArgumentException("filename must be non-null.");

		Graph<Double, String> weightGraph = new Graph<Double, String>(new HashMap<Node<String>,
				HashSet<Edge<Double, String>>>());
		
		Map<String, HashMap<String, Integer>> tallyShared = new HashMap<String, HashMap<String, Integer>>();

		// parsing data
		MarvelParser2.parseData(filename, tallyShared);

		// adding nodes to graph. in the parser, i add all the characters into sharedbooks so this should
		// be adding every character in the file.
		for (String nodeValue : tallyShared.keySet()) {
			Node<String> node = new Node<String>(nodeValue);
			//System.out.println(nodeValue);
			//verified that tallyShared contains the nodes
			weightGraph.placeNode(node);
		}
		
		// place edges based on weights stored up from the parser
		for (String nodeValue : tallyShared.keySet()) {
			Node<String> node = new Node<String>(nodeValue);
			//characters that have a book connection to nodeValue
			HashMap<String, Integer> count = tallyShared.get(nodeValue);
			for (String nodeOfNode : count.keySet()) {
				Node<String> node2 = new Node<String>(nodeOfNode);
				int weight = count.get(nodeOfNode);
				Edge<Double, String> e1 = new Edge<Double, String>(node, node2, 1.0 / weight);
				//Edge<Double> e2 = new Edge<Double>(node2, node, 1.0 / weight);
				weightGraph.placeEdge(e1);
				//weightGraph.placeEdge(e2);
			}
		}

		return weightGraph;
	}

	/**
	 * Finds the least distance path in a weighted dataset between two characters
	 * @param <T>
	 * 
	 * @param graph
	 *            used to search for two strings representing start and end
	 *            character
	 * @requires graph contains start and end character, graph != null, start !=
	 *           null, end != null
	 * @throws IllegalArgumentException
	 *             if any requirements not met
	 * @return the shortest path (least-weighted) between the two characters, and
	 *         otherwise null if it terminates
	 * 
	 */
	//TODO: Coordinates vs edge in list
	public static <E extends Comparable<E>> List<Edge<Double, E>> findPath(Graph<Double, E> dGraph, E start, E end) {
		if (dGraph == null) {
			throw new IllegalArgumentException("graph must not be null.");
		}

		if (!(dGraph.adjacencyList.containsKey(new Node<E>(start)))
				|| !(dGraph.adjacencyList.containsKey(new Node<E>(end)))) {
			throw new IllegalArgumentException("Nodes start and end must exist in the graph.");
		}

		if (start == null || end == null) {
			throw new IllegalArgumentException("start and end cannot be null.");
		}

		// sNode = starting node
		Node<E> sNode = new Node<E>(start);
		// eNode = destination node
		Node<E> eNode = new Node<E>(end);
		// finished = set of nodes for which we know the minimum-cost path from start.
		Set<Node<E>> finished = new HashSet<Node<E>>();

		//distances of all the edges added up, comparing those
		PriorityQueue<List<Edge<Double, E>>> workList = new PriorityQueue<List<Edge<Double, E>>>(20,
				new Comparator<List<Edge<Double, E>>>() {
					public int compare(List<Edge<Double, E>> p1, List<Edge<Double, E>> p2) {
						double p1Sum = p1.get(0).getLabel();
						double p2Sum = p2.get(0).getLabel();
						/*for (Edge<Double, E> e : p1) {
							p1Sum += e.getLabel();
						}
						for (Edge<Double, E> e : p2) {
							p2Sum += e.getLabel();
						}*/
						if (p1Sum < p2Sum) {
							return -1;
						} else if (p1Sum > p2Sum) {
							return 1;
						} else {
							return 0;
						}
						/*Edge<Double, E> p1Dest = p1.get(p1.size() - 1);
						Edge<Double, E> p2Dest = p2.get(p2.size() - 1);
						return p1Dest.getLabel().compareTo(p2Dest.getLabel());*/
					}
				});
		
		List<Edge<Double, E>> startPath = new ArrayList<Edge<Double, E>>();
		startPath.add(new Edge<Double, E>(sNode, sNode, 0.0));
		workList.add(startPath);
		// while workList is non-empty:
		while (!workList.isEmpty()) {

			// lowest-cost path in workList
			List<Edge<Double, E>> minPath = workList.poll();

			// minDest = destination node in minPath
			Node<E> minDest = minPath.get(minPath.size() - 1).getEnd();

			if (minDest.equals(eNode)) {
				return minPath;
			}
			// if minDest is in finished: continue (jump to the next iteration)
			if (finished.contains(minDest)) {
				continue;
			}

			//double minDestWeight = minPath.get(minPath.size() - 1).getLabel();
			double minDestWeight = minPath.get(0).getLabel();
			HashSet<Edge<Double, E>> minDestChildren = dGraph.ListEdges(minDest);
			// for each edge e = ⟨minDest, child⟩:
			for (Edge<Double, E> e : minDestChildren) {
				// If we don't know the minimum-cost path from start to child,
				// examine the path we've just found
				if (!finished.contains(e.getEnd())) {
					//newPath = minPath + e
					double newPathWeight = minDestWeight + e.getLabel();
					//double newPathWeight = e.getLabel();
					// add newPath to workList
					List<Edge<Double, E>> newPath = new ArrayList<Edge<Double, E>>(minPath);
					//newPath.add(new Edge<Double, E>(e.getStart(), e.getEnd(), newPathWeight));
					
					//keep track of the total distance in the 0th index
					newPath.set(0, new Edge<Double, E>(sNode, sNode, newPathWeight));
					newPath.add(e);
					
					workList.add(newPath);
				}
			}
			//not sure if when i call this has an effect
			finished.add(minDest);
		}
		return null;
	}
	
	//make a class for a path that implements its own comparator to factor it out. having an object that has all my edges together and the total number
	//would be easier for me.
}
