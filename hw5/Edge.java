package hw5;


/**
 * @author luntzel
 *
 * A class for one-way edges, which are connections between two nodes.
 * 
 * Specification fields:
 * @specfield origin: Node<String> //the edge's initial node
 * @specfield destination: Node<String>  //the edge's terminating node
 * @specfield label: String //identifier associated with the edge
 * 
 * Derived specification fields:
 * @derivedfield child: Node<String> //a node stemming from another node
 * @derivedfield parent: Node<String> //a node with one or more nodes stemming from it
 * 
 * Abstract Invariant: 
 * Non-null nodes and label
 * 
 */
//make it generic
public class Edge<T extends Comparable<T>, E extends Comparable<E>> {

	private final Node<E> origin;
	private final Node<E> destination;
	private final T label;

	/**
	 * Abstraction Function: AF(start, end, label) = an edge with node start, node
	 * end, and label
	 * 
	 * Rep invariant: origin != null, destination != null, label != null
	 */

	/**
	 * @requires origin != null && destination != null
	 * @param initial node origin, terminating node destination, and edge label
	 * @effects Sets up the edge's nodes and label
	 */
	public Edge(Node<E> origin, Node<E> destination, T label) {
		this.origin = origin;
		this.destination = destination;
		this.label = label;
	}

	/**
	 * @requires !label.equals(null)
	 * @return the edge's label
	 */
	public T getLabel() {
		checkRep();
		return label;
	}

	/**
	 * @requires !origin.equals(null)
	 * @return start node
	 */
	public Node<E> getStart() {
		return origin;
	}

	/**
	 * @requires !destination.equals(null)
	 * @return end node
	 */
	public Node<E> getEnd() {
		return destination;
	}
	
	/**
	 * @returns hashCode for an edge as determined by node and label codes
	 */
	public int hashCode() {
		return  this.getStart().hashCode() + this.getEnd().hashCode() + label.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof Edge) {
			Edge<?,?> other = (Edge<?,?>) o;
			return (this.getLabel().equals(other.getLabel()) && this.getStart().equals(other.getStart())
					&& this.getEnd().equals(other.getEnd()));
		}
		return false;
	}
	
	

	/**
	 * @effects determines if the edge rep invariant is upheld
	 */
	private void checkRep() {
		assert origin != null : "start node must be non-null.";
		assert destination != null : "end node must be non-null.";
		assert label != null : "label must be non-null.";
	}
}
