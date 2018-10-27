package hw8;

/**
 * Represents spatial coordinate-pair on a map
 * 
 * Specification Fields:
 * @specfield x: double // A point's horizontal component
 * @specfield y: double // A point's vertical component (y axis points downwards)
 * 
 * Derived Specification Fields:
 * @derivedspecfield orientation : String // given two points, the compass direction of the path
 */
public class CampusCoord implements Comparable<CampusCoord>{
	/**
	 * Abstraction Function: AF(this.x, this.y) = a coordinate-pair made up of a horizontal x
	 * and vertical y component to designate a location.
	 * 
	 * Rep Invariant: this.x && this.y != null 
	 */
	public Double x;
	public Double y;
	
	/**
	 * @param takes in two doubles
	 * @effects sets x to the first parameter and y to the second
	 */
	public CampusCoord(double x, double y) {
		this.x = x;
		this.y = y;
		checkRep();
	}
	
	/**
	 * @return the coordinate's vertical y value
	 */
	public double getY() {
		checkRep();
		return y;
	}
	
	/**
	 * @return the coordinate's horizontal x value
	 */
	public double getX() {
		checkRep();
		return x;
	}
	
	/**
	 *@param Object to check against
	 *@return true if this and o are equal, otherwise false
	 */
	public boolean equals(Object o) {
		checkRep();
		if (o instanceof CampusCoord) {
			CampusCoord c = (CampusCoord) o;
			return (c.y.equals(y) && c.x.equals(x));
			
		}
		return false;
	}
	
	/**
	 * @param other coordinate 
	 * @requires no null elements in either coordinate
	 * @return path direction given other as destination
	 */
	public String getDirection(CampusCoord other) {
		double theta = Math.atan2(other.getY() - this.getY(), other.getX() - this.getX());
		if (theta >= (Math.PI * -1) / 8 && theta <= Math.PI / 8) {
			return "E";
		} else if (theta > (Math.PI * -3) / 8 && theta < (Math.PI * -1) / 8) {
			return "NE";
		} else if (theta >= (Math.PI * -5) / 8 && theta <= (Math.PI * -3) / 8) {
			return "N";
		} else if (theta > (Math.PI * -7) / 8 && theta < (Math.PI * -5) / 8) {
			return "NW";
			//else if (theta > (Math.PI * 7) / 8 && theta <= (Math.PI * -7) / 8) {
		} else if (theta >= (Math.PI * 7) / 8 && theta <= (Math.PI * -7) / 8) {
			return "W";
		} else if (theta > (Math.PI * 5) / 8 && theta < (Math.PI * 7) / 8) {
			return "SW";
		} else if (theta >= (Math.PI * 3) / 8 && theta <= (Math.PI * 5) / 8) {
			return "S";
		} else {
			return "SE";
		}
	}
	
	/**
	 * @return a hash code for the CampusPoint object
	 */
	public int hashCode() {
		checkRep();
		return x.hashCode() + y.hashCode();
	}
	/**
	 * @effects determines if the campus coordinate rep invariant is upheld
	 */
	public void checkRep() {
		assert x != null : "x coordinate must be non-null.";
		assert y != null : "y coordinate must be non-null.";
	}

	public int compareTo(CampusCoord o) {
		return this.x.compareTo(o.x) + this.y.compareTo(o.y);
	}
}
