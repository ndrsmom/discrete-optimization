import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.NavigableMap;

public class OptimizedBranchBound {
	private int[] values;
	private int[] weights;
	private int items;
	private int capacity;
	
	private int[] taken; // possibly just keep track of 1s if need more space
	private NavigableMap<Float, Integer> ordered;
	private int partial;
	private int partialVal;
	private float estimate;
	private int bestValue;

	public OptimizedBranchBound(int[] values, int[] weights, int items, int capacity){
		this.values = values;
		this.weights = weights;
		this.items = items;
		this.capacity = capacity;
		taken = new int[items];
		
		estimateForLinearRelaxation();
	}
	
	public int[] findSolution(){
		int weight = 0;
		int value = 0;
		float currEstimate = estimate;
		int item;
		for (Entry<Float, Integer> elt : ordered.entrySet()){
			item = elt.getValue();
			if (weight + value > capacity){
				if (item == partial) currEstimate -= partialVal;
				else currEstimate -= values[item];
			} else {
				taken[item] = 1;
				value += values[item];
				weight += weights[item];
			}
		}
		bestValue = value;
		return taken;
	}
	
	public int getValue(){
		return bestValue;
	}
	
	private void estimateForLinearRelaxation(){
		sortByRatio();
		estimate = 0;
		int used = 0;
		int item;
		for (Entry<Float, Integer> elt : ordered.entrySet()){
			item = elt.getValue();
			if (used + weights[item] <= capacity){
				estimate += values[item];
				used += weights[item];
			} else {
				// add partial item
				partial = item;
				float fraction = ((float) weights[item]) / ((float) (capacity - used));
				partialVal = (int)(values[item] / fraction);
				estimate += partialVal;
				used = capacity; // no more room
				break;
			}
		}
	}

	private void sortByRatio() {
		float ratio;
		
		TreeMap<Float, Integer> sorted = new TreeMap<Float, Integer>();
		for (int i = 0; i < items; i++){
			ratio = ((float) values[i]) / ((float)weights[i]);
			sorted.put(ratio, i);
		}
		ordered = sorted.descendingMap();
	}
}
