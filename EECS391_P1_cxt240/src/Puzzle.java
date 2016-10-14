import java.util.Random;

/**
 * The 8-puzzle class and related methods
 * Part of the Fall 2016 AI project1
 * @author Chris Tsuei
 */
public class Puzzle {

	public int[][] board = new int [3][3];
	public enum Direction {up, down, left, right};
	public int seed = 240;
	
	// location of the blank spot (or 0)
	public int blank_row = 0;
	public int blank_column = 0;
	
	public static void main(String[] args) {

		Puzzle start = new Puzzle("b12 345 678");		
		String get = start.printState();
		System.out.println(get);
		System.out.println((start.blank_row + 1) + ", " + (start.blank_column + 1));
		
		start.randomizeState(50);
		get = start.printState();
		System.out.println((start.blank_row + 1) + ", " + (start.blank_column + 1));
	}

	/**
	 * Takes in a param string
	 * @param state current state of the puzzle
	 */
	public Puzzle(String state) {
		this.setState(state);
	}

	/**
	 * Default constructor for the Puzzle board
	 * takes no input and puts the goal state
	 * 0 is the blank spot
	 */
	public Puzzle() {
		this.setState("b12 345 678");
	}
	
	/**
	 * Sets the initial state of the board
	 * assumes that the user enters in the correct single digit values for the board
	 * state (values 0-8, b is blank (value on board = 0))
	 * @param state state string of length 10 specified by a command line prompt 
	 */
	public void setState(String state) {
		
		int offset = 0;
		//iterates through the state string and sets the board to a specified state
		for(int row = 0; row < board.length; row++) {
			for(int column = 0; column < board.length; column++) {
				
				// if the char at the point is a space, move the pointer to the next char
				if(state.charAt(row + column + offset) == ' '){
					offset++;
				}
				
				// checks if the char at the pointer is not a 'b'
				// otherwise replaces with a 0
				if(state.charAt(row + column + offset) != 'b') { 
					char c = state.charAt(row + column + offset);
					board[column][row] = Character.getNumericValue(c);
					
				}
				else {
					board[column][row] = 0;
					this.blank_column = column;
					this.blank_row = row;
				}
				
				if(column == 2) {
					offset += 2;
				}
			}
		}
	}
	
	/**
	 * Randomizes the default puzzle board
	 * @param n the number random moves to be made from the goal state
	 */
	public void randomizeState(int n) {
		Random rand_gen = new Random(seed);	// create new random generator and seeds it
		int move_count = 0;
		/* Random number generator generates an integer and it determines the move
			1: up
			2: down
			3: right
			4: left
		*/
		while (move_count < n) {
			int moves = rand_gen.nextInt(4) + 1;
			boolean valid = false;
			
			if (moves == 1) {
				valid = this.move(Direction.up);
			}
			else if (moves == 2) {
				valid = this.move(Direction.down);
			}
			else if (moves == 3) {
				valid = this.move(Direction.right);
			}
			else if (moves == 4) {
				valid = this.move(Direction.left);
			}
			
			if(valid) {
				move_count++;
			}
		}
	}
	
	/**
	 * prints the current state of the board
	 * 0 is a blank spot
	 * @return String state of the puzzle
	 */
	public String printState() {
		
		StringBuilder state = new StringBuilder();
		//iterates throught the board adding numbers to the string
		for(int row = 0; row < board.length; row++) {
			for(int column = 0; column < board.length; column++) {
				//Checks for a zero
				if(board[column][row] != 0) {
					state.append(board[column][row]);
				}
				else {
					state.append('b');
				}
				
				// end of column add a space as a divider
				if(column == 2 && row != 2) {
					state.append(" ");
				}
			}
		}
		return state.toString();
	}
	
	/**
	 * moves the blank spot up, down, left, or right
	 * checks whether the movement is valid first, if not prints out an error
	 * also specifies where the updated blank spot is if the move was valid
	 * @param direction up or down, left or right
	 * @return if the move was valid or not
	 */
	public boolean move(Direction direction) {
		switch(direction) {
		case up:
			if(this.blank_row == 0) {
				return false;
			}
			else {
				board[this.blank_column][this.blank_row] = board[this.blank_column][this.blank_row - 1];
				board[this.blank_column][this.blank_row - 1] = 0;
				this.blank_row -= 1;
				return true;
			}
		case down:
			if(this.blank_row == 2) {
				return false;
			}
			else {
				board[this.blank_column][this.blank_row] = board[this.blank_column][this.blank_row +1];
				board[this.blank_column][this.blank_row + 1] = 0;
				this.blank_row += 1;
				return true;
			}
		case right:
			if(this.blank_column == 2) {
				return false;
			}
			else {
				board[this.blank_column][this.blank_row] = board[this.blank_column + 1][this.blank_row];
				board[this.blank_column + 1][this.blank_row] = 0;
				this.blank_column += 1;
				return true;
			}
		case left:
			if(this.blank_column == 0) {
				return false;
			}
			else {
				board[this.blank_column][this.blank_row] = board[this.blank_column - 1][this.blank_row];
				board[this.blank_column - 1][this.blank_row] = 0;
				this.blank_column -= 1;
				return true;
			}
		default:
			return false;
			
		}
	}
}