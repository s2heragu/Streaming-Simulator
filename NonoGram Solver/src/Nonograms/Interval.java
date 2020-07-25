package Nonograms;

import java.util.ArrayList;

public class Interval {

	public int start;
	public int end;
	public ArrayList<Integer> indicies;
	
	//Constructor
	public Interval(int start, int end) {
		this.start = start;
		this.end = end;
		this.indicies = new ArrayList<>();
		for(int i = start; i <= end; i++) {
			this.indicies.add(i);
		}
	}
	
	
	//Removes multiple indicies in interval
	public void removeIndexNearStart(int index) {
		this.indicies.remove(new Integer(index));
		for(int i = start; i <= index; i++) {
			this.indicies.remove(new Integer(i));
		}
		this.start = this.indicies.get(0);
	}
	public void removeIndexNearEnd(int index) {
		this.indicies.remove(new Integer(index));
		for(int i = index; i <= end; i++) {
			this.indicies.remove(new Integer(i));
		}
		this.start = this.indicies.get(this.indicies.size() - 1);
	}
	
	public boolean isBetween(int num) {
		if(num>=start && num<=end) {
			return true;
		}
		return false;
	}
	
	//Gets interval length
	public int length() {
		return end-start+1;
	}
	
	public String toString() {
		return this.indicies.toString();
//		String result = "";
//		result+="(";
//		result+=Integer.toString(start);
//		result+=", ";
//		result+=Integer.toString(end);
//		result+=")";
//		
//		return result;
	}
}
