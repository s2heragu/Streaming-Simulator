package Nonograms;

import java.util.ArrayList;

public class Board {
	
	//Number of rows and columns
	private int rows;
	private int cols;
	public ArrayList<ArrayList<Integer>> solution;
	public ArrayList<ArrayList<Integer>> rowArrays;
	public ArrayList<ArrayList<Integer>> colArrays;
	
	public ArrayList<ArrayList<Hint>> rowHints;
	public ArrayList<ArrayList<Hint>> colHints;
	
	public ArrayList<ArrayList<Box>> solutionBoxes;

	
	public Board(int rows, 
			int cols, 
			ArrayList<ArrayList<Integer>> solution, 
			ArrayList<ArrayList<Integer>> rowArrays, 
			ArrayList<ArrayList<Integer>> colArrays) {
		this.rows = rows;
		this.cols = cols;
		this.solution = solution;
		this.colArrays = colArrays;
		this.rowArrays = rowArrays;
		
		this.solutionBoxes = getBoxes();
		this.rowHints = getHints(rowArrays, "r");
		this.colHints = getHints(colArrays, "c");
		setPotentialBoxes();
	}
	
	public void setPotentialBoxes() {
		//Loop through each hint for rows
		for(int i = 0; i < rowHints.size(); i++) {
			for(int j = 0; j < rowHints.get(i).size(); j++) {
				Interval currInterval = rowHints.get(i).get(j).interval;
				Hint currHint = rowHints.get(i).get(j);
				//Loop through interval and assign potential boxes
				for(int k = currInterval.start; k <= currInterval.end; k++) {
					Box currBox = solutionBoxes.get(currHint.row).get(k);
					currBox.associatedRowHints.add(currHint);
				}
			}
		}
		//column hints
		for(int i = 0; i < colHints.size(); i++) {
			for(int j = 0; j < colHints.get(i).size(); j++) {
				Interval currInterval = colHints.get(i).get(j).interval;
				Hint currHint = colHints.get(i).get(j);
				//Loop through interval and assign potential boxes
				for(int k = currInterval.start; k <= currInterval.end; k++) {
					Box currBox = solutionBoxes.get(k).get(currHint.col);
					currBox.associatedColHints.add(currHint);
				}
			}
		}
	}
	
	public int maxRowHints() {
        int max = 0;
        for(ArrayList<Integer>curr:this.rowArrays) {
            if(curr.size()>max) {
                max = curr.size();
            }
        }
        return max;
    }

    public int maxColHints() {
        int max = 0;
        for(ArrayList<Integer>curr:this.colArrays) {
            if(curr.size()>max) {
                max = curr.size();
            }
        }
        return max;
    }
	
	//Assign hints
	private ArrayList<ArrayList<Hint>> getHints(ArrayList<ArrayList<Integer>> intHintArrays, String rowOrCol){
		ArrayList<ArrayList<Hint>> result = new ArrayList<>();
		int length;
		if(rowOrCol.equals("r")) {
			length = this.rows;
		}
		else {
			length = this.cols;
		}
		
		//For each Integer Hint Array (such as the 1, 6)
		for(int i = 0; i < length; i++) {
			ArrayList<Hint> hintArray = Hint.getHints(intHintArrays.get(i), i, rowOrCol, this.rows, this.cols);
			result.add(hintArray);
		}
		
		return result;
	}
	
	
	//Getters for number of rows and columns
	public int getRows() {
		return this.rows;
	}
	public int getColumns() {
		return this.cols;
	}
	
	//Get boxes for solution, row, column
	private ArrayList<ArrayList<Box>> getBoxes(){
		ArrayList<ArrayList<Box>> boxes = new ArrayList<>();
		for(int i = 0; i < getRows(); i++) {
			ArrayList<Box> boxRow = new ArrayList<>();
			for(int j = 0; j < getColumns(); j++) {
				Box currBox;
				int val = this.solution.get(i).get(j);
				//Not filled in 
				if(val == 0) {
					currBox = new Box(false, true, false);
				}
				//Filled in
				else {
					currBox = new Box(true, true, false);
				}
				currBox.row = i;
				currBox.col = j;
				boxRow.add(currBox);
			}
			boxes.add(boxRow);
		}
		return boxes;
	}
	
	
	public Board clear() {
        ArrayList<ArrayList<Integer>> newSol = new ArrayList<>(this.rows);
        for(int i = 0;i<this.rows;i++) {
            ArrayList<Integer> currRow = new ArrayList<>();
            for(int j = 0;j<this.cols;j++) {
                currRow.add(new Integer(0));
            }
            newSol.add(currRow);
        }
        return new Board(this.rows,this.cols,newSol,this.rowArrays,this.colArrays);
    }
	
	//Method for comparing board and solution
	public boolean compareBoards(Board otherBoard) {
		boolean result = true;
		for(int i = 0; i < getRows(); i++) {
			for(int j = 0; j < getColumns(); j++) {
				int myBox = this.solution.get(i).get(j);
				int otherBox = otherBoard.solution.get(i).get(j);
				if(myBox == otherBox) {
					
				}else {
					result = false;
				}
				/*
				if(myBox.isUnsolved() == true || otherBox.isUnsolved() == true) {
					result = false;
				}
				else {
					if(!(myBox.isFilled() == otherBox.isFilled())) {
						result = false;
					}
				}
				*/
				
			}
		}
		return result;
	}
	
	public String toString() {
		String result = "";
		for(ArrayList<Integer> row : solution) {
			result+=row.toString();
			result+="\n";
		}
		return result;
	}
}
