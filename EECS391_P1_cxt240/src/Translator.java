import java.io.*;


public class Translator {

	public static Puzzle solve_this;
	public static Solver my_solver;
	
	/**
	 * main statement of the translator class
	 * please enter a valid directory for the text file (double slash instead of single
	 * @param args arguments
	 * @throws Exception invalid file location
	 */
	public static void main(String[] args) throws Exception{
		
		try {
			//Translator.translate("C:\\Users\\Chris\\workspace\\EECS391_P1_cxt240\\src\\command.txt");
			Translator.translate(args[0]);
		}
		catch (IndexOutOfBoundsException c) {
			System.out.println("Give a valid txt file please.");
		}
		
	}
	
	/**
	 * Takes the command file and translates it into commands to be executed
	 * @param input the input text file directory
	 * @throws Exception null pointer exception if there is no solution to the search
	 */
	public static void translate(String input) throws Exception{
		
		// processes the input text file by line
		FileReader inputFile = new FileReader(input);
		BufferedReader read = new BufferedReader(inputFile);
		
		// NOTE: change this to the text file and directory that you want the output to go to
		FileWriter write = new FileWriter("C:\\Users\\Chris\\workspace\\EECS391_P1_cxt240\\src\\results.txt");
		
		// creates a solver and a puzzle to process
		solve_this = new Puzzle();
		my_solver = new Solver();
		
		// reads the file by line (commands must be exactly as specified in the instructions
		String command = new String();
		while((command = read.readLine()) != null) {
			
			if(command.contains("setState")) {
				String subString = command.substring((command.indexOf("setState") + 10), (command.indexOf("setState") + 21));
				
				solve_this.setState(subString);
			}
			else if(command.contains("randomizeState")) {
				String subString = command.substring((command.indexOf("randomizeState") + 15), command.length());
				
				int moves = Integer.parseInt(subString);
				solve_this.randomizeState(moves);
			}
			else if(command.contains("printState")) {
				
				write.write(solve_this.printState() + "\n");
			}
			else if(command.contains("move")) {
				String subString = command.substring((command.indexOf("move") + 6),command.length() - 1);
				
				if(subString.equals("up")) {
					solve_this.move(Puzzle.Direction.up);
				}
				else if(subString.equals("down")) {
					solve_this.move(Puzzle.Direction.down);
				}
				else if(subString.equals("right")) {
					solve_this.move(Puzzle.Direction.right);
				}
				else if(subString.equals("left")) {
					solve_this.move(Puzzle.Direction.left);
				}
				else {
					System.out.println("Invalid move");
				}
			}
			else if(command.contains("solve A-star")) {
				String subString = command.substring((command.indexOf("solve") + 14), command.length() - 1);

				try {
					my_solver.solve_this = solve_this;
					my_solver.solve_A_star(subString);
					
					write.write("Solving puzzle using A* heuristic " + subString + '\n');
					write.write(my_solver.complete_solution + '\n');
				}
				catch (Exception e) {
					write.write("A* search failed. Heuristic " + subString + '\n' + e.toString());
				}
			}
			else if(command.contains("solve beam")) {
				String subString = command.substring((command.indexOf("solve") + 12), command.length() - 1);
				
				try {
					my_solver.solve_this = solve_this;
					my_solver.solve_beam(Integer.parseInt(subString));
					
					write.write("Solving puzzle using beam search " + subString + '\n');
					write.write(my_solver.complete_solution + '\n');
				}
				catch (Exception e) {
					write.write("Beam search failed. Choosing " + subString + '\n' + e.toString());
				}
			}
			else if(command.contains("maxNodes")) {
				String subString = command.substring((command.indexOf("maxNodes") + 10), command.length() - 1);
				
				my_solver.maxNodes = Integer.parseInt(subString);
			}
		}
		inputFile.close();
		read.close();
		write.close();
		
	}

}
