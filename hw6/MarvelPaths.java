package hw6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import hw5.Edge;
import hw5.Graph;
import hw5.Node;
import hw6.MarvelParser.MalformedDataException;
/**
 * @author luntzel
 * 
 * A class for graphs between comic book characters based on their related book appearances.
 * Allows bidirectional labeled multigraphs without reflexive edges. 
 * 
 * Specification fields:
 *
 * Derived specification fields:
 *
 * Abstract Invariant: 
 * 
 */
public class MarvelPaths {
	/**
	 * Abstraction Function: AF(this) = a directed multigraph represented by a
	 * collection of nodes labeled as characters, and their outgoing edges, 
	 * which are labeled by book names.
	 * 
	 * Rep Invariant: for each node in the file, node != null. For
	 * each edge, the edge's nodes exist as a key to their start node and be non-null.
	 * 
	 */
	
	/**
	 * Takes in user data to find a path of their choice.
	 * @param args
	 * @effects prints the path result to the user
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Scanner c = new Scanner(System.in);
		System.out.println("Which graph?");
		try {
			String graph = c.nextLine();
			System.out.println("Which start node?");
			String sNode = c.nextLine();
			System.out.println("Which end node?");
			String eNode = c.nextLine();
			// /src?
			Graph<String, String> g = MarvelPaths.loadGraph("src/hw6/data/" + graph);
			List<Edge<String, String>> path = MarvelPaths.findPath(g, sNode, eNode);
			String pathPrint = path.get(0).getStart().getValue();
			if (path != null) {
				for (Edge<String, String> e : path) {
					pathPrint += ", " + e.getEnd().getValue();
				}
			} else {
				System.out.println("No path.");
			}
		} catch (MalformedDataException m) {
			System.out.println("Malformed filename.");
		}
		c.close();
	}
	
	/**
	 * Creates a graph based on a file, deciding nodes and edges based on the file format
	 * 
	 * @param filename file to be parsed and translated
	 * @return graph with characters connected to each other if they occurr in the same book
	 * @throws MalformedDataException
	 */
	public static Graph<String, String> loadGraph(String filename) throws MalformedDataException {
		if (filename == null) {
			throw new IllegalArgumentException("file name must be non-null.");
		}

		HashMap<Node<String>, HashSet<Edge<String, String>>> marvelAdjList = new HashMap<Node<String>,
				HashSet<Edge<String, String>>>();
		Graph<String, String> marvelGraph = new Graph<String, String>(marvelAdjList);

		Set<String> characters = new HashSet<String>();
		Map<String, List<String>> books = new HashMap<String, List<String>>();
		MarvelParser.parseData(filename, characters, books);

		// while there are books, loop over characters, loop over characters again, make
		// edges between char w/ book for each char
		Set<String> bookSet = books.keySet(); // a set of books
		Iterator<String> itr = bookSet.iterator();

		//nodes start and end must exist in the graph: this is where i am adding nodes into the graph.
		for (String s : characters) {
			marvelGraph.placeNode(new Node<String>(s));
		}
		
		while (itr.hasNext()) { // while there are still books to iterate over
			String book = itr.next();
			List<String> bookCharacters = books.get(book); 
			for (int i = 0; i < bookCharacters.size(); i++) { 
				String name = bookCharacters.get(i);
				Node<String> firstName = new Node<String>(name);

				for (int j = 0; j < bookCharacters.size(); j++) {
					String other = bookCharacters.get(j);
					Node<String> nextName = new Node<String>(other);
					Edge<String, String> e = new Edge<String, String>(firstName, nextName, book);
					// not reflexive and is bidirectional
					if (!marvelGraph.hasEdge(e) && !firstName.equals(nextName)) {
						marvelGraph.placeEdge(e); // add the edge into the list
					}
				}
			}
		}
		//return new Graph<String>(marvelAdjList);
		return marvelGraph;
	}

	/**
	 * Finds the shortest path between two given comic book characters
	 * 
	 * @param graph to be checked, starting string to find path from, 
	 * and ending string to find shortest path to
	 * @throws IllegalArgumentException if the graph or strings are null, or if the graph does not
	 * contain both nodes
	 * @return a list of edges that contains the shortest path
	 */
	public static List<Edge<String, String>> findPath(Graph<String, String> marvelGraph,
			String start, String end) {
		if (start == null || end == null) {
			throw new IllegalArgumentException("start and end cannot be null.");
		}

		if (marvelGraph == null) {
			throw new IllegalArgumentException("graph must not be null.");
		}

		// make a method for this
		if (!(marvelGraph.adjacencyList.containsKey(new Node<String>(start)))
				|| !(marvelGraph.adjacencyList.containsKey(new Node<String>(end)))) {
			throw new IllegalArgumentException("Nodes start and end must exist in the graph.");
		}

		// starting and ending nodes
		Node<String> sNode = new Node<String>(start);
		Node<String> eNode = new Node<String>(end);
		// map of node keys to path values, and worklist
		Map<Node<String>, List<Edge<String, String>>> paths = new HashMap<Node<String>,
				List<Edge<String, String>>>();
		Queue<Node<String>> workList = new LinkedList<Node<String>>();

		workList.add(sNode);
		paths.put(sNode, new ArrayList<Edge<String, String>>());
		Node<String> visitNode;
		//workList.remove();
		// while the queue still has values to visit
		//one error here
		while (!workList.isEmpty()) {
			visitNode = workList.remove();
			if (visitNode.equals(eNode)) {
				// return the path that has reached the end node
				return paths.get(visitNode);
			}
			// otherwise visit the next node
			//visitNode = workList.remove();
			Set<Edge<String, String>> lexEdges = new TreeSet<Edge<String, String>>((e, e2) -> {
				if (!(e.getEnd().equals(e2.getEnd()))) {
					return e.getEnd().compareTo(e2.getEnd());
				}
				if (!(e.getLabel().equals(e2.getLabel()))) {
					return e.getLabel().compareTo(e2.getLabel());
				}
				return 0;
			});
			// BFS checks the current edges proximal edges. We store all the children in
			// lexicographical order to visit them.
			lexEdges.addAll(marvelGraph.ListEdges(visitNode));
			// for each edge visit node to proximal node
			for (Edge<String, String> e : lexEdges) {
				String endLabel = e.getEnd().getValue();
				Node<String> proxNode = new Node<String>(endLabel);
				// if the proximal node is not in the map (unvisited)
				if (!(paths.containsKey(proxNode))) {
					// let p be the path the visit node is mapped to
					List<Edge<String, String>> p = paths.get(visitNode);
					// let pP be the path formed by appending e to p
					List<Edge<String, String>> pP = new ArrayList<Edge<String, String>>(p);
					pP.add(e);
					// add m->pP to the map of paths
					paths.put(proxNode, pP);
					// add to visited nodes
					workList.add(proxNode);
				}
			}
		}
		return null;
	}
	

}
