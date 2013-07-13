import java.util.Arrays;


public class OptimizedRecurrence {
	private int[] col_val;
	private int[] col_items;
	private int[] values;
	private int[] weights;
	private int[] taken;
	private MyPair[] ordered;
	//private int[] lookup;
	private int items;
	private int capacity;
	private int bestValue = 0;
	
	public OptimizedRecurrence(int[] values, int[] weights, int capacity){
		this.values = values;
		this.weights = weights;
		this.items = values.length;
		this.capacity = capacity;
		
		ordered = new MyPair[items];
		//lookup = new int[items];
		taken = new int[items];
		col_val = new int[capacity+1];
		col_items = new int[capacity+1];
	}
	
	public int[] computeSolution(){
		sortByRatio();
		buildTable();
		return taken;
	}
	
	public int getValue(){
		return bestValue;
	}
	
	private void buildTable(){
		int with = 0;
		int without = 0;
		int itm; //zero based idx
		for (int j = 0; j < items; j++){
			itm = ordered[j].itemNum;
			int weight = weights[itm];
			int value = values[itm];
			//System.out.print("Col " + j + ": ");
			for (int k = capacity; k >= 0; k--){
				with = weight <= k ? value + col_val[k-weight] : 0;
				without = col_val[k];
				// does it fit and does it add value?
				if (weight <= k && with > without){
					col_val[k] = with;
					col_items[k] = itm;
				}
				//System.out.print(col_val[k] + " ");
			}
			//System.out.println();
		}
		int elt = capacity;
		itm = col_items[elt];
		while(itm > 0){
			taken[itm] = 1;
			bestValue += values[itm];
			elt -= weights[itm];
			itm = col_items[elt];
		}
	}
	
	private void sortByRatio() {
		float ratio;
		ordered = new MyPair[items];
		for (int i = 0; i < items; i++){
			ratio = ((float) values[i]) / ((float)weights[i]);
			ordered[i] = new MyPair(ratio, i);
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
