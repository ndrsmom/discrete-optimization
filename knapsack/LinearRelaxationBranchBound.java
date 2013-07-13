import java.util.Arrays;
import java.util.ArrayList;

public class LinearRelaxationBranchBound {
	private int[] values;
	private int[] weights;
	private int items;
	private int capacity;
	
	private int[] bestSolution; // possibly just keep track of 1s if need more space
	private MyPair[] ordered;
	private int estimate;
	private int bestEstimate;
	private int totalValue;

	public LinearRelaxationBranchBound(int[] values, int[] weights, int items, int capacity){
		this.values = values;
		this.weights = weights;
		this.items = items;
		this.capacity = capacity;
		bestSolution = new int[items];
		
		estimateForLinearRelaxation();
	}
	
	public int[] findSolution(){
		for (int i = 0; i < 10; i++){
			System.out.println(values[ordered[i].itemNum] + " " + weights[ordered[i].itemNum] 
					+ " " + ordered[i].ratio);
		}
		bestEstimate = -1;
		recursiveBranch(true, 0, new int[items], estimate, capacity, 0);
		recursiveBranch(false, 0, new int[items], estimate, capacity, 0);
		return bestSolution;
	}
	
	private void recursiveBranch(boolean taken, int orderedIdx, int[] solution, int currentEst, int currentRoom, int total){
		int item = ordered[orderedIdx].itemNum;
		if (taken) solution[item] = 1;
		else solution[item] = 0;
		int room = taken ? currentRoom - weights[item] : currentRoom;
		int value = taken ? currentEst : currentEst - values[item];
		if (taken) total += values[item];
		
		System.out.println("------------------------------------");
		System.out.println("Taken: " + taken);
		System.out.println("OrderedIdx: " + orderedIdx);
		System.out.println("Item No: " + item);
		System.out.println("Room: " + room);
		System.out.println("Value: " + value);
		for (int i = 0; i < solution.length; i++){
			System.out.print(solution[i] + " ");
		}
		System.out.println();
		
		// check if node should be pruned
		if (room < 0 || value < bestEstimate){ System.out.println("****PRUNING****"); return; }
		
		// check if we're at the end of the elts
		if (orderedIdx == ordered.length-1){
			if (value > bestEstimate){
				bestEstimate = value;
				bestSolution = solution;
				totalValue = total;
				System.out.println("****FOUNDSOLUTION****");
			}
		} else {
		// otherwise keep branching
			orderedIdx++;
			recursiveBranch(true, orderedIdx, solution.clone(), value, room, total);
			recursiveBranch(false, orderedIdx, solution.clone(), value, room, total);
		}
	}
	
	public int getValue(){
		return totalValue;
	}
	
	private void estimateForLinearRelaxation(){
		sortByRatio();
		estimate = 0;
		int used = 0;
		int item;
		MyPair elt;
		for (int i = 0; i < ordered.length; i++){
			elt = ordered[i];
			item = elt.itemNum;
			//System.out.println(values[item] + " " + weights[item] + " " + elt.ratio + " " + elt.itemNum);
			if (used + weights[item] <= capacity){
				estimate += values[item];
				used += weights[item];
			} else {
				// add partial item
				float fraction = ((float) weights[item]) / ((float) (capacity - used));
				int partialVal = (int)(values[item] / fraction);
				estimate += partialVal;
				used = capacity; // no more room
				break;
			}
		}
	}

	private void sortByRatio() {
		float ratio;
		ordered = new MyPair[items];
		for (int i = 0; i < items; i++){
			ratio = ((float) values[i]) / ((float)weights[i]);
			ordered[i] = new MyPair(ratio, i+1);
			//System.out.println(values[i] + " " + weights[i] + " " + ratio + " " + i);
		}
		Arrays.sort(ordered);
	}
	
	private static class MyPair implements Comparable<MyPair> {
		final Float ratio;
		final Integer itemNum;
		
		public MyPair(Float left, Integer right){
			this.ratio = left;
			this.itemNum = right;
		}
		
		@Override
		public int hashCode(){ return ratio.hashCode() ^ itemNum.hashCode(); }
		
		@Override
		public boolean equals(Object o){
			if (o == null) return false;
			if (!(o instanceof MyPair)) return false;
			MyPair other = (MyPair) o;
			return this.ratio.equals(other.ratio) && this.itemNum.equals(other.itemNum);
		}

		@Override
		public int compareTo(MyPair o) {
			if (this.ratio.compareTo(o.ratio) == 0) return 0;
			else if (this.ratio.compareTo(o.ratio) < 0) return 1;
			else return -1;
		}
		
	}
}
