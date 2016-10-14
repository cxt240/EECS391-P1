import java.util.Objects;

/**
 * Stores the state of the puzzle
 * @author Chris Tsuei
 */
public class StateHolder implements Comparable<StateHolder> {
	
	public String state;
	public StateHolder parent;
	public enum Direction {up, down, left, right};
	public Direction direction;
	int moves;
	int total;
	
	/**
	 * Constructor for the StateHolder class
	 * this node will store the puzzle as well as the parent node
	 * @param parent parent of this node
	 * @param State state string of this puzzle
	 */
	public StateHolder(StateHolder parent, String State, Direction direction, int moves, int total) {
		
		this.parent = parent;
		this.state = State;
		this.direction = direction;
		this.moves = 0;
		this.total = total;
	}
	
	/**
	 * writing the comparable method
	 * @param compare the node we're comparing this node to
	 * @return result of comparison
	 */
	public int compareTo(StateHolder compare) {
		return (this.total - compare.total);
	}
	
	/**
	 * @Override
	 * Overriding the .equals method
	 * @param o the StateHolder or object this node is being compared to
	 * @return whether the nodes contain the same state, false if the object is not a node
	 */
	public boolean equals(Object o) {
		if(o.getClass() != getClass()) {
			return false;
		}

		StateHolder compare = (StateHolder)o;

		String comparison = compare.state;
		comparison.replaceAll(" ", "");
		String current = state;
		current.replaceAll(" ", "");
		
		return comparison.contains(current);
	}
}