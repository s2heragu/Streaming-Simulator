package Nonograms;
import java.lang.*;
import java.util.*;
import java.io.*;


/*

Board text file format
0 for not filled in
1 for filled in

Number of Rows (int)
Number of Columns (int)
Read row (0) Solution
Read row (1) Solution
...

Read row (0) Array
...
Read col (0) Array
...



 */



//Static class to read board text files
public class BoardReader {
	
	//Properties
	public String boardFilePath = "Board.txt";

	
	public static Board makeBoard(String fileName) {
		
		File boardFile = new File(fileName);
		Scanner scan;
		try {
			scan = new Scanner(boardFile);
			return makeBoard(scan);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private static Board makeBoard(Scanner boardScan) {
		
		//make a board
		Board board;
		
		//read number of rows and columns
		int rows = Integer.parseInt(boardScan.nextLine());
		int cols = Integer.parseInt(boardScan.nextLine());
		

		ArrayList<ArrayList<Integer>> boardArray = new ArrayList<>();
		ArrayList<ArrayList<Integer>> rowArrays = new ArrayList<>();
		ArrayList<ArrayList<Integer>> colArrays = new ArrayList<>();
		
		//Read board solution
		for (int i = 0; i < rows; i++) {
			//Read Array
			ArrayList<Integer> row = new ArrayList<>();
			String line = boardScan.nextLine();
			row = getArray(line);
			boardArray.add(row);
		}
		
		//Row numbers
		boardScan.nextLine();
		
		for(int i = 0; i < rows; i++) {
			ArrayList<Integer> rowArray = new ArrayList<>();
			String line = boardScan.nextLine();
			rowArray = getArray(line);
			rowArrays.add(rowArray);
		}
		
		//Column numbers
		boardScan.nextLine();
		for(int i = 0; i < cols; i++) {
			ArrayList<Integer> colArray = new ArrayList<>();
			String line = boardScan.nextLine();
			colArray = getArray(line);
			colArrays.add(colArray);
		}
		boardScan.close();
		
		board = new Board(rows, cols, boardArray, rowArrays, colArrays);
		return board;
		
	}
	
	private static ArrayList<Integer> getArray(String line){
		ArrayList<Integer> array = new ArrayList<>();
		String[] lineElements = line.split(",");
		for(String num: lineElements) {
			array.add(Integer.parseInt(num));
		}
		
		return array;
	}
	
}
