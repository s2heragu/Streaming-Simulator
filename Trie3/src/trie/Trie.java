package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		
		//NULL OR EMPTY CASE
		if(allWords == null || allWords.length==0) {
			return new TrieNode(null,null,null);
		}
		
		//INDEX OF FIRST WORD TO BE ENTERED
		Indexes Start = new Indexes(0,(short)0,(short)(allWords[0].length()-1));
		
		//FIRST TRIENODE CHILD OF ROOT
		TrieNode firstChild = new TrieNode(Start,null,null);
		
		//ROOT TO BE RETURNED: FIRST CHILD IS INITIALIZED
		TrieNode root = new TrieNode(null,firstChild,null);
		
		//PARSING THROUGH allWords AND ADDING EVERY OTHER WORD
		for(int i = 1;i<allWords.length;i++) {
			
			//INDEXES OF WORD TO BE INSERTED
			Indexes currIndex = new Indexes(i,(short)0,(short)(allWords[i].length()-1));
			
			//TRIENODE OF CURRENT WORD TO BE INSERTED
			TrieNode curr = new TrieNode(currIndex,null,null);
			
			//DRIVING POINTER: STARTS AT FIRSTCHILD
			TrieNode ptr = root.firstChild;
			
			//LAGGING POINTER
			TrieNode back = root;
			
			//SIMILAR INDEX MARKER
			int Sim = 0;
			
			//BOOLEAN THAT INDICATES WHETHER WE HAVE TO INSERT NEW WORD
			//AS A SIBLING: IF FALSE, WE INSERT IT AS A CHILD
			boolean siblingEnd = false;
			
			//BOOLEAN THAT INDICATES WHETHER WE ARE AT A DEAD-END
			boolean deadEnd = false;
			
			//FINDING PLACE IN TREE TO INSERT NEW WORD
			while(ptr!=null) {
				
				//current indexes of pointer
				Indexes ptrInd = ptr.substr;
				
				//string representation of ptrInd
				String ptrWord = allWords[ptrInd.wordIndex].substring(
						(int)ptrInd.startIndex,(int)(ptrInd.endIndex+1));
				
				//updating the similar index number
				int adjust = StringCompare(allWords[i].substring(Sim),ptrWord);
				
				//if there is no similarity
				if(adjust==-1) {
					
					//updating back for next iteration of while loop
					back = ptr;
					
					//move to ptr's sibling for next iteration of while loop
					ptr = ptr.sibling;
					
					//we know that we have to enter the new word as a sibling
					if(ptr == null) {
						siblingEnd = true;
					}
					
				}
				
				else{
					
					//update similarity index
					Sim += adjust+1;
					
					//prefix adjustment check
					if(ptr.firstChild!=null) {
						
						//if word doesn't completely align with a prefix, we're at a
						//dead end
						if(!(allWords[i].substring(Sim-adjust-1).startsWith(ptrWord)||
								ptrWord.startsWith(allWords[i].substring(Sim-adjust-1)))) {
							deadEnd = true;
							break;
						}
						
					}
					
					//updating back for next iteration of while loop
					back = ptr;
					
					//move to ptr's child for next iteration of while loop
					ptr = ptr.firstChild;
					
				}
				
			}
			
			//update starting index of the node to be inserted with the similarity index
			curr.substr.startIndex = (short)Sim;
			
			//enter as a sibling
			if(siblingEnd) {
				back.sibling = curr;
			}
			
			//reached a dead end
			else if(deadEnd) {
				
				//indexes for ptr's new parent
				Indexes par = new Indexes(ptr.substr.wordIndex,ptr.substr.startIndex,(short)(Sim-1));
				
				//adjusting ptr's start index
				ptr.substr.startIndex = (short)Sim;
				
				//storing ptr's sibling
				TrieNode sib = ptr.sibling;
				
				//change ptr's sibling to the node currently being inserted
				ptr.sibling = curr;
			
				//if we approach a dead end from a parent
				if(ptr == back.firstChild) {
					
					//ptr's new parent becomes the child of ptr's old parent and inherits ptr's siblings
					back.firstChild = new TrieNode(par,ptr,sib);
					
				}
				
				//if we approach a dead end from a sibling
				else {
					
					//ptr's new parent becomes sibling of ptr's old sibling and inherits ptr's siblings
					back.sibling = new TrieNode(par,ptr,sib);
					
				}
				
			}
			
			//enter as a child
			else {
				
				//end index of unaltered parent TrieNode
				short oldBackEnd = back.substr.endIndex;
				
				//updating the endIndex of the parent TrieNode with the similarity index
				back.substr.endIndex = (short)(Sim-1);
				
				//creating new indexes for the first child; this is a reduction of the original,
				//unaltered, parent node
				Indexes first = new Indexes(back.substr.wordIndex,(short)Sim,oldBackEnd);
				
				//making the new parent's first child the reduced TrieNode, and the reduced TrieNode's
				//sibling the node to be inserted
				back.firstChild = new TrieNode(first,null,curr);
				
			}
			
			
		}
		
		//returning tree root
		return root;
	}
	
	//RETURNS THE LAST INDEX OF SEQUENTIAL SIMILARITY BETWEEN TWO STRINGS: 
	//RETURNS -1 IF THEY ARE NOT SIMILAR AT ALL
	private static int StringCompare(String a, String b) {
		
		int index = -1;
		
		//finding smaller word to avoid indexOutOfBoundsException
		String smaller = b;
		if(b.length()>a.length()) {
			smaller = a;
		}
		
		//Parsing through string characters, one by one
		for(int i = 0; i<smaller.length();i++) {
			if(b.charAt(i)!=a.charAt(i)) {
				break;
			}
			index++;
		}
		return index;
		
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		
		//NULL CASES
		if(root == null || root.firstChild == null || allWords == null || 
				allWords.length == 0 || prefix == null) {
			return null;
		}
		
		//RETURNED ARRAYLIST OF TREENODES
		ArrayList<TrieNode>list = new ArrayList<TrieNode>();
		
		//RETURN ALL WORDS FOR AN PREFIX WITH ZERO CHARACTERS
		if(prefix.length()==0) {
			Collect(root.firstChild,list,allWords);
			return list;
		}
		
		//DRIVING TRIENODE POINTER
		TrieNode ptr = root.firstChild;
		
		//STRING THAT GETS UPDATED BASED ON LOCATIO IN THE TRIE
		String preMarker = "";
		
		//LEVEL FOR EFFICIENCY CASE
		int level = 0;
		
		//TRAVERSING THROUGH THE TRIE
		while(ptr!=null) {
			
			//STRING OF CURRENT NODE
			String str = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex,ptr.substr.endIndex+1);
			
			//we've found a TrieNode that starts with the prefix
			//we can break the loop
			if((preMarker+str).startsWith(prefix)) {
				break;
			}
			
			//our prefix starts with our constructed word at the current location
			if(prefix.startsWith(preMarker+str)) {
				
				//update preMarker
				preMarker+=str;
				
				//we travel to the current node's child to hopefully construct a word
				//that contains the prefix
				ptr = ptr.firstChild;
				
				//increment level
				level++;
				
			}
			
			//haven't obtained sequential similarity with prefix
			else {
				
				//we know we can't encounter a similar word in the tree, so we halt the process
				if((str.charAt(0)==prefix.charAt(0)) && level == 0) {
					ptr = null;
					break;
				}
				
				//go to the sibling of the node
				else {
					ptr = ptr.sibling;
				}
				
			}
			
		}
		
		//no matching words found
		if(ptr == null) {
			return null;
		}
		
		//no child: only one word fits prefix
		if(ptr.firstChild == null) {
			list.add(ptr);
		}
		
		else {
			Collect(ptr.firstChild,list,allWords);
		}

		return list;
	}
	
	//COLLECTS ALL FULL WORDS BRANCHING OUT FROM A STARTING POINT
	private static void Collect(TrieNode start, ArrayList<TrieNode>List, String[]allWords) {
		
		//end case
		if(start==null) {
			return;
		}
		
		//if start is a full word
		if(start.substr.endIndex == (short)(allWords[start.substr.wordIndex].length()-1)) {
			List.add(start);
		}
		
		//branch out to sibling
		Collect(start.sibling,List,allWords);
		
		//branch out to child
		Collect(start.firstChild,List,allWords);
		
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
