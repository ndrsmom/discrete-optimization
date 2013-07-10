import java.util.LinkedList;
import static java.util.Arrays.asList;


public class BranchBoundTree {
	private int totalEstimate;
	private int best = -1;
	private LinkedList<Integer> solutionTakenItems;
	
	private int[] values;
	private int[] weights;
	private int items;
	private int capacity;
	
	private Node solution;

	public BranchBoundTree(int[] values, int[] weights, int items, int capacity){
		this.values = values;
		this.weights = weights;
		this.items = items;
		this.capacity = capacity;
		
		totalEstimate = estimateForRelaxedCapacity();
	}
	
	public int getValue(){
		return best;
	}
	
	public int[] findSolution(){
		buildTree(new Node(0, 0, 0, 0, 0, new LinkedList<Integer>()));
		assert (solution != null);
		return constructSolution();
	}
	
	private int[] constructSolution(){
		int[] taken = new int[items];
		for (Object elt : solutionTakenItems){
			taken[(int)elt-1] = 1;
		}
		return taken;
	}
	
	private void buildTree(Node current){
		boolean branched = branchOrBound(current);
		if (branched){
			// Clone forces use of type wildcards or you get a warning in the output
			LinkedList<Integer> left = new LinkedList<Integer>();
			left.addAll(current.takenItems);
			buildTree(new Node(
					current.height+1, 1, current.value, current.room,
					current.estimate, left));
			LinkedList<Integer> right = new LinkedList<Integer>();
			right.addAll(current.takenItems);
			buildTree(new Node(
					current.height+1, 0, current.value, current.room,
					current.estimate, right));
		}
	}
	
	/**
	 * Determine whether to branch or bound. Returns true if
	 * branch, false otherwise.
	 */
	private boolean branchOrBound(Node current){
		// check if ran out of room. if so, bound.
		if (current.room < 0) return false;
		
		// check if this is a first best or if we found a new best
		if (current.height == items &&
				(best == -1 || current.estimate >= best)){
			best = current.estimate;
			solutionTakenItems = current.takenItems;
			solution = current;
			return false;
		}
		
		// check if lower than best found value. if so, bound.
		if (best != -1 && current.estimate < best){
			return false;
		}
		
		// check if max height and we can't branch further
		if (current.height == items) return false;
		
		// otherwise branch
		return true;
	}
	
	private int estimateForRelaxedCapacity(){
		int est = 0;
		for (int i = 0; i < items; i++){
			est += values[i];
		}
		return est;
	}
	
	private class Node {
		int height;
		int value;
		int room;
		int estimate;
		LinkedList<Integer> takenItems;
		
		public Node(int height, int taken, int prevVal, int prevRoom, int prevEst, LinkedList<Integer> takenList){
			this.height = height;
			this.takenItems = takenList;
			
			// special base case for root
			if (height == 0){
				value = 0;
				room = capacity;
				estimate = totalEstimate;
			} 
			else {
				if (taken == 0){
					value = prevVal;
					room = prevRoom;
					estimate = prevEst - values[height-1];
				} else {
					takenItems.add(height);
					value = prevVal + values[height-1];
					room = prevRoom - weights[height-1];
					estimate = prevEst;
				}
			}
		}
	}
}
