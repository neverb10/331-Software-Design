package hw5;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 * @author luntzel
 * 
 * A class for directed multigraphs, which are collections of nodes and
 * edges that allow any number of one-way edges between a given pair of
 * nodes.
 * 
 * Specification fields:
 * @specfield adjacencyList : HashMap // A map with a single node for keys and,
 * for values, a list of nodes that are connected by edges to the key
 *
 * Derived specification fields:
 *
 * Abstract Invariant: 
 * 
 */

//Additions: add a contains node method so you don't have to access the adjacency list and can make it private.
//rename place methods with add just in case it is messing up the script.
//add a method that checks if the a node's edge list contains an edge.
public class Graph<T extends Comparable<T>, E extends Comparable<E>> {
	public HashMap<Node<E>, HashSet<Edge<T, E>>> adjacencyList;
	public static final boolean DEBUG_FLAG = false;
	/**
	 * Abstraction Function: AF(this) = a directed multigraph represented by a
	 * collection of nodes and their outgoing edges.
	 * 
	 * Rep Invariant: for each node in adjacencyList.keySet(), node != null. For
	 * each edge in each list in adjacencyList.values(), the edge's nodes exist as a
	 * key and be non-null. adjacencyList != null.
	 * 
	 */

	/**
	 * @param map of nodes in the graph as keys and edges as values
	 * @effects sets the graph's adjacency list to the one passed in
	 */
	public Graph(HashMap<Node<E>, HashSet<Edge<T, E>>> adjacencyList) {
		this.adjacencyList = adjacencyList;
		checkRep();
	}
	
	/**
	 * @param node a node
	 * @requires node != null
	 * @effects adds the node into the graph if it does not already exist
	 * 
	 */
	public void placeNode(Node<E> node) {
		checkRep();
		if (!adjacencyList.containsKey(node)) {
			HashSet<Edge<T, E>> edges = new HashSet<Edge<T, E>>();
			adjacencyList.put(node, edges);
			//System.out.println("placed node " + node.getValue());
		}
		checkRep();
	}
	
	/**
	 * @param edge an edge made up of nodes
	 * @requires no e != null, e.start != null, e.end != null
	 * @effects adjacencyList by adding the edge if it does not exist already and the two nodes 
	 * are already in the graph, otherwise doing nothing
	 * 
	 */
	public void placeEdge(Edge<T, E> edge) {
		checkRep();
		Node<E> origin = edge.getStart();
		HashSet<Edge<T, E>> edges = adjacencyList.get(origin);
		if (!edges.contains(edge)) {
			edges.add(edge);
			adjacencyList.put(origin, edges);
			//System.out.println(edge.getStart().getValue() + " and node " + edge.getEnd().getValue() + " of edge " + edge.getLabel());
		}
		checkRep();
	}
	
	/**
	 * @param node a node to check if within graph
	 * @requires node rep invariant
	 * @returns true if the graph contains the node and false otherwise
	 * 
	 */
	public boolean hasNode(Node<E> node) {
		checkRep();
		return adjacencyList.containsKey(node);
	}
	
	/**
	 * @param edge an edge made up of nodes
	 * @requires e != null, e.start != null, e.end != null
	 * @returns true if the graph contains the edge and false otherwise
	 * 
	 */
	public boolean hasEdge(Edge<T, E> edge) {
		checkRep();
		return adjacencyList.get(edge.getStart()).contains(edge);
	}
	
	/**
	 * @return a set of node values in the graph
	 */
	public Set<E> ListNodes() {
		checkRep();
		Set<Node<E>> nodeList = adjacencyList.keySet();
		Set<E> orderedNodes = new HashSet<E>();
		for (Node<E> node : nodeList) {
			orderedNodes.add(node.getValue());
			//System.out.println(node.getValue());
		}
		return orderedNodes;
	}
	
	/**
	 * @param node a node with a value
	 * @requires graph contains parameter node
	 * @return a list of ordered outgoing edges from the node
	 */
	public HashSet<Edge<T, E>> ListEdges(Node<E> node) {
		checkRep();		
		HashSet<Edge<T, E>> children = adjacencyList.get(node);
		return children;
	}

	/**
	 * @requires e != null
	 * @modifies this
	 * @effects @effects determines if the graph rep invariant is upheld
	 */
	private void checkRep() {
		if (DEBUG_FLAG) {
		assert adjacencyList != null : "The graph's map must be non-null.";
		
		Iterator<HashMap.Entry<Node<E>, HashSet<Edge<T, E>>>> iter = adjacencyList.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry<Node<E>, HashSet<Edge<T, E>>> keyValue = iter.next();
			assert keyValue.getKey() != null : "Nodes must be non-null.";
			for (Edge<T, E> e : keyValue.getValue()) {
			    assert e != null : "Edge must be non-null.";
			    Node<E> destination = e.getEnd();
				if (!adjacencyList.containsKey(destination)) {
				     assert adjacencyList.containsKey(destination) : 
				    	 "Edge's nodes must exist in the graph";
				}
			}
		}
		}
	}
}
