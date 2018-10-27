package hw5;


/**
 * @author luntzel
 *
 * A class for nodes, which are vertices in a graph.
 * 
 * Specification fields:
 * @specfield value: String //the value of the node
 * 
 * Abstract Invariant:
 * Non-null fields only in the node
 * 
 */
public class Node<E extends Comparable<E>> implements Comparable<Node<E>> {
	private final E value;

	/**
	 * Abstraction Function: AF(this) = a node with node.value = value
	 * 
	 * Rep invariant: value != null
	 */

	/**
	 * 
	 * @param value the value of the node
	 * @modifies this
	 * @effects Sets the node's value
	 */
	public Node(E value) {
		this.value = value;
		checkRep();
	}

	/**
	 * 
	 * @return the node's value
	 */
	public E getValue() {
		checkRep();
		return value;
	}

	/**
	 * @returns hashCode for a node as determined by value
	 */
	public int hashCode() {
		return value.hashCode();
	}

	/**
	 * @param Object
	 *            (overrides) to compare
	 * @returns false if this node and o are unequal, and true otherwise
	 */
	public boolean equals(Object o) {
		if (o instanceof Node<?>) {
			return ((Node<?>) o).getValue().equals(this.getValue());
		}
		return false;
	}

	@Override
	public int compareTo(Node<E> o) {
		return this.getValue().compareTo(o.getValue());
	}

	/**
	 * @effects determines if the node rep invariant is upheld
	 */
	private void checkRep() {
		assert value != null : "value must be non-null.";
	}
}
