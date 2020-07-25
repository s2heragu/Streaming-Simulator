package Nonograms;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.lang.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Driver {
	
	public static JFrame gui;
	public static Board currboard;
	public static Board solboard;
	public static ArrayList<ArrayList<JButton>>controlBoard;
	public static PriorityQueue<Box> q = new PriorityQueue<>();
	public static ArrayList<Box> hasBeenUpdated = new ArrayList<>();
	//to be fixed
	public static void main(String[] args) {
		init("Board.txt");
		//PriorityQueue<Box> q = new PriorityQueue<>();
		//Get guaranteed and fill them in, add to queue
		trivialRows();
		trivialCols();
		
		while(!q.isEmpty()) {
			Box curr = q.poll();
			updateRow(curr.row);
			if(solboard.compareBoards(currboard)) {
				//System.out.println("We did it");
				break;
			}
			updateCol(curr.col);
			if(solboard.compareBoards(currboard)) {
				//System.out.println("We did it");
				break;
			}
		}
		System.out.println("We did it");
	}
	
	public static void updateRow(int row) {
		ArrayList<Integer> filled = new ArrayList<>();
		ArrayList<Integer> notFilled = new ArrayList<>();
		ArrayList<Hint> rowHints = currboard.rowHints.get(row);
		//loop through the row
		for(int i = 0; i < currboard.getColumns(); i++) {
			Box currBox = currboard.solutionBoxes.get(row).get(i);
			if(currBox.isUnsolved()) {
				//Do nothing
			}
			else if(currBox.isFilled()) {
				filled.add(currBox.col);
			}
			else if(!currBox.isFilled() && !currBox.isUnsolved()) { //Red (not filled)
				//System.out.println(currBox.isUnsolved());

				notFilled.add(currBox.col);
			}
		}
		//System.out.println("filled = " + filled);
		//System.out.println("not filled = " + notFilled);
		
		//Associate hints for each box
		ArrayList<ArrayList<Integer>> associatedHints = new ArrayList<>();
		for(int i = 0; i < currboard.getColumns(); i++) {
			associatedHints.add(new ArrayList<>());
			for(Hint h : currboard.rowHints.get(row)) {
				if(h.interval.indicies.contains(i)) {
					associatedHints.get(i).add(h.val);
				}
			}
		}
		//System.out.println("associated hints = " + associatedHints);
		//Splice filled into consecutive arrays
		ArrayList<ArrayList<Integer>> consecutive = findConsecutive(filled);
		//System.out.println("consecutive = " + consecutive);
		
		//Loop through hints
		for(Hint h : rowHints) {
			//What blocks are filled and in the interval
			ArrayList<Integer> filledInterval = new ArrayList<>();
			ArrayList<Integer> notFilledInterval = new ArrayList<>();
			for(int index : filled) {
				//Only has one associated hint in that interval, 
				//nearby blocks can no longer associate with other hints
				if(h.interval.indicies.contains(index) && associatedHints.get(index).size() <= 1) {
					if(index == 0) {
						associatedHints.get(index + 1).clear();
						associatedHints.get(index + 1).add(h.val);
					}
					else if(index == currboard.getColumns() - 1) {
						associatedHints.get(index - 1).clear();
						associatedHints.get(index - 1).add(h.val);
					}
					else {
						associatedHints.get(index - 1).clear();
						associatedHints.get(index - 1).add(h.val);
						associatedHints.get(index + 1).clear();
						associatedHints.get(index + 1).add(h.val);
					}
					filledInterval.add(index);
				}
				//Has more than one hint, but largest and unique
				else if(h.interval.indicies.contains(index) && associatedHints.get(index).size() > 1) {
					//Largest hint, check solved but do not say it is solved unless unique
					if(getLargest(associatedHints.get(index)) == h.val) {
						for(ArrayList<Integer> block : consecutive) {
							if(block.contains(index)) {
								if(block.size() == h.val) {
									//Pad with red
									flag(row, block.get(0) - 1);
									flag(row, block.get(block.size() - 1) + 1);
									//Solve if unique
									if(isUniqueHint(associatedHints.get(index), h)) {
										h.interval.indicies = block;
										h.isSolved = true;
									}
								}

							}
						}
					}
				}
			}
			for(int index : notFilled) {
				if(h.interval.indicies.contains(index) && associatedHints.get(index).size() <= 1) {
					notFilledInterval.add(index);
				}
			}
			h.updateInterval(filledInterval, notFilledInterval, currboard.getRows(), currboard.getColumns());
			boolean hintSolved = h.checkSolved();
			if(hintSolved) {
				filled.removeAll(h.interval.indicies);
				int currHintIndex = currboard.rowHints.get(row).indexOf(h);
				
				
				int left = h.interval.start;
				int right = h.interval.end;
				
				
				
				if(!(left == 0)) {
					flag(row, left - 1);
					if(!notFilled.contains(left - 1)) {
						notFilled.add(left - 1);
					}
				}
				if(!(right == currboard.getColumns() - 1)) {
					flag(row, right + 1);
					if(!notFilled.contains(right + 1)) {
						notFilled.add(right + 1);
					}
				}
				//Only 1 hint case
				if(currboard.rowHints.get(row).size() == 1) {
					for(int i = 0 ; i <= left - 1; i++) {
						flag(row, i);
						if(!notFilled.contains(i)) {
							notFilled.add(i);
						}
					}
					for(int i = right + 1 ; i <= currboard.getColumns() - 1; i++) {
						flag(row, i);
						if(!notFilled.contains(i)) {
							notFilled.add(i);
						}
					}
				}
				//First hint case
				else if(currHintIndex == 0) {
					for(int i = 0 ; i <= left - 1; i++) {
						flag(row, i);
						if(!notFilled.contains(i)) {
							notFilled.add(i);
						}
					}
					Hint next = currboard.rowHints.get(row).get(currHintIndex + 1);
					if(next.isSolved) {
						int nextStart = next.interval.start;
						for(int i = right + 1 ; i <= nextStart - 1; i++) {
							flag(row, i);
							if(!notFilled.contains(i)) {
								notFilled.add(i);
							}
						}
					}
				}
				//Last hint case
				else if(currHintIndex == rowHints.size() - 1) {
					for(int i = right + 1 ; i <= currboard.getColumns() - 1; i++) {
						flag(row, i);
						if(!notFilled.contains(i)) {
							notFilled.add(i);
						}
					}
					Hint prev = currboard.rowHints.get(row).get(currHintIndex - 1);
					if(prev.isSolved) {
						int prevEnd = prev.interval.end;
						for(int i = prevEnd + 1 ; i <= left - 1; i++) {
							flag(row, i);
							if(!notFilled.contains(i)) {
								notFilled.add(i);
							}
						}
					}
				}
				//Middle hint case
				else {
					//Get hint before and after
					Hint prev = currboard.rowHints.get(row).get(currHintIndex - 1);
					Hint next = currboard.rowHints.get(row).get(currHintIndex + 1);
					if(prev.isSolved) {
						int prevEnd = prev.interval.end;
						for(int i = prevEnd + 1 ; i <= left - 1; i++) {
							flag(row, i);
							if(!notFilled.contains(i)) {
								notFilled.add(i);
							}
						}
					}
					if(next.isSolved) {
						int nextStart = next.interval.start;
						for(int i = right + 1 ; i <= nextStart - 1; i++) {
							flag(row, i);
							if(!notFilled.contains(i)) {
								notFilled.add(i);
							}
						}
					}
				}
				//each hint before
				for(int i = 0; i < currHintIndex; i++) {
					currboard.rowHints.get(row).get(i).interval.removeIndexNearEnd(left);
//					for(int j = 0; j < currboard.getColumns(); j++) {
//						if(!currboard.solutionBoxes.get(row).get(j).isUnsolved()) {
//							currboard.rowHints.get(row).get(i).interval.indicies.remove(new Integer(j));
//						}
//					}
				}
				//each hint after
				for(int i = currHintIndex + 1; i <= currboard.rowHints.get(row).size() - 1 ; i++) {
					currboard.rowHints.get(row).get(i).interval.removeIndexNearStart(right);
//					for(int j = 0; j < currboard.getColumns(); j++) {
//						if(!currboard.solutionBoxes.get(row).get(j).isUnsolved()) {
//							currboard.rowHints.get(row).get(i).interval.indicies.remove(new Integer(j));
//						}
//					}
				}
			}
			
			//Fill in guaranteed
			ArrayList<Integer> guaranteed = h.getGuaranteed();
			//System.out.println("Guaranteed  = " + h.getGuaranteed());
			for(Integer col : guaranteed) {
				Box currBox = currboard.solutionBoxes.get(row).get(col);
				if(currBox.isUnsolved()) {
					blacken(row, col);
				}
			}
		}
		
		//Check if row solved
		boolean rowSolved = true;
		for(int i = 0; i < rowHints.size(); i++) {
			if(i > consecutive.size() - 1) {
				rowSolved = false;
				break;
			}
			if(rowHints.get(i).val != consecutive.get(i).size()) {
				rowSolved = false;
				break;
			}
		}
		//make sure to make everything else red, set each hint to be solved
		if(rowSolved) {
			for(int i = 0; i < currboard.getColumns(); i++) {
				if(controlBoard.get(row).get(i).getBackground() == Color.WHITE) {
					flag(row, i);
				}
			}
			for(Hint h : rowHints) {
				h.isSolved = true;
			}
		}
		
		//Associate hints for each box 
		ArrayList<ArrayList<Integer>> newAssociatedHints = new ArrayList<>();
		for(int i = 0; i < currboard.getColumns(); i++) {
			newAssociatedHints.add(new ArrayList<>());
			for(Hint h : currboard.rowHints.get(row)) {
				if(h.interval.indicies.contains(i)) {
					newAssociatedHints.get(i).add(h.val);
				}
			}
		}
		//find updated boxes that don't have associated hints and redden
		//System.out.println("newly associated hints = " + newAssociatedHints);
		for(int i = 0; i < currboard.getColumns(); i++) { // i is column
			if(newAssociatedHints.get(i).isEmpty()) {
				flag(row, i);
			}
		}
	}
	public static void updateCol(int col) {
		ArrayList<Integer> filled = new ArrayList<>();
		ArrayList<Integer> notFilled = new ArrayList<>();
		ArrayList<Hint> colHints = currboard.colHints.get(col);
		//System.out.println("colHints = " + colHints);
		//loop through the row
		for(int i = 0; i < currboard.getRows(); i++) {
			Box currBox = currboard.solutionBoxes.get(i).get(col);
			if(currBox.isUnsolved()) {
				//Do nothing
			}
			else if(currBox.isFilled()) {
				filled.add(currBox.row);
			}
			else if(!currBox.isFilled() && !currBox.isUnsolved()) { //Red (not filled)
				//System.out.println(currBox.isUnsolved());

				notFilled.add(currBox.row);
			}
		}
		//System.out.println("filled = " + filled);
		//System.out.println("not filled = " + notFilled);
		
		//Associate hints for each box
		ArrayList<ArrayList<Integer>> associatedHints = new ArrayList<>();
		for(int i = 0; i < currboard.getRows(); i++) {
			associatedHints.add(new ArrayList<>());
			for(Hint h : currboard.colHints.get(col)) {
				if(h.interval.indicies.contains(i)) {
					associatedHints.get(i).add(h.val);
				}
			}
		}
		//System.out.println("associated hints = " + associatedHints);
		//Splice filled into consecutive arrays
		ArrayList<ArrayList<Integer>> consecutive = findConsecutive(filled);
		//System.out.println("consecutive = " + consecutive);
		
		//Loop through hints
		for(Hint h : colHints) {
			//What blocks are filled and in the interval
			ArrayList<Integer> filledInterval = new ArrayList<>();
			ArrayList<Integer> notFilledInterval = new ArrayList<>();
			for(int index : filled) {
				//Only has one associated hint in that interval, 
				//nearby blocks can no longer associate with other hints
				if(h.interval.indicies.contains(index) && associatedHints.get(index).size() <= 1) {
					if(index == 0) {
						associatedHints.get(index + 1).clear();
						associatedHints.get(index + 1).add(h.val);
					}
					else if(index == currboard.getRows() - 1) {
						associatedHints.get(index - 1).clear();
						associatedHints.get(index - 1).add(h.val);
					}
					else {
						associatedHints.get(index - 1).clear();
						associatedHints.get(index - 1).add(h.val);
						associatedHints.get(index + 1).clear();
						associatedHints.get(index + 1).add(h.val);
					}
					filledInterval.add(index);
				}
				//Has more than one hint, but largest and unique
				else if(h.interval.indicies.contains(index) && associatedHints.get(index).size() > 1) {
					//Largest hint, check solved but do not say it is solved unless unique
					if(getLargest(associatedHints.get(index)) == h.val) {
						for(ArrayList<Integer> block : consecutive) {
							if(block.contains(index)) {
								if(block.size() == h.val) {
									//Pad with red
									flag(block.get(0) - 1, col);
									flag(block.get(block.size() - 1) + 1, col);
									//Solve if unique
									if(isUniqueHint(associatedHints.get(index), h)) {
										h.interval.indicies = block;
										h.isSolved = true;
									}
								}

							}
						}
					}
				}
			}
			for(int index : notFilled) {
				if(h.interval.indicies.contains(index) && associatedHints.get(index).size() <= 1) {
					notFilledInterval.add(index);
				}
			}
			h.updateInterval(filledInterval, notFilledInterval, currboard.getRows(), currboard.getColumns());
			boolean hintSolved = h.checkSolved();
			if(hintSolved) {
				filled.removeAll(h.interval.indicies);
				int currHintIndex = currboard.colHints.get(col).indexOf(h);
				
				
				int left = h.interval.start;
				int right = h.interval.end;
				
				
				
				if(!(left == 0)) {
					flag(left - 1, col);
					if(!notFilled.contains(left - 1)) {
						notFilled.add(left - 1);
					}
				}
				if(!(right == currboard.getRows() - 1)) {
					flag(right + 1, col);
					if(!notFilled.contains(right + 1)) {
						notFilled.add(right + 1);
					}
				}
				//Only 1 hint case
				if(currboard.colHints.get(col).size() == 1) {
					for(int i = 0 ; i <= left - 1; i++) {
						flag(i, col);
						if(!notFilled.contains(i)) {
							notFilled.add(i);
						}
					}
					for(int i = right + 1 ; i <= currboard.getRows() - 1; i++) {
						flag(i, col);
						if(!notFilled.contains(i)) {
							notFilled.add(i);
						}
					}
				}
				//First hint case
				else if(currHintIndex == 0) {
					for(int i = 0 ; i <= left - 1; i++) {
						flag(i, col);
						if(!notFilled.contains(i)) {
							notFilled.add(i);
						}
					}
					Hint next = currboard.colHints.get(col).get(currHintIndex + 1);
					if(next.isSolved) {
						int nextStart = next.interval.start;
						for(int i = right + 1 ; i <= nextStart - 1; i++) {
							flag(i, col);
							if(!notFilled.contains(i)) {
								notFilled.add(i);
							}
						}
					}
				}
				//Last hint case
				else if(currHintIndex == colHints.size() - 1) {
					for(int i = right + 1 ; i <= currboard.getRows() - 1; i++) {
						flag(i, col);
						if(!notFilled.contains(i)) {
							notFilled.add(i);
						}
					}
					Hint prev = currboard.colHints.get(col).get(currHintIndex - 1);
					if(prev.isSolved) {
						int prevEnd = prev.interval.end;
						for(int i = prevEnd + 1 ; i <= left - 1; i++) {
							flag(i, col);
							if(!notFilled.contains(i)) {
								notFilled.add(i);
							}
						}
					}
				}
				//Middle hint case
				else {
					//Get hint before and after
					Hint prev = currboard.colHints.get(col).get(currHintIndex - 1);
					Hint next = currboard.colHints.get(col).get(currHintIndex + 1);
					if(prev.isSolved) {
						int prevEnd = prev.interval.end;
						for(int i = prevEnd + 1 ; i <= left - 1; i++) {
							flag(i, col);
							if(!notFilled.contains(i)) {
								notFilled.add(i);
							}
						}
					}
					if(next.isSolved) {
						int nextStart = next.interval.start;
						for(int i = right + 1 ; i <= nextStart - 1; i++) {
							flag(i, col);
							if(!notFilled.contains(i)) {
								notFilled.add(i);
							}
						}
					}
				}
				//each hint before
				for(int i = 0; i < currHintIndex; i++) {
					currboard.colHints.get(col).get(i).interval.removeIndexNearEnd(left);
//					for(int j = 0; j < currboard.getRows(); j++) {
//						if(!currboard.solutionBoxes.get(j).get(col).isUnsolved()) {
//							currboard.colHints.get(col).get(i).interval.indicies.remove(new Integer(j));
//						}
//					}
				}
				//each hint after
				for(int i = currHintIndex + 1; i <= currboard.colHints.get(col).size() - 1 ; i++) {
					currboard.colHints.get(col).get(i).interval.removeIndexNearStart(right);
//					for(int j = 0; j < currboard.getRows(); j++) {
//						if(!currboard.solutionBoxes.get(j).get(col).isUnsolved()) {
//							currboard.colHints.get(col).get(i).interval.indicies.remove(new Integer(j));
//						}
//					}
				}
			}
			
			//Fill in guaranteed
			ArrayList<Integer> guaranteed = h.getGuaranteed();
			//System.out.println("Guaranteed  = " + h.getGuaranteed());
			for(Integer row : guaranteed) {
				Box currBox = currboard.solutionBoxes.get(row).get(col);
				if(currBox.isUnsolved()) {
					blacken(row, col);
				}
			}
		}
		
		//Check if col solved
		boolean colSolved = true;
		for(int i = 0; i < colHints.size(); i++) {
			if(i > consecutive.size() - 1) {
				colSolved = false;
				break;
			}
			if(colHints.get(i).val != consecutive.get(i).size()) {
				colSolved = false;
				break;
			}
		}
		//System.out.println("col solved = " + colSolved);
		//make sure to make everything else red, set each hint to be solved
		if(colSolved) {
			for(int i = 0; i < currboard.getRows(); i++) {
				if(controlBoard.get(i).get(col).getBackground() == Color.WHITE) {
					flag(i, col);
				}
			}
			for(Hint h : colHints) {
				h.isSolved = true;
			}
		}
		
		//Associate hints for each box 
		ArrayList<ArrayList<Integer>> newAssociatedHints = new ArrayList<>();
		for(int i = 0; i < currboard.getRows(); i++) {
			newAssociatedHints.add(new ArrayList<>());
			for(Hint h : currboard.colHints.get(col)) {
				if(h.interval.indicies.contains(i)) {
					newAssociatedHints.get(i).add(h.val);
				}
			}
		}
		//find updated boxes that don't have associated hints and redden
		//System.out.println("newly associated hints = " + newAssociatedHints);
		for(int i = 0; i < currboard.getRows(); i++) { // i is row
			if(newAssociatedHints.get(i).isEmpty()) {
				flag(i, col);
			}
		}
	}
	
	//Get largest hint value from array
	public static int getLargest(ArrayList<Integer> hints) {
		int max = 0;
		for(int i : hints){
			if(i > max) {
				max = i;
			}
		}
		return max;
	}
	
	//Given hint values, determine uniqueness
	public static boolean isUniqueHint(ArrayList<Integer> hints, Hint currHint) {
		int val = currHint.val;
		int count = 0;
		for(int i : hints) {
			if(val == i) {
				count++;
			}
		}
		return count == 1;
	}
	
	
	//Splices filled array into consecutive lists
	public static ArrayList<ArrayList<Integer>> findConsecutive(ArrayList<Integer> filled){
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();
		if(filled.isEmpty()) {
			return result;
		}

		ArrayList<Integer> consecutive = new ArrayList<>();
		int prev = filled.get(0) - 1;
		for(int i : filled) {
			//is consecutive
			if(i - prev <= 1) {
				consecutive.add(i);
			}
			else {
				result.add(consecutive);
				consecutive = new ArrayList<>();
				consecutive.add(i);
			}
			prev = i;
		}
		result.add(consecutive);
		
		return result;
	}
	
	
	public static void trivialRows() {
		for(int i = 0; i < currboard.rowHints.size(); i++) {
			for(int j = 0; j < currboard.rowHints.get(i).size(); j++) {
				ArrayList<Integer> temp = currboard.rowHints.get(i).get(j).getGuaranteed();
				int row = i;
				int col = -1;
				for(int e: temp) {
					col = e;
					blacken(row, col);
					Box coordinate = currboard.solutionBoxes.get(row).get(col);
					if(q.contains(coordinate)) {
						continue;
					}
					q.offer(coordinate);
				}
			}
		}
	}
	public static void trivialCols() {
		for(int i = 0; i < currboard.colHints.size(); i++) {
			for(int j = 0; j < currboard.colHints.get(i).size(); j++) {
				ArrayList<Integer> temp = currboard.colHints.get(i).get(j).getGuaranteed();
				int row = -1;
				int col = i;
				for(int e: temp) {
					row = e;
					blacken(row, col);
					Box coordinate = currboard.solutionBoxes.get(row).get(col);
					if(q.contains(coordinate)) {
						continue;
					}
					q.offer(coordinate);
				}
			}
		}
	}
	
	//color a block on the board black
	public static void blacken(int row, int col) {
		if(row > currboard.getRows() - 1 || row < 0 || col > currboard.getColumns() - 1 || col < 0) {
			return;
		}
		currboard.solution.get(row).set(col,new Integer(1));
		Box currBox = currboard.solutionBoxes.get(row).get(col);
		if(!currBox.isUnsolved()) {
			return;
		}
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currBox.setFill(true);
		currBox.setUnsolved(false);
		controlBoard.get(row).get(col).setBackground(Color.BLACK);
		//Do not queue a second time if already in queue or has been queued
		if(!q.contains(currBox) && !hasBeenUpdated.contains(currBox)) {
			q.offer(currBox);
			hasBeenUpdated.add(currBox);
		}
	}
	
	//color a block on the board white
	public static void whiten(int row, int col) {
		if(row > currboard.getRows() - 1 || row < 0 || col > currboard.getColumns() - 1 || col < 0) {
			return;
		}
		currboard.solution.get(row).set(col,new Integer(0));
		Box currBox = currboard.solutionBoxes.get(row).get(col);
		currBox.setFill(false);
		currBox.setUnsolved(true);
		controlBoard.get(row).get(col).setBackground(Color.WHITE);
	}
	
	//flag a block on the board (color it red)
	//Does not do anything for out of bounds
	public static void flag(int row, int col) {
		if(row > currboard.getRows() - 1 || row < 0 || col > currboard.getColumns() - 1 || col < 0) {
			return;
		}
		currboard.solution.get(row).set(col,new Integer(0));
		Box currBox = currboard.solutionBoxes.get(row).get(col);
		if(!currBox.isUnsolved()) {
			return;
		}
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currBox.setFill(false);
		currBox.setUnsolved(false);
		controlBoard.get(row).get(col).setBackground(Color.RED);
		if(!q.contains(currBox) && !hasBeenUpdated.contains(currBox)) {
			q.offer(currBox);
			hasBeenUpdated.add(currBox);
		}
	}
	
	
	public static void makeBoards(String boardFilePath) {
		solboard = BoardReader.makeBoard(boardFilePath);
		currboard = solboard.clear();
	}
	
	//makes gui board
	public static void makeGUI() {
		gui = new JFrame("Nonogram");
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(800,800);
		
		int maxRowHints = solboard.maxRowHints();
		int maxColHints = solboard.maxColHints();
		int rows = solboard.getRows();
		int cols = solboard.getColumns();
		
		gui.setLayout(new GridLayout(rows+maxColHints,cols+maxRowHints));
		controlBoard = new ArrayList<ArrayList<JButton>>();
		
		for(int i = 0;i<rows+maxColHints;i++) {
			ArrayList<JButton>currRow = new ArrayList<JButton>();
			if(i>=maxColHints) {
				controlBoard.add(currRow);
			}
			
			for(int j = 0;j<cols+maxRowHints;j++) {
				JButton btn = new JButton("");
				btn.setOpaque(true);
				
				if(i<maxColHints && j<maxRowHints) {
					btn.setBackground(Color.DARK_GRAY);
					btn.disable();
				}
				
				else if(i>=maxColHints && j>=maxRowHints) {
					btn.setBackground(Color.WHITE);
					btn.addMouseListener(new MouseAdapter() {
		                @Override
		                public void mouseClicked(MouseEvent e) {
		                    if (SwingUtilities.isRightMouseButton(e)) {
		                    	if(!btn.getBackground().equals(Color.RED)) {
									btn.setBackground(Color.RED);
								}
								else {
									btn.setBackground(Color.WHITE);
								}
							}
		                    else {
		                    	if(btn.getBackground().equals(Color.WHITE)) {
									btn.setBackground(Color.BLACK);
								}
								else {
									btn.setBackground(Color.WHITE);
								}
		                    }
		  
		                }
		            });
					currRow.add(btn);
				}
				
				else {
					btn.setBackground(Color.GREEN);
					btn.disable();
					ArrayList<Integer>curr = new ArrayList<>();
					int size;
					int start;
					
					if(i<maxColHints) {
						curr = solboard.colArrays.get(j-maxRowHints);
						size = curr.size();
						start = maxColHints-size;
						if(i>=start) {
							btn.setText(curr.get(size - (maxColHints - i - 1) - 1).toString());
						}
					}
					
					if(j<maxRowHints) {
						curr = solboard.rowArrays.get(i-maxColHints);
						size = curr.size();
						start = maxRowHints-size;
						if(j>=start) {
							btn.setText(curr.get(size - (maxRowHints - j - 1) - 1).toString());
						}
					}
					
				}		
				gui.getContentPane().add(btn);	
			}
		}
		
		gui.setVisible(true);
		
	}
	
	//initializes everything
	public static void init(String boardFilePath) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			makeBoards(boardFilePath);
			makeGUI();
		}catch(Exception e){
			  e.printStackTrace(); 
			 }
		
	}
}
