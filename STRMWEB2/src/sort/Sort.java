package sort;

import movElem.Movie;

public class Sort {

	public static Movie split(Movie mov, int size) {
	       if (mov == null) {
	    	   return null;
	       }
	       int mid = (size+1)/2;
	       Movie ptr = mov;
	       Movie back = null;
	       for(int i = 0;i<mid;i++) {
	    	   back = ptr;
	    	   ptr = ptr.next; 
	       }
	       back.next = null;
	       return ptr;
	   }
	
	public static Movie merge(Movie l1, Movie l2, String param) {
	       if (l1 == null) { return l2; }
	       if (l2 == null) { return l1; }
	       double l1info = 0;
	       double l2info = 0;
	       if(param == "r" ) {
	    	   l1info = l1.Score();
	    	   l2info = l2.Score();
	       }
	       else if(param == "p") {
	    	   l1info = l1.Streams();
	    	   l2info = l2.Streams();
	       }
	       if(l1info>l2info) {
	    	   l1.next = merge(l1.next,l2,param);
	    	   return l1;
	       }
	       else {
	    	   l2.next = merge(l1,l2.next,param);
	    	   return l2;
	       }
	}
	
	public static Movie mergeSort(Movie front, int size, String param) {
		if(front==null || front.next==null) {
			return front;
		}
		Movie second = split(front,size);
		
		front = mergeSort(front,(size+1)/2, param);
		second = mergeSort(second,size-(size+1)/2, param);
	
		return merge(front,second,param);
	}
	      	
}