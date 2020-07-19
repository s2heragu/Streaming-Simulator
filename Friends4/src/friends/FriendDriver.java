package friends;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;

public class FriendDriver {

	static Scanner stdin = new Scanner(System.in);
	
	public static void main(String[] args) throws FileNotFoundException{
		System.out.print("Enter words file name, or enter quit to stop => ");
		String wordsFile = stdin.nextLine();
		while(!wordsFile.equals("quit")) {
		Scanner sc = new Scanner(new File(wordsFile));
		Graph graph = new Graph(sc);
		ShortestPath(graph);
		SchoolCliques(graph);
		Connectors(graph);
		System.out.println();
		System.out.print("Enter new words file name, or enter quit to stop => ");
		wordsFile = stdin.nextLine();
		}
	}
	
	private static void ShortestPath(Graph input) {
		System.out.println("\nShortest Chain Between Two Names (enter two names, or 'quit' to advance): ");
		System.out.print("First Name: ");
		String name1 = stdin.nextLine().trim().toLowerCase();
		if(!name1.equals("quit")) {
			System.out.print("Second Name: ");
			String name2 = stdin.nextLine().trim().toLowerCase();
			while((!(name1.equals("quit")))&&(!(name2.equals("quit")))){
				ArrayList<String>out = Friends.shortestChain(input,name1,name2);
				if (out == null||out.size()==0) {
					System.out.println("No path found.");
				}
				else {
					String output = out.get(0);
					for(int i = 1;i<out.size();i++) {
						output += " --> " + out.get(i);
					}
					System.out.println(output);
				}
				System.out.println("\nEnter next pair, or enter 'quit' to advance: ");
				System.out.print("First Name: ");
				name1 = stdin.nextLine().trim().toLowerCase();
				if(name1.equals("quit")){
					break;
				}
				System.out.print("Second Name: ");
				name2 = stdin.nextLine().trim().toLowerCase();
			}
		}
	}
	
	private static void SchoolCliques(Graph input) {
		System.out.println("\nFinding School Cliques (enter a school name, or 'quit' to advance): ");
		System.out.print("School Name: ");
		String schoolName = stdin.nextLine().trim().toLowerCase();
		while(!schoolName.equals("quit")) {
			ArrayList<ArrayList<String>>Out = Friends.cliques(input, schoolName);
			if(Out == null||Out.size()==0) {
				System.out.println("No cliques found.");
			}
			else {
				for(int i = 0;i<Out.size();i++) {
					int j = i+1;
					System.out.println("Clique " + j + ": "+ Out.get(i));
				}
			}
			System.out.println("\nEnter next school, or enter 'quit' to advance: ");
			System.out.print("School Name: ");
			schoolName = stdin.nextLine().trim().toLowerCase();
		}
	}
	
	private static void Connectors(Graph input) {
		System.out.print("\nConnectors: ");
		ArrayList<String>Out = Friends.connectors(input);
		if(Out==null||Out.size()==0) {
			System.out.println("No connectors found.");
		}
		else {
			System.out.println(Out);
		}
	}
}

