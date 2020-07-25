package Nonograms;

import java.util.ArrayList;

public class Box implements Comparable<Box> {
	
	//Properties
	private boolean filled;
	private boolean unsolved;
	private boolean guess;
	
	public int row;
	public int col;
	
	//Remove this?
	public ArrayList<Hint> associatedRowHints;
	public ArrayList<Hint> associatedColHints;
	
	public Box(){
		this.filled = false;
		this.unsolved = true;
		
		//Not using for now
		this.guess = false;
	}
	public Box(boolean filled, boolean unsolved, boolean guess){
		this.filled = filled;
		this.unsolved = unsolved;
		this.guess = guess;
		this.associatedRowHints = new ArrayList<>();
		this.associatedColHints = new ArrayList<>();
	}
	
	public boolean isFilled() {
		return this.filled;
	}
	public boolean isUnsolved() {
		return this.unsolved;
	}
	public boolean getGuess() {
		return this.guess;
	}
	
	public void setFill(boolean a) {
		this.filled = a;
	}
	public void setUnsolved(boolean a) {
		this.unsolved = a;
	}
	public void setGuess(boolean a) {
		this.guess = a;
	}
	@Override
	public int compareTo(Box b) {
		return 0;
	}
	
	public String toString() {
		if(this.filled == false) {
			return "0";
		}
		return "1";
	}
	
	
}
