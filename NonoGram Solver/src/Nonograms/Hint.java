package Nonograms;

import java.util.ArrayList;
import java.util.HashMap;

public class Hint {

	
	//Properties
	
	//Which row or column does the hint belong to
	public int row = -1;
	public int col = -1;
	//Each hint maps the integer to a possible interval
	//Length of corresponding block
	public int val;
	//Length of potential placement
	public Interval interval;
	
	public boolean isSolved;
	public ArrayList<Integer> potentialBoxes;
	
	//TODO handle hint association
	public boolean isDistinctHint;
	public boolean isLargestHint;
	
	//Constructor
	public Hint(int index, String rowOrCol) {
		if(rowOrCol.equals("r")) {
			this.row = index;
		}
		else {
			this.col = index;
		}
		this.isSolved = false;
	}
	
	
	public boolean checkSolved() {
		if(this.interval.indicies.size() == this.val) {
			this.isSolved = true;
			return true;
		}
		return false;
	}
	
	public void updateInterval(ArrayList<Integer> filled, ArrayList<Integer> notFilled, int boardRows, int boardCols) {
		//val is 5 in this case
		//Given index 2, 3 are filled
		int first;
		int last;
		//no new information was found
		if(filled.isEmpty()) {
			this.interval.indicies.removeAll(notFilled);
			return;
		}
		else {
			first = filled.get(0);
			last = filled.get(filled.size() - 1);
		}

		int blockSize = last - first + 1;
		int blocksLeft = val - blockSize;
		int right = last + blocksLeft;
		int left = first - blocksLeft;
		if(right >= boardRows - 1) {
			right = boardRows - 1;
		}
		if(left < 0) {
			left = 0;
		}
		this.interval.start = left;
		this.interval.end = right;
		this.interval.indicies.clear();
		for(int i = left; i <= right; i++) {
			this.interval.indicies.add(i);
		}
		//This will cause a split interval
		for(int index : notFilled) {
			this.interval.indicies.remove(new Integer(index));
		}
		int count = 0;
		int prev = this.interval.indicies.get(0);
		ArrayList<Integer> toRemove = new ArrayList<>();
		for(int index : this.interval.indicies) {
			//Consecutive
			if(index - prev <= 1) {
				count++;
			}
			else {
				if(count < this.val) {
					for(int i = 0; i <= prev; i++) {
						toRemove.add(new Integer(i));
						//this.interval.indicies.remove(new Integer(i));
					}
				}
				count = 1;
			}
			prev = index;
		}
		this.interval.indicies.removeAll(toRemove);
		this.interval.start = this.interval.indicies.get(0);
		this.interval.end = this.interval.indicies.get(this.interval.indicies.size() - 1);
		
	}
	
	//Returns array of indices of boxes that need to be filled
	public ArrayList<Integer> getGuaranteed(){
		ArrayList<Integer> result = new ArrayList<>();
		//Number of guaranteed
		int guaranteed = 2*this.val - this.interval.length();
		
		int notGuaranteed = this.interval.length() - guaranteed;
		int start = (notGuaranteed/2) + this.interval.start;
		int end = start + guaranteed - 1;
		
		for(int i = start; i <= end; i++) {
			result.add(i);
		}
		
		return result;
	}
	
	//Returns an array of hint objects
	//Specify if row or column with string "r" or "c"
	public static ArrayList<Hint> getHints(ArrayList<Integer> array,int index, String rowOrCol, int rows, int cols) {
		
		//this could be wrong
		int length;
		if(rowOrCol.equals("r")) {
			length = rows;
		}
		else {
			length = cols;
		}
		
		ArrayList<Hint> result = new ArrayList<>();
		int totalSize = getTotalSize(array);
		int start = 0;
		for(int i = 0; i < array.size(); i++) {
			Hint hint = new Hint(index, rowOrCol);
			int shift = length - totalSize;
			Interval interval = new Interval(start, start + shift + array.get(i) - 1 );			
			hint.val= array.get(i);
			hint.interval = interval;
			result.add(hint);
			start += array.get(i) + 1;
		}
		
		
		return result;
	}
	
	
	//Get total block size occupied
	public static int getTotalSize(ArrayList<Integer> nums) {
		int result = 0;
		for(Integer num : nums) {
			result+=num;
		}
		result += nums.size() - 1;
		return result;
	}
	
	public String toString() {
		return Integer.toString(val);
	}
}
