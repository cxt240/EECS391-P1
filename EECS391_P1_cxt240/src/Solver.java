import java.util.*;

/**
 * Solver for the 8-puzzle
 * part of Project 1 for EECS 391 Fall 2016
 * @author Chris Tsuei
 */
public class Solver {

	public int maxNodes = 1000000;
	public static Puzzle solve_this;
	public String complete_solution;
	public int length = 0;
	
	public static void main(String[] args) {
		
		int misplaced_succeed = 0;
		int misplaced_moves = 0;
		
		int distance_succeed = 0;
		int distance_moves = 0;
		
		int beam_succeed = 0;
		int beam_moves = 0;
		
		Solver s = new Solver();
		for(int i = 0; i < 25; i++) {
			System.out.println("iteration: " + i);
			Puzzle test = new Puzzle();
			test.randomizeState(50 + i);
			Puzzle test1 = new Puzzle();
			test1.randomizeState(50 + i);
			Puzzle test2 = new Puzzle();
			test2.randomizeState(50 + i);
			
			try {
				System.out.println("misplaced" + i);
				s.misplaced_search(test);
				misplaced_succeed++;
				misplaced_moves += s.length;
				s.length = 0;
				System.out.println("misplaced done");

			}
			catch (Exception e) {

			}
			
			try {
				System.out.println("distance" + i);
				s.distance_search(test1);
				distance_succeed++;
				distance_moves += s.length;
				s.length = 0;
				System.out.println("distance done");

			}
			catch (Exception e) {

			}
			try {
				System.out.println("beam" + i);
				s.solve_this = test2;
				s.solve_beam(2);
				beam_succeed++;
				beam_moves += s.length;
				s.length = 0;
				System.out.println("beam done");

			}
			catch (Exception e) {

			}
		}
		System.out.println("done ");
		System.out.println(misplaced_succeed + " " + (double)(misplaced_moves) / misplaced_succeed);
		System.out.println(distance_succeed + " " + (double)(distance_moves) / distance_succeed);
		System.out.println(beam_succeed + " " + (double)(beam_moves) / beam_succeed);
	}
	
	/**
	 * solves the puzzle using A* search using a given heuristic
	 * either distance or misplaced squares
	 * @param heuristic the heuristic to be used. h1 is misplaced squares, h2 is distance to goal state
	 * @return the solution to the puzzle
	 */
	public String solve_A_star(String heuristic) {
		
		String solution = " ";
		if(heuristic.equals("h1")) {
			complete_solution = this.misplaced_search(solve_this);
		}
		else if(heuristic.equals("h2")) {
			complete_solution = this.distance_search(solve_this);
		}
		else {
			System.out.println("Please give a valid A* search function");
		}
		
		return solution;		
	}
	
	/**
	 * using the first heuristic (number of squares misplaced) to solve the 8-puzzle
	 * @param n puzzle to solve
	 * @return number of moves to solve
	 */
	public String misplaced_search(Puzzle n) {
		ArrayList<StateHolder> consider = new ArrayList<StateHolder> ();
		Hashtable<StateHolder,String> nodes = new Hashtable<StateHolder, String>();
		StateHolder solution = null;
		
		consider.add(new StateHolder(null, n.printState(), null, 0, this.misplaced(n.printState())));
		boolean found = false;
		
		// checks if a solution is found and the maximum number of nodes has been considered
		while(!found && nodes.size() < maxNodes && consider.size() > 0) {
			Collections.sort(consider);
			StateHolder current = consider.get(0);
			
			//check for a solution
			if(this.misplaced(current.state) == 0) {
				found = true;
				solution = current;
			}
			
			// checks for an already expanded state
			if(!nodes.contains(current.state) && !found) {
				nodes.put(current, current.state);
	
				//left
				Puzzle next = new Puzzle(current.state);
				boolean valid = next.move(Puzzle.Direction.left);
				if(valid) { // checks for a valid move
					int moves = current.moves + 1;
					int total = moves + this.misplaced(next.printState());
					StateHolder potential = new StateHolder(current, next.printState(), StateHolder.Direction.left, moves, total);
					boolean exists = consider.contains(potential);
					if(!exists) {
						consider.add(potential);
					}
				}					
			
				//right
				Puzzle next1 = new Puzzle(current.state);
				boolean valid1 = next1.move(Puzzle.Direction.right);
				if(valid1) { // checks for a valid move
					int moves = current.moves + 1;
					int total = moves + this.misplaced(next1.printState());
					StateHolder potential = new StateHolder(current, next1.printState(), StateHolder.Direction.right, moves, total);
					boolean exists = consider.contains(potential);
					if(!exists) {
						consider.add(potential);
					}
				}
				
				//up
				Puzzle next2 = new Puzzle(current.state);
				boolean valid2 = next2.move(Puzzle.Direction.up);
				if(valid2) { // checks for a valid move
					int moves = current.moves + 1;
					int total = moves + this.misplaced(next2.printState());
					StateHolder potential = new StateHolder(current, next2.printState(), StateHolder.Direction.up, moves, total);
					boolean exists = consider.contains(potential);
					if(!exists) {
						consider.add(potential);
					}
				}
				
				//down
				Puzzle next3 = new Puzzle(current.state);
				boolean valid3 = next3.move(Puzzle.Direction.down);
				if(valid3) { // checks for a valid move
					int moves = current.moves + 1;
					int total = moves + this.misplaced(next3.printState());
					StateHolder potential = new StateHolder(current, next3.printState(), StateHolder.Direction.down, moves, total);
					boolean exists = consider.contains(potential);
					if(!exists) {
						consider.add(potential);
					}
				}
			}
			// take current node off of arraylist
			consider.remove(0);
		}
		
		System.out.println(solution.state);
		String problem_solver = this.printSolution(solution);
		System.out.println("Solution: " + problem_solver);
		return problem_solver;
	}
	
	/**
	 * Using the second heuristic (total distance) to solve the 8-puzzle
	 * @param n the puzzle to be solved
	 * @return solution to the problem
	 */
	public String distance_search(Puzzle n) {
		ArrayList<StateHolder> consider = new ArrayList<StateHolder> ();
		Hashtable<StateHolder, String> nodes = new Hashtable<StateHolder, String>();
		StateHolder solution = null;
		
		consider.add(new StateHolder(null, n.printState(), null, 0, this.distance(n)));
		boolean found = false;
		
		// checks if a solution is found and the maximum number of nodes has been considered
		while(!found && nodes.size() < maxNodes) {
			Collections.sort(consider);
			StateHolder current = consider.get(0);
			
			//check for a solution
			if(this.distance(new Puzzle(current.state)) == 0) {
				found = true;
				solution = current;
			}
			
			// checks for an already expanded state
			if(!nodes.contains(current) && !found) {
				// checks if the move to the current state was right (so if it is don't undo with a left)
				if(current.direction != StateHolder.Direction.right) {
					Puzzle next = new Puzzle(current.state);
					boolean valid = next.move(Puzzle.Direction.left);
					if(valid) { // checks for a valid move
						int moves = current.moves + 1;
						int total = moves + this.distance(next);
						StateHolder potential = new StateHolder(current, next.printState(), StateHolder.Direction.left, moves, total);
						if(!consider.contains(potential)) {
							consider.add(potential);
						}	
					}
				}
				
				// checks if the move to the current state was left (so if it is don't undo with a right)
				if(current.direction != StateHolder.Direction.left) {
					Puzzle next = new Puzzle(current.state);
					boolean valid = next.move(Puzzle.Direction.right);
					if(valid) { // checks for a valid move
						int moves = current.moves + 1;
						int total = moves + this.distance(next);
						StateHolder potential = new StateHolder(current, next.printState(), StateHolder.Direction.right, moves, total);
						if(!consider.contains(potential)) {
							consider.add(potential);
						}				
					}
				}
				
				// checks if the move to the current state was up (so if it is don't undo with a down)
				if(current.direction != StateHolder.Direction.up) {
					Puzzle next = new Puzzle(current.state);
					boolean valid = next.move(Puzzle.Direction.down);
					if(valid) { // checks for a valid move
						int moves = current.moves + 1;
						int total = moves + this.distance(next);
						StateHolder potential = new StateHolder(current, next.printState(), StateHolder.Direction.down, moves, total);
						if(!consider.contains(potential)) {
							consider.add(potential);
						}				
					}
				}
				
				// checks if the move to the current state was down (so if it is don't undo with a up)
				if(current.direction != StateHolder.Direction.down) {
					Puzzle next = new Puzzle(current.state);
					boolean valid = next.move(Puzzle.Direction.up);
					if(valid) { // checks for a valid move
						int moves = current.moves + 1;
						int total = moves + this.distance(next);
						StateHolder potential = new StateHolder(current, next.printState(), StateHolder.Direction.up, moves, total);
						if(!consider.contains(potential)) {
							consider.add(potential);
						}				
					}
				}
				// expanded state already, add to hashtable
				nodes.put(current, current.state);
			}
			// take current node off of arraylist
			consider.remove(0);
		}
		System.out.println(solution.state);
		String problem_solver = this.printSolution(solution);
		System.out.println("Solution: " + problem_solver);
		return problem_solver;
	}

	/**
	 * Local beam search using k states considered at once
	 * I'm using the h2 heuristic to determine how "good a solution is
	 * @param k the number of nodes to be considered
	 * @return the solution to the puzzle
	 */
	public String solve_beam(int k) {
		ArrayList<StateHolder> consider = new ArrayList<StateHolder> ();
		Hashtable<String, StateHolder> nodes = new Hashtable<String, StateHolder>();
		StateHolder solution = null;
		
		consider.add(new StateHolder(null, solve_this.printState(), null, 0, this.misplaced(solve_this.printState())));
		boolean found = false;
		
		while(!found && nodes.size() < maxNodes) {
			
			Collections.sort(consider);
			
			//save the best k children, get rid of the rest
			if(consider.size() > k) {
				for(int j = k-1; j < consider.size(); j++) {
					consider.remove(k);
				}
				
			}
			StateHolder current = consider.get(0);

			// checks for an already expanded state
			if(!nodes.contains(current.state) && !found) {
				
				for(int i = 0; i < k; i++) {
					current = consider.get(0);
					
					//check for a solution
					if(this.misplaced(current.state) == 0) {
						found = true;
						solution = current;
					}
					
					// checks if the move to the current state was right (so if it is don't undo with a left)
					if(current.direction != StateHolder.Direction.right) {
						Puzzle next = new Puzzle(current.state);
						boolean valid = next.move(Puzzle.Direction.left);
						if(valid) { // checks for a valid move
							int total = this.distance(next);
							StateHolder potential = new StateHolder(current, next.printState(), StateHolder.Direction.left, 0, total);
							if(!consider.contains(potential) && !nodes.contains(potential)) {
								consider.add(potential);
							}		
						}
					}
					
					// checks if the move to the current state was left (so if it is don't undo with a right)
					if(current.direction != StateHolder.Direction.left) {
						Puzzle next = new Puzzle(current.state);
						boolean valid = next.move(Puzzle.Direction.right);
						if(valid) { // checks for a valid move
							int total = this.distance(next);
							StateHolder potential = new StateHolder(current, next.printState(), StateHolder.Direction.right, 0, total);
							if(!consider.contains(potential) && !nodes.contains(potential)) {
								consider.add(potential);
							}
						}
					}
					
					// checks if the move to the current state was up (so if it is don't undo with a down)
					if(current.direction != StateHolder.Direction.up) {
						Puzzle next = new Puzzle(current.state);
						boolean valid = next.move(Puzzle.Direction.down);
						if(valid) { // checks for a valid move
							int total = this.distance(next);
							StateHolder potential = new StateHolder(current, next.printState(), StateHolder.Direction.down, 0, total);
							if(!consider.contains(potential) && !nodes.contains(potential)) {
								consider.add(potential);
							}		
						}
					}
					
					// checks if the move to the current state was down (so if it is don't undo with a up)
					if(current.direction != StateHolder.Direction.down) {
						Puzzle next = new Puzzle(current.state);
						boolean valid = next.move(Puzzle.Direction.up);
						if(valid) { // checks for a valid move
							int total = this.distance(next);
							StateHolder potential = new StateHolder(current, next.printState(), StateHolder.Direction.up, 0, total);
							if(!consider.contains(potential) && !nodes.contains(potential)) {
								consider.add(potential);
							}				
						}
					}
					// expanded state already, add to hashtable
					nodes.put(current.state, current);
					consider.remove(0);
				}
			}
		}
		System.out.println(solution.state);
		String problem_solver = this.printSolution(solution);
		System.out.println("Solution: " + problem_solver);
		
		complete_solution = problem_solver;
		return problem_solver;
	}
	
	/**
	 * Sets the maximum number of nodes to be considered in a search
	 * @param n maximum specirfied number of nodes
	 */
	public void max_nodes(int n) {
		this.maxNodes = n;
	}

	/**
	 * calculate the number of tiles that are misplaced
	 * @param current state of the puzzle
	 * @return number of tiles that are misplaced
	 */
	public int misplaced(String current) {
		
		int misplaced = 0;
		String solved = "b12 345 678";
		
		// iterates through the solved and current strings ignoring b and ' '
		for(int i = 0; i < solved.length(); i++) {
			if((current.charAt(i) != ('b')) && (current.charAt(i) != ' ' )) {
				if(current.charAt(i) != solved.charAt(i)) {
					misplaced++;
				}
			}
		}		
		return misplaced;
	}

	/**
	 * calculate the total distance for all puzzle squares from the solution
	 * @param current the current puzzle state
	 * @return total sum of distances for each square from its solution
	 */
	public int distance(Puzzle current) {
		
		Puzzle solution = new Puzzle("b12 345 678");
		int distance = 0;
		
		// iterating through the current puzzle
		for(int current_row = 0; current_row < 3; current_row++){
			for(int current_column = 0; current_column < 3; current_column++) {
				
				// if the value at that point isn't 0
				if(current.board[current_column][current_row] != 0) {
					
					// iterate through the solution array for a matching value
					for(int solution_row = 0; solution_row < 3; solution_row++) {
						for(int solution_column = 0; solution_column < 3; solution_column++) {
							
							// if there is a match, find the distance to the solution and exit loop
							if(current.board[current_column][current_row] == solution.board[solution_column][solution_row]) {
								distance += Math.abs(solution_column - current_column) + Math.abs(solution_row - current_row);
								solution_row = 4;
								solution_column = 4;
							}
						}
					}
				}
				
			}
		}
		return distance;
	}

	/**
	 * prints the solution of the puzzle
	 * traces the path of the solution to the parent node
	 * 
	 * @param solved final state of the solution
	 * @return the step by step solution from the randomized puzzle
	 */
	public String printSolution(StateHolder solved) {
		
		length = 0;
		StringBuilder print = new StringBuilder();
		StateHolder current = solved;
		while(current.parent != null) {
			String move = new String();
			if(current.direction == StateHolder.Direction.right) {
				move = "right";
			}
			else if(current.direction == StateHolder.Direction.left) {
				move = "left";
			}
			else if(current.direction == StateHolder.Direction.up) {
				move = "up";
			}
			else if(current.direction == StateHolder.Direction.down) {
				move = "down";
			}
			current = current.parent;
			length++;
			print.append("\n" + new StringBuilder(move).reverse());
		}
		return print.reverse().toString();
	}
}
