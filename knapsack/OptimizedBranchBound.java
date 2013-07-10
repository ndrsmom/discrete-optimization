import java.util.Arrays;

public class OptimizedBranchBound {
	private int[] values;
	private int[] weights;
	private int items;
	private int capacity;
	
	private int[] taken; // possibly just keep track of 1s if need more space
	private MyPair[] ordered;
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
		MyPair elt;
		for (int i = 0; i < ordered.length; i++){
			elt = ordered[i];
			item = elt.itemNum;
			//System.out.println(values[item] + " " + weights[item] + " " + elt.getKey() + " " + elt.getValue());
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
		ordered = new MyPair[items];
		for (int i = 0; i < items; i++){
			ratio = ((float) values[i]) / ((float)weights[i]);
			ordered[i] = new MyPair(ratio, i);
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
			else if (this.ratio.compareTo(o.ratio) < 0) return -1;
			else return 1;
		}
		
	}
}
