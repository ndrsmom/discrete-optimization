
public class BranchBounder {
	int value = 0;
	int estimate;
	int[] values;
	int[] weights;
	int items;
	int capacity;
	int[] taken;

	public BranchBounder(int[] values, int[] weights, int capacity, int items){
		this.values = values;
		this.weights = weights;
		this.items = items;
		this.capacity = capacity;
		
		estimate = computeSimpleEstimate();
	}
	
	public void startBranch(){
		branchAndBoundHelper(1, true, new int[items], 0, capacity, estimate);
		branchAndBoundHelper(1, false, new int[items], 0, capacity, estimate);
	}
	
	// ****** Optimization, use tree with nodes to represent ?
	private void branchAndBoundHelper(
			int j, boolean chosen, int[] path, int prevVal, int prevRoom, int prevEst
			){
		int val = prevVal;
		int room = prevRoom;
		int est = prevEst;
		
		// compute values for current node
		if (chosen){
			val += values[j-1];
			room -= weights[j-1];
		}
		else {
			est -= values[j-1];
		}
		
		// bound if possible
		if (room < 0)
		
		// check if solution
		if (j == items){
			
		}
		
		
	}
	
	private int computeSimpleEstimate(){
		int est = 0;
		for (int i = 0; i < items; i++){
			est += values[i];
		}
		return est;
	}
}
