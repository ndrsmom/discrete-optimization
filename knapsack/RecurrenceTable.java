package knapsack.java;

/**
 * A table of values computed using recurrence relations.
 * The columns represent items 0 through N, inclusive.
 * The rows represent capacity 0 through K, inclusive.
 * The table can be used to determine which items to
 * include in the knapsack.
 */
public class RecurrenceTable {
	int[][] table;
	int[] values;
	int[] weights;
	int[] taken;
	int items;
	int capacity;
	
	public RecurrenceTable(int[] values, int[] weights, int[] taken, int items, int capacity){
		this.values = values;
		this.weights = weights;
		this.taken = taken;
		this.items = items;
		this.capacity = capacity;
		
		table = new int[capacity+1][items+1];
	}
	
	/**
	 * Build the table. Compute the value for each item j at a capacity k. 
	 * Each column is computed assuming we know the values for the previous
	 * column (ie, by induction).
	 * 
	 * The process can be formalized by the equation:
	 * If wj <= k, O(k,j) = max( O(k,j-1) , vj + O(k-wj, j-1) )  
	 * otherwise O(k,j) = O(k,j-1)
	 */
	public void buildTable(){
		// First column is "item 0", where all values are 0.
		// This is necessary to provide a base for induction in the next step.
		for (int i = 0; i <= capacity; i++){
			table[i][0] = 0;
		}
		
		// Inductive step
		for (int j = 1; j <= items; j++){
			int weight = weights[j-1]; // lookup weights and values for the current item 
			int value = values[j-1]; //using j - 1, since these lists do not include "item 0"
			for (int k = 0; k <= capacity; k++){
				// if the weight of the current item is within capacity
				if (weight <= k){
					// then decide whether to leave the item or take it,
					// whichever will produce the maximum value
					table[k][j] = max(table[k][j-1], value + table[k-weight][j-1]);
				}
				// otherwise leave the item because it doesn't fit
				else{
					table[k][j] = table[k][j-1];
				}
			}
		}
	}
	
	private int max(int a, int b){
		if (a > b) return a;
		else return b;
	}
	
	/**
	 * Determine which items to add to the knapsack,
	 * based on whether value has increased in this column.
	 */
	public int[] traceBack(){
		// start at the bottom right of the table
		int k = capacity;
		for (int j = items; j > 0; j--){
			// if the current item has added a value greater than the previous
			if (table[k][j] > table[k][j-1]){
				// take the current item
				taken[j-1] = 1;
				k -= weights[j-1];
			} else {
				// otherwise do not take this item
				taken[j-1] = 0;
			}
		}
		return taken;
	}
}
