package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {
	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		//Catches null Inputs, 0 sized graph, same starting and end point, and non-existent names
		if(p1==null||p2==null||g==null||g.members.length==0||p1.equals(p2)||g.map.get(p1)==null||g.map.get(p2)==null) {
			return null;
		}
		//first word index; second word index
		int one = g.map.get(p1);
		int two = g.map.get(p2);
		//Returns null for isolated graph members
		if(g.members[one].first==null||g.members[two].first==null) {
			return null;
		}
		//Boolean array of visited members
		boolean [] visited = new boolean[g.members.length];
		//Queue of all Paths collected
		Queue<ArrayList<String>>Paths = new Queue<ArrayList<String>>();
		//Starting path
		ArrayList<String>start = new ArrayList<String>();
		start.add(p1);
		Paths.enqueue(start);
		visited[one]=true;
		while(!Paths.isEmpty()) {
			int size = Paths.size();
			//Empties out all the current paths
			for(int i = 0;i<size;i++) {
				ArrayList<String>currPath = Paths.dequeue();
				//Last member of the current path
				Person last = g.members[g.map.get(currPath.get(currPath.size()-1))];
				for(Friend fr = last.first; fr!=null; fr=fr.next) {
					if(!visited[fr.fnum]) {
						//Copy of current path
						ArrayList<String>nextPath = new ArrayList<String>(currPath);
						//Appending current friend to the path copy
						nextPath.add(g.members[fr.fnum].name);
						//First instance of second word; return the resulting list
						if(fr.fnum==two) {
							return nextPath;
						}
						visited[fr.fnum] = true;
						//adding updated paths
						Paths.enqueue(nextPath);
					}
				}
			}
		}
		//if we reach outside of the while loop, no path was found
		return null;
	}
	
	//DFS method for Cliques
	private static void DepthFirstClique(Graph g,ArrayList<String>Group,boolean[]visited,String school,int pos){
		visited[pos]=true;
		//Adding current member name: assuming that method is only called
		//if current member is a student at the input school in the cliques method
		Group.add(g.members[pos].name);
		for(Friend f = g.members[pos].first;f!=null;f=f.next) {
			//If: we've not visited the member, and member is a student
			if(!visited[f.fnum] && g.members[f.fnum].student) {
				//If: student is from the input school
				if(g.members[f.fnum].school.equals(school)) {
					//Updating Group accordingly
					DepthFirstClique(g,Group,visited,school,f.fnum);
				}
			}
		}
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		//Testing for null inputs, or a 0 sized graph
		if(g==null||school==null||g.members.length==0) {
			return null;
		}
		//returned arrayList
		ArrayList<ArrayList<String>>Cliques = new ArrayList<ArrayList<String>>();
		//boolean array for visited members
		boolean [] visited = new boolean[g.members.length];
		//Parses through all members
		for(int i = 0;i<visited.length;i++) {
			//If: member not visited and member is a student
			if(!visited[i] && g.members[i].student) {
				//If: member is a student at input school
				if(g.members[i].school.equals(school)){
					ArrayList<String>Clique = new ArrayList<String>();
					DepthFirstClique(g,Clique,visited,school,i);
					Cliques.add(Clique);
				}
			}
		}
		//no students from input school found
		if(Cliques.isEmpty()) {
			return null;
		}
		return Cliques;
	}
	
	//DFS Connectors Method
	private static void DFSTraversal(Graph g, ArrayList<String>Connectors, int Start, boolean[]visited, Edge[]DFSmemory, 
			int[]GraphDFSLinks, Integer DFSnum, int GraphNum, boolean[]cameBack) {
		visited[GraphNum]=true;
		//Links DFS number to relative position in the graph hierarchy
		GraphDFSLinks[GraphNum] = DFSnum;
		//Array of DFS edges (v1 is dfsnum, v2 is backnum)
		DFSmemory[DFSnum]=new Edge(DFSnum,DFSnum);
		//used to preserve current DFSnum for future purposes
		int temp = DFSnum;
		for(Friend f = g.members[GraphNum].first;f!=null;f=f.next) {
			//We've already visited the friend
			if(visited[f.fnum]) {
				DFSmemory[temp].v2 = Math.min(DFSmemory[temp].v2,DFSmemory[GraphDFSLinks[f.fnum]].v1);
			}
			//first visit to the friend
			else {
				//next DFSnum
				DFSnum = new Integer(DFSnum+1);
				//Next level of Depth First Search
				DFSTraversal(g,Connectors,Start,visited,DFSmemory,GraphDFSLinks, DFSnum,f.fnum, cameBack);
				if(DFSmemory[temp].v1>DFSmemory[GraphDFSLinks[f.fnum]].v2) {
					DFSmemory[temp].v2 = Math.min(DFSmemory[temp].v2, DFSmemory[GraphDFSLinks[f.fnum]].v2);
				}
				//connector test
				if(temp <= DFSmemory[GraphDFSLinks[f.fnum]].v2 && 
						Connectors.indexOf(g.members[GraphNum].name)==-1 && g.members[GraphNum].first.next!=null) {
					//1st condition, not for starting points: always a connector
					//2nd condition, for starting point: we come back to the starting point more than once if it is a connector
					if(GraphNum!=Start || cameBack[GraphNum]) {
						Connectors.add(g.members[GraphNum].name);
					}
				}
				cameBack[GraphNum]=true;
			}
		}
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		// 1.null graph test, 2.no connector test
		if(g == null||g.members.length<=2) {
			return null;
		}
		//Returned ArrayList
		ArrayList<String>connectors = new ArrayList<String>();
		//Main visited boolean array: drives the for loop below
		boolean [] visited = new boolean[g.members.length];
		//Boolean array for starting point connector test
		boolean [] CameBack = new boolean[g.members.length];
		for(int i = 0;i<visited.length;i++) {
			if(!visited[i]) {
				//DFS not initialized if member has no friends
				if(g.members[i].first!=null) {
					//Linkage array between graph position and DFS position
					int[]GtoDFS1 = new int[g.members.length];
					//Array that holds dfsnum and backnum for each visited member, in ascending order
					//Length increased by 1 due to DFS starting at 1, not 0
					Edge[]DFSmem1 = new Edge[g.members.length+1];
					//Integers that are consistently updated during the DFS process
					Integer DFSNum1 = new Integer(1);
					//Tests if there are any connectors accessible from the current member "i" 
					DFSTraversal(g,connectors,i,visited,DFSmem1,GtoDFS1,DFSNum1,i,CameBack);
				}
				visited[i]=true;
			}
		}
		//No connectors found
		if(connectors.isEmpty()) {
			return null;
		}
		return connectors;
	}
}

