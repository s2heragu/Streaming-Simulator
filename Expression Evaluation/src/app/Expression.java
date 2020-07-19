package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	
    	//LISTS CONTAINING THE NAMES OF ALL VARIABLES AND ARRAYS
    	ArrayList<String>Vars = new ArrayList<String>();
    	ArrayList<String>Arrays = new ArrayList<String>();
    	
    	//DELETING ALL WHITESPACE IN EXPRESSION
    	
    	expr = expr. replaceAll("\\s","");
    	
    	//POPULATING VARS AND ARRAYS
    	
    	//marker tracks the name of each potential variable/array
    	String marker = "";
    	
    	for(int i = 0;i<expr.length();i++) {
    		
    		//if we encounter a letter character, we add it to the name
    		//currently being constructed
    		if(Character.isLetter(expr.charAt(i))){
    			marker+=expr.substring(i,i+1);
    		}
    		
    		//the name terminates if we've reached the end of the expression,
    		//or if we encountered a non-letter character
    		if(!Character.isLetter(expr.charAt(i)) || i == expr.length()-1){
    			
    			//if we have a constructed name thus far, we see if we
    			//can add it as a variable or an array
    			if(marker.length()!=0) {
    				
    				//bracket indicates that marker refers to an array
    				if(expr.charAt(i)=='[') {
    					
    					//prevents duplicates from being added
    					if(!Arrays.contains(marker)) {
    						Arrays.add(marker);
    						arrays.add(new Array(marker));
    					}
    					
    				}
    				
    				//otherwise, marker refers to a variable
    				else {
    					
    					//prevents duplicates from being added
    					if(!Vars.contains(marker)) {
    						Vars.add(marker);
    						vars.add(new Variable(marker));
    					}
    					
    				}
    				
    				//emptying marker so that it can store next name we
    				//encounter
    				marker = "";
    				
    			}
    			
    		}
    		
    	}
      		
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    //FINDS CLOSED PARTNER OF AN OPEN PARENTHESIS IN A STRING
    private static int ClosedParenPair(int start, String expr) {
    	
    	//INT THAT SHOWS HOW MANY OPEN PARENTHESES WE'VE PASSED
    	int toBeClosed = 1;
    	
    	//PARSING THROUGH STRING TO FIND MATCHING CLOSING PARENTHESIS
    	for(int i = start+1;i<expr.length();i++) {
    		
    		//new pair to be closed
    		if(expr.charAt(i)=='(') {
    			toBeClosed++;
    		}
    		
    		//we've closed a pair of parentheses
    		else if(expr.charAt(i)==')') {
    			toBeClosed--;
    		}
    		
    		//we've found the partner to the input open parenthesis
    		if(toBeClosed == 0) {
    			toBeClosed = i;
    			break;
    		}
    		
    	}
    	
    	return toBeClosed;
    }
    
    //FINDS CLOSED PARTNER OF AN OPEN BRACKET IN A STRING
    private static int ClosedBracketPair(int start, String expr) {
    	
    	//INT THAT SHOWS HOW MANY OPEN BRACKETS WE'VE PASSED
    	int toBeClosed = 1;
    	
    	//PARSING THROUGH STRING TO FIND MATCHING CLOSING BRACKET
    	for(int i = start+1;i<expr.length();i++) {
    		
    		//new pair to be closed
    		if(expr.charAt(i)=='[') {
    			toBeClosed++;
    		}
    		
    		//we've closed a pair of brackets
    		else if(expr.charAt(i)==']') {
    			toBeClosed--;
    		}
    		
    		//we've found the partner to the input open brackets
    		if(toBeClosed == 0) {
    			toBeClosed = i;
    			break;
    		}
    		
    	}
    	
    	return toBeClosed;
    }
    
    //BREAKS DOWN EXPRESSION INTO ARRAYLISTS WITH EVALUATED TERMS AND
    //OPERATORS
    private static void
    Parse(String expr, ArrayList<Variable>vars, ArrayList<String>Vars,
    		ArrayList<Array>arrays,ArrayList<String>Arrays, 
    		ArrayList<Float>terms, ArrayList<Character>ops){
    	
    	//STRING DENOTING CURRENT PROCESSED VARIABLE OR ARRAY
    	String name = "";
    	
    	//STRING DENOTING CURRENT PROCESSED NUMBER
    	String number = "";
    	
    	//INDEX MARKER FOR TRAVERSAL THROUGH THE EXPRESSION
    	int i = 0;
    	
    	//EXPRESSION TRAVERSAL
    	while(i<expr.length()) {
    		
    		if(expr.charAt(i)=='(') {
    			
    			//find matching closed parenthesis
    			int closed = ClosedParenPair(i,expr);
    			
    			//add the evaluated expression inside the parentheses
    			//to the terms list
    			String sub = expr.substring(i+1,closed);
    			terms.add(evaluate(sub,vars,arrays));
    			
    			//move i to one after the closed parentheses
    			i = closed+1;
    			
    		}
    		
    		else if(expr.charAt(i)=='[') {
    			
    			//use name to obtain the corresponding Array
    			int arrIndex = Arrays.indexOf(name);
    			Array arr = arrays.get(arrIndex);
    			
    			//find matching closed bracket
    			int closed = ClosedBracketPair(i,expr);
    			
    			//evaluate expression within brackets as an index and
    			//add the number in the obtained Array's index to the terms list
    			String sub = expr.substring(i+1,closed);
    			terms.add((float)arr.values[(int)evaluate(sub,vars,arrays)]);
    			
    			//reset name for future iterations
    			name = "";
    			
    			//move i to one position after the closed bracket
    			i = closed+1;
    			
    		}
    		
    		//if we encounter a digit and we're not at the end of the expression:
    		//a bigger number can still be formed
    		else if(Character.isDigit(expr.charAt(i)) && i!=expr.length()-1) {
    			
    			//add the digit to the number field
    			number += expr.substring(i,i+1);
    			
    			//move i one spot over
    			i++;
    			
    		}
    		
    		//if we encounter a letter and we're not at the end of the expression:
    		//a bigger number can still be formed
    		else if(Character.isLetter(expr.charAt(i)) && i!=expr.length()-1) {
    			
    			//add the letter to the name field
    			name += expr.substring(i,i+1);
    			
    			//move i one spot over
    			i++;
    			
    		}
    		
    		//WILL ONLY TRIGGER IF WE ENCOUNTER AN OPERATOR OR REACH END OF
    		//EXPRESSION: CLOSED PARENTHESES AND BRACKETS ARE SKIPPED FROM 
    		//PREVIOUS CASES ABOVE
    		else {
    			
    			//if we are at the end of the expression:
    			//in this case, a full number, or variable
    			//name has to have been formed with the last character
    			if(i==expr.length()-1) {
    				
    				//letter character indicate we are storing a variable name
    				if(Character.isLetter(expr.charAt(i))) {
    					name+=expr.substring(i,i+1);
    				}
    				
    				//digit indicates that we are storing a number
    				else if(Character.isDigit(expr.charAt(i))) {
    					number+=expr.substring(i,i+1);
    				}
    				
    			}
    			
    			//we've encountered an operator, and thus, need to
    			//add it to the ops list
    			else {
        			ops.add(expr.charAt(i));
    			}
    			
    			//if we've stored a variable name
    			if(name.length()!=0) {
    				
    				//obtain corresponding variable
    				int varIndex = Vars.indexOf(name);
    				Variable varr = vars.get(varIndex);
    				
    				//adding value of variable to terms list
    				terms.add((float)varr.value);
    				
    				//resetting name for future iterations
    				name = "";
    				
    			}
    			
    			//if we've stored a number
    			else if(number.length()!=0) {
    				
    				//adding number to terms list
    				terms.add(Float.parseFloat(number));
    				
    				//resetting number for future iterations
    				number = "";
    				
    			}
    			
    			//moving i over 1 position
    			i++;
    		}
    		
    	}
    	
    }
    
    //RETURNS INT PRIORITY OF OP1 WITH RESPECT TO OP2
    //0 IS FOR EQUAL PRIORITY, 1 FOR HIGHER PRIORITY, AND -1
    //FOR LOWER PRIORITY
    private static int priority(char op1, char op2) {
    	
    	if(op1==op2) {
    		return 0;
    	}
    	
    	//ADDITION
    	if(op1 == '+') {
    		if(op2 == '-') {
    			return 0;
    		}
    		if(op2 == '/' || op2 == '*') {
    			return -1;
    		}
    	}
    	
    	//SUBTRACTION
    	if(op1 == '-') {
    		if(op2 == '+') {
    			return 0;
    		}
    		if(op2 == '/' || op2 == '*') {
    			return -1;
    		}
    	}
    	
    	//MULTIPLICATION
    	if(op1 == '*') {
    		if(op2 == '/') {
    			return 0;
    		}
    		if(op2 == '+' || op2 == '-') {
    			return 1;
    		}
    	}
    	
    	//DIVISION
    	if(op2 == '*') {
    		return 0;
    	}
    	else {
    		return 1;
    	}
    }
    
    
    //DOES MATH BETWEEN TWO INPUT FLOATS, GIVEN A CHAR OPERATOR
    private static float doMath(float a, float b, char op) {
    	
    	if(op=='+') {
    		return a+b;
    	}
    	if(op=='-') {
    		return a-b;
    	}
    	if(op=='*') {
    		return a*b;
    	}
    	return a/b;
    	
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
   
    	//DELETING ALL WHITESPACE IN EXPRESSION
    	
    	expr = expr. replaceAll("\\s","");
    	
    	//STRING ARRAYLISTS THAT STORE THE NAMES OF VARIABLES AND ARRAYS
    	ArrayList<String>Vars = new ArrayList<String>();
    	ArrayList<String>Arrays = new ArrayList<String>();
    	
    	//populating Vars
    	for(int i = 0;i<vars.size();i++) {
    		Vars.add(vars.get(i).name);
    	}
    	
    	//populating Arrays
    	for(int j = 0;j<arrays.size();j++) {
    		Arrays.add(arrays.get(j).name);
    	}
    	
    	//ARRAYLIST THAT STORES EVALUATED TERMS OF THE EXPRESSION
    	ArrayList<Float>Terms = new ArrayList<Float>();
    	
    	//ARRAYLIST THAT STORES RELEVANT OPERATORS IN THE REDUCED EXPRESSION
    	ArrayList<Character>Ops = new ArrayList<Character>();
    	
    	//populating Terms and Ops lists
    	Parse(expr,vars,Vars,arrays,Arrays,Terms,Ops);
    	
    	float retVal = 0;
    	
    	//ONLY ONE TERM IN REDUCED EXPRESSION: NO COMPUTATION REQUIRED
    	if(Ops.isEmpty()) {
    		retVal = Terms.get(0);
    		return retVal;
    	}
    	
    	//STACK USED TO STORE ALL NUMERICAL TERMS
    	Stack<Float>nums = new Stack<Float>();
    	
    	//STACK USED TO STORE ALL OPERATORS
    	Stack<Character>oops = new Stack<Character>();
    	
    	//EVALUATION DRIVER
    	for(int i = 0;i<Ops.size();i++) {
    		
    		//we are starting the process
    		if(i==0) {
    			
    			//insert first two numbers into nums
    			nums.push(Terms.get(i));
    			nums.push(Terms.get(i+1));
    			
    			//insert first operator into oops
    			oops.push(Ops.get(i));
    			
    		}
    		
    		//we have multiple operators and have to determine priority
    		else {
    			
    			//operator to be inserted
    			Character next = Ops.get(i);
    			
    			//priority of next vs. last operator in the stack
    			int pr = priority(next,oops.peek());
    			
    			//first appearing float in expression
    			float first = 0;
    			
    			//second appearing float in expression
    			float second = 0;
    			
    			//float that holds mathematical output of first and second
    			float temp = 0;
    			
    			//if next has less priority: must evaluate expression
    			//with previous operator
    			if(pr<1) {
    				
    				//obtain last two numbers from nums
    				second = nums.pop();
    				first = nums.pop();
    				
    				//obtain last operator from oops
    				char curr = oops.pop();
    				
    				temp = doMath(first,second,curr);
    				
    				//insert mathematical output of first and second
    				//into nums, followed by next number
    				nums.push(new Float(temp));
    				nums.push(Terms.get(i+1));
    				
    				//push next operator into oops
    				oops.push(next);
    				
    			}
    			
    			//must evaluate expression with new operator first
    			else {
    				
    				//number already in nums
    				first = nums.pop();
    				
    				//number that we currently want to insert
    				second = Terms.get(i+1);
    				
    				temp = doMath(first,second,next);
    				
    				//inserting mathematical output of first and second
    				nums.push(new Float(temp));
    				
    			}
    		}
    	}
    	
    	//doing math on last two numbers in nums
    	float two = nums.pop();
    	float one = nums.pop();
    	char jar = oops.pop();
    	retVal = doMath(one,two,jar);
    	return retVal;
    	
    }
}
