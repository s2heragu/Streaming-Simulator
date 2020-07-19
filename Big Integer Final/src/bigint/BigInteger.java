package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		//null or empty string
		if(integer==null || integer.length()==0) {
			throw new IllegalArgumentException();
		}
		//end-whitespace deletion
		integer = integer.trim();
		//empty string after end-whitespace deletion
		if(integer.length()==0) {
			throw new IllegalArgumentException();
		}
		//parsing through string
		for(int i = 0; i<integer.length();i++) {
			if(i==0) {
				if(!Character.isDigit(integer.charAt(i))) {
					//no exception thrown for first character an operator
					if(!(integer.charAt(i)=='+' || integer.charAt(i)=='-')) {
						throw new IllegalArgumentException();
					}
				}
			}
			else {
				if(!Character.isDigit(integer.charAt(i))){
					throw new IllegalArgumentException();
				}
			}
		}
		boolean Negative = false;
		int start = 0;
		if(!Character.isDigit(integer.charAt(0))) {
			if(integer.charAt(0)=='-') {
				Negative = true;
			}
			start = 1;
		}
		//first occurrence of '0' in string
		int zeroStart = integer.indexOf('0');
		int case0 = start;
		//parses only if first digit is 0
		if(zeroStart==start) {
			case0 = zeroStart+1;
			while(case0<integer.length() && integer.charAt(case0)=='0') {
				case0++;
			}
		}
		//reduced string is a "0"
		if(case0==integer.length()) {
			return new BigInteger();
		}
		//reduced string
		String bigInt = integer.substring(case0);
		case0 = bigInt.length()-1;
		//returned BigInteger
		BigInteger retval = new BigInteger();
		retval.negative = Negative;
		//Adding least significant digit to linked list
		retval.front = new DigitNode(Integer.parseInt(bigInt.substring(case0,case0+1)),null);
		retval.numDigits++;
		DigitNode ptr = retval.front;
		case0--;
		//Adding remaining digits to linked list
		while(case0>=0) {
			ptr.next = new DigitNode(Integer.parseInt(bigInt.substring(case0,case0+1)),null);
			ptr = ptr.next;
			retval.numDigits++;
			case0--;
		}
		return retval;
	}
	
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		//Identity tests
		if(first.numDigits == 0) {
			return second;
		}
		if(second.numDigits == 0) {
			return first;
		}
		//Larger integer
		BigInteger Large = first;
		//Smaller integer
		BigInteger Small = second;
		//Front of returned Big Integer
		DigitNode Front = null;
		//Size of returned Big Integer
		int NumDigits = 0;
		//Smaller>Larger
		if(Large.numDigits<Small.numDigits) {
			Large = second;
			Small = first;
		}
		//Parses through each integer to see which one is bigger
		else if(Large.numDigits == Small.numDigits) {
			//Large pointer
			DigitNode ptr1 = Large.front;
			//Small pointer
			DigitNode ptr2 = Small.front;
			//Front of Large's backwards representation
			DigitNode bFront1 = null;
			//Front of Small's backwards representation
			DigitNode bFront2 = null;
			//Creating backward Digit Node arrangements
			while(ptr1!=null) {
				bFront1 = new DigitNode(ptr1.digit,bFront1);
				bFront2 = new DigitNode(ptr2.digit,bFront2);
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			}
			ptr1 = bFront1;
			ptr2 = bFront2;
			//Comparing each digit, one by one
			//Starting with most significant digit
			while(ptr1!=null) {
				//Large > Small
				if(ptr1.digit > ptr2.digit) {
					break;
				}
				//Small < Large
				if(ptr1.digit<ptr2.digit) {
					Large = second;
					Small = first;
					break;
				}
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			}
			//Integer digits are identical
			//Sum is 0 if Large and Small have opposite signs
			if(ptr1==null) {
				if (Large.negative!=Small.negative) {
					return new BigInteger();
				}
			}
		}
		//Answer will always have sign of larger magnitude
		boolean Negative = Large.negative;
		//Same sign inputs
		if(Large.negative == Small.negative) {
			//Boolean that determines whether to carry a 1
			boolean carry = false;
			//Sum of current two digits
			int digSum = Large.front.digit + Small.front.digit;
			//Dealing with carries
			if(digSum>=10) {
				carry = true;
				digSum = digSum%10;
			}
			Front = new DigitNode(digSum,null);
			//Updating Big Integer size
			NumDigits++;
			//Large pointer
			DigitNode ptr1 = Large.front.next;
			//Small pointer
			DigitNode ptr2 = Small.front.next;
			//Last digit place
			DigitNode prev = Front;
			//Current digit place
			DigitNode curr = new DigitNode(0,null);
			//Carrying over 1
			if(carry) {
				curr.digit++;
			}
			//Parsing across Small: Adding works same way as above
			while(ptr2!=null) {
				carry = false;
				digSum = ptr1.digit + ptr2.digit + curr.digit;
				if(digSum>=10) {
					carry = true;
					digSum = digSum%10;
				}
				curr.digit = digSum;
				prev.next = curr;
				NumDigits++;
				prev = curr;
				curr = new DigitNode(0,null);
				if(carry) {
					curr.digit++;
				}
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			}
			//Parsing across rest of Large; Adding works same way as above
			while(ptr1!=null) {
				carry = false;
				digSum = ptr1.digit + curr.digit;
				if(digSum>=10) {
					carry = true;
					digSum = digSum%10;
				}
				curr.digit = digSum;
				prev.next = curr;
				NumDigits++;
				prev = curr;
				curr = new DigitNode(0,null);
				if(carry) {
					curr.digit++;
				}
				ptr1 = ptr1.next;
			}
			if(curr.digit==1) {
				prev.next=curr;
				NumDigits++;
			}	
		}
		//Differently signed inputs
		else {
			//Boolean that determines whether to borrow a 1
			boolean borrow = false;
			//Difference of current two digits
			int digDiff = Large.front.digit - Small.front.digit;
			//Dealing with borrow
			if(digDiff<0) {
				borrow = true;
				digDiff = digDiff+10;
			}
			Front = new DigitNode(digDiff,null);
			//Updating Big Integer size
			NumDigits++;
			//Large pointer
			DigitNode ptr1 = Large.front.next;
			//Small pointer
			DigitNode ptr2 = Small.front.next;
			//Last digit place
			DigitNode prev = Front;
			//Current digit place
			DigitNode curr = new DigitNode(0,null);
			//Borrowing 1
			if(borrow) {
				curr.digit--;
			}
			//Parsing across Small; Subtraction works the same way as above
			while(ptr2!=null) {
				borrow = false;
				digDiff = ptr1.digit - ptr2.digit + curr.digit;
				if(digDiff<0) {
					borrow = true;
					digDiff = digDiff+10;
				}
				curr.digit = digDiff;
				prev.next = curr;
				NumDigits++;
				prev = curr;
				curr = new DigitNode(0,null);
				if(borrow) {
					curr.digit--;
				}
				ptr1 = ptr1.next;
				ptr2 = ptr2.next;
			}
			//Parsing across rest of Large; Subtraction works the same way as above
			while(ptr1!=null) {
				borrow = false;
				digDiff = ptr1.digit + curr.digit;
				if(digDiff<0) {
					borrow = true;
					digDiff = digDiff+10;
				}
				curr.digit = digDiff;
				prev.next = curr;
				NumDigits++;
				prev = curr;
				curr = new DigitNode(0,null);
				if(borrow) {
					curr.digit--;
				}
				ptr1 = ptr1.next;
			}
			//Checking for hanging zeroes
			int oldNum = NumDigits;
			DigitNode ptr3 = Front;
			//DigitNode Linked List in opposite order
			DigitNode bResult = null;
			//Reversing order of linked list
			while(ptr3!=null) {
				bResult = new DigitNode(ptr3.digit,bResult);
				ptr3 = ptr3.next;
			}
			//Finding first non-zero digit
			//This will be where we start our final number output
			//Decrement NumDigits as necessary
			while(bResult.digit == 0) {
				bResult = bResult.next;
				NumDigits--;
			}
			//We only adjust Front if we have hanging zeroes
			if(NumDigits!=oldNum) {
				Front = null;
				while(bResult!=null) {
					Front = new DigitNode(bResult.digit,Front);
					bResult = bResult.next;
				}
			}
		}
		//Finalizing
		BigInteger retVal = new BigInteger();
		retVal.front = Front;
		retVal.negative = Negative;
		retVal.numDigits = NumDigits;
		return retVal;
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		//ZeroIdentity
		if(first.numDigits == 0 || second.numDigits == 0) {
			return new BigInteger();
		}
		//OneIdentities
		if(first.numDigits == 1 && first.front.digit == 1) {
			return second;
		}
		if(second.numDigits == 1 && second.front.digit == 1) {
			return first;
		}
		//First pointer
		DigitNode ptr1 = first.front;
		//Second pointer
		DigitNode ptr2 = second.front;
		//Current position in returned BigInteger
		int index = 0;
		//Returned BigInteger
		BigInteger retVal = new BigInteger();
		//Second is multiplier; parse through second in parent loop
		while(ptr2!=null) {
			//If Second's digit is '0', we know that the 
			//current product of First, and Second's digit
			//is 0. No operations needed
			if(ptr2.digit!=0) {
				//BigInteger for current product of First and
				//Second's digit
				BigInteger temp = new BigInteger();
				//Temp's numDigits
				int NumDigits = 0;
				//Initializing front
				DigitNode tempFront = new DigitNode(0,null);
				//Current digit product
				int digProd = ptr1.digit*ptr2.digit;
				//Integer to carry over to next digit product
				int carry = 0;
				//dealing with carries
				if(digProd>=10) {
					carry = digProd/10;
					digProd = digProd%10;
				}
				tempFront.digit = digProd;
				DigitNode prev = tempFront;
				//Incrementing NumDigits accordingly
				NumDigits++;
				DigitNode curr = new DigitNode(carry,null);
				ptr1 = ptr1.next;
				//Doing the above math for the rest of first's digits
				while(ptr1 != null) {
					digProd = ptr1.digit * ptr2.digit + curr.digit;
					carry = 0;
					if(digProd>=10) {
						carry = digProd/10;
						digProd = digProd%10;
					}
					curr.digit = digProd;
					prev.next = curr;
					NumDigits++;
					prev = curr;
					curr = new DigitNode(carry,null);
					ptr1 = ptr1.next;
				}
				//Checking whether we need to add an extra digit
				if(curr.digit!=0) {
					prev.next = curr;
					NumDigits++;
				}
				//Placing zeroes at the end based on second's current digit position
				for(int i = 0;i<index;i++) {
					tempFront = new DigitNode(0,tempFront);
					NumDigits++;
				}
				temp.front = tempFront;
				temp.numDigits = NumDigits;
				//updating retVal by adding it to temp
				//only happens if temp!=0
				retVal = add(retVal,temp);
			}
			//Resetting for next iteration
			index++;
			ptr2 = ptr2.next;
			ptr1 = first.front;
		}
		if(first.negative!=second.negative) {
			retVal.negative = true;
		}
		return retVal;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}