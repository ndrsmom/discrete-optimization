import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * The class <code>Solver</code> is an implementation of a greedy algorithm to solve the knapsack problem.
 *
 */
public class Solver {
	int[] values;
	int[] weights;
	int[] taken;
	int items;
	int capacity;
	int value;
	int weight;
    
    /**
     * The main class. Should be invoked with -file=filename option.
     */
    public static void main(String[] args) {
        try {
            new Solver().solve(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read the instance, solve it, and print the solution in the std output
     */
    public void solve(String[] args) throws IOException {
        // get the temp file name
        String fileName = getFileName(args);
        if(fileName == null)
            return;
        
        // read the lines out of the file
        List<String> lines = readLines(fileName);
        
        // parse the data in the file
        parseInput(lines);

        // solve, using an appropriate algorithm
        if (items < 20) dynamicProgramming();
        else branchAndBound();
        
        // output the solution
        printOutput();        
    }
    
    private void dynamicProgramming(){
    	// TODO
    }
    
    private void branchAndBound(){
    	// TODO
    }

	/**
	 * Prepare the solution in the specified output format.
	 */
	private void printOutput() {
		System.out.println(value+" 0");
        for(int i=0; i < items; i++){
            System.out.print(taken[i]+" ");
        }
        System.out.println("");
	}

	/**
	 * A trivial greedy algorithm for filling the knapsack.
     * It takes items in-order until the knapsack is full.
	 */
	private void solveTrivial() {
		value = 0;
        weight = 0;
        taken = new int[items];

        for(int i=0; i < items; i++){
            if(weight + weights[i] <= capacity){
                taken[i] = 1;
                value += values[i];
                weight += weights[i];
            } else {
                taken[i] = 0;
            }
        }
	}

	/**
	 * Parse the input data.
	 */
	private void parseInput(List<String> lines) {
		String[] firstLine = lines.get(0).split("\\s+");
        items = Integer.parseInt(firstLine[0]);
        capacity = Integer.parseInt(firstLine[1]);

        values = new int[items];
        weights = new int[items];

        for(int i=1; i < items+1; i++){
          String line = lines.get(i);
          String[] parts = line.split("\\s+");

          values[i-1] = Integer.parseInt(parts[0]);
          weights[i-1] = Integer.parseInt(parts[1]);
        }
	}

	/**
	 * Read the lines out of a file.
	 */
	private static List<String> readLines(String fileName)
			throws FileNotFoundException, IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader input =  new BufferedReader(new FileReader(fileName));
        try {
            String line = null;
            while (( line = input.readLine()) != null){
                lines.add(line);
            }
        }
        finally {
            input.close();
        }
        return lines;
	}

    /**
     * Get the provided file name.
     */
	private static String getFileName(String[] args) {
		String fileName = null;
		for(String arg : args){
            if(arg.startsWith("-file=")){
                fileName = arg.substring(6);
            } 
        }
		return fileName;
	}
}