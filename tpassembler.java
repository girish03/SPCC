package precodes;
import java.util.*;
import java.io.*;

class Tuple {
	String mnemonic, bin_opcode, type;
	int length;
	
	Tuple() {}
	
	Tuple(String string1, String string2, String string3, String string4) {
		mnemonic = string1;
		bin_opcode = string2;
		try {
			length = Integer.parseInt(string3);
		} catch(Exception E) {
			length = 0;
		}
		type = string4;
	}
}

class SymTuple {
	String symbol, ra;
	int value, length;
	
	SymTuple(String string1, int integer1, int integer2, String string2) {
		symbol = string1;
		value = integer1;
		length = integer2;
		ra = string2;
	}
}

class LitTuple {
	String literal, ra;
	int value, length;
	
	LitTuple() {}
	
	LitTuple(String string1, int integer1, int integer2, String string2) {
		literal = string1;
		value = integer1;
		length = integer2;
		ra = string2;
	}
}

public class tpassembler {
	
	public static int lc;
	public static List<Tuple> mnem_table;
	public static List<String> po_table;
	public static List<SymTuple> symtable;
	public static List<LitTuple> littable;
	public static List<Integer> lclist;
	public static Map<Integer, Integer> basetable;
	public static PrintWriter output_pass2;
	public static PrintWriter pass1_output;
	public static int line_no = 0;
	
	public static double start;
	public static double end;
	public static double total = 0;
	
	public static void main(String args[]) throws Exception {
		
		symtable = new LinkedList<>();
		littable = new LinkedList<>();
		lclist = new ArrayList<>();
		basetable = new HashMap<>();
		mnem_table = new LinkedList<>();
		po_table = new LinkedList<>();
		
		mnem_table.add(new Tuple("LA","01H","4","RX"));
		mnem_table.add(new Tuple("SR","02H","2","RR"));
		mnem_table.add(new Tuple("L","03H","4","RX"));
		mnem_table.add(new Tuple("AR","04H","2","RR"));
		mnem_table.add(new Tuple("A","05H","4","RX"));
		mnem_table.add(new Tuple("C","06H","4","RX"));
		mnem_table.add(new Tuple("BNE","07H","4","RX"));
		mnem_table.add(new Tuple("LR","08H","2","RR"));
		mnem_table.add(new Tuple("ST","09H","4","RX"));
		mnem_table.add(new Tuple("BR","15H","2","RR"));
	
		po_table.add("START");
		po_table.add("END");
		po_table.add("LTORG");
		po_table.add("DC");
		po_table.add("DS");
		po_table.add("DROP");
		po_table.add("USING");
		po_table.add("EQU");
		
		Collections.sort(po_table);
		
		System.out.println("---------------PASS 1----------------\n");
		
		start = System.currentTimeMillis();
		pass1();		
		System.out.println("\n---------------PASS 2----------------\n");
		pass2();
		end = System.currentTimeMillis();
		
		System.out.println("\nTime Required: " + (end - start));
	
	}
	
	public static void pass1() throws Exception {
		BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Admin\\workspace\\Learning_Java\\src\\precodes\\input.txt")));
		
		pass1_output = new PrintWriter(new FileWriter("pass1_op.txt"), true);
		PrintWriter out_symtable = new PrintWriter(new FileWriter("out_symbolop.txt"), true);
		PrintWriter out_littable = new PrintWriter(new FileWriter("literalop.txt"), true);
		
		
		String s;
	
		while((s = input.readLine()) != null) {
			
			//start = System.nanoTime();

			StringTokenizer token = new StringTokenizer(s, " ", false);
			String token_array[] = new String[token.countTokens()];
		
			for(int i = 0 ; i < token_array.length ; i++) {
			
				token_array[i] = token.nextToken();
			
			}
			
			if (!searchPot(token_array)) {
				
				searchMot(token_array);
				System.out.println(s);
				pass1_output.println(s);
			}
			lclist.add(lc);
		}		
		System.out.println();
		
		int j;
		String output_tuple = new String();
		System.out.println("Symbol Table:");
		System.out.println("Symbol \t\t Value \t\t Length \t\t R/A");
		
		for(SymTuple i : symtable) {
			
			output_tuple = i.symbol + "\t\t" + i.value + "\t\t" + i.length + "\t\t" + i.ra;
			
			System.out.println(output_tuple);
			out_symtable.println(output_tuple);	
		}
		System.out.println("\nLiteral Table:");
		System.out.println("Literal \t\t Value \t\t Length \t\t R/A");
		for(LitTuple i : littable) {
			output_tuple = i.literal + "\t\t" + i.value + "\t\t" + i.length + "\t\t" + i.ra;
			System.out.println(output_tuple);
			out_littable.println(output_tuple);
		}
	}
	public static boolean searchPot(String[] s) {
		
		int i = 0;
		int l = 0;

		if(s.length == 3) {
				
			i = 1;
		}
		s = tokenize(s);
		if(s[i].equalsIgnoreCase("DS") || s[i].equalsIgnoreCase("DC")) {
			String x = s[i+1];
			int index = x.indexOf("F");
			if(i == 1) {
				symtable.add(new SymTuple(s[0], lc, 4, "R"));
			}
			if(index != 0) {
				// Ends with F
				l = Integer.parseInt(x.substring(0, x.length()-1));
				l *= 4;
			} 
			else {
				// Starts with F
				for(int j = i + 1; j < s.length; j++) {
					l += 4;
				}
			}
			lc += l;
			return true;
		
		}
		else if(s[i].equalsIgnoreCase("EQU")) {

			// EQU statement
			if(!s[2].equals("*")) {
				symtable.add(new SymTuple(s[0], Integer.parseInt(s[2]), 1, "A"));
			} 
			else {
				symtable.add(new SymTuple(s[0], lc, 1, "R"));
			}
			return true;
		
		}
		else if(s[i].equalsIgnoreCase("START")) {

			// START statement
			symtable.add(new SymTuple(s[0], Integer.parseInt(s[2]), 1, "R"));
			return true;
	
		}
		else if(s[i].equalsIgnoreCase("LTORG")) {
			
			// LTORG statement
			ltorg(false);
			return true;
		
		}
		else if(s[i].equalsIgnoreCase("END")) {
			
			// END statement
			ltorg(true);
			return true;
		
		}
		
		return false;
	
	}
	
	static void searchMot(String[] s) {
		
		Tuple t = new Tuple();
		int i = 0;
		
		if(s.length == 3) {
			
			i = 1;
		
		}
		s = tokenize(s);
		
		for(int j = i + 1; j < s.length; j++) {
			
			if(s[j].startsWith("=")) {
				
				littable.add(new LitTuple(s[j].substring(1, s[j].length()), -1, 4, "R"));
				
			}
		
		}
		if((i == 1) && (!s[0].equalsIgnoreCase("END"))) {
			
			symtable.add(new SymTuple(s[0], lc, 4, "R"));
		
		}
		for(Tuple x : mnem_table) {
			
			if(s[i].equals(x.mnemonic)) {
				
				t = x;
				break;
			}
		}
		lc += t.length;
	}	
	static String[] tokenize(String[] s) {
		
		List<String> temp = new LinkedList<>();
		
		for(int j = 0; j < s.length - 1; j++) {
			
			temp.add(s[j]);
		}
		StringTokenizer st = new StringTokenizer(s[s.length-1], " ,", false);
		
		while(st.hasMoreTokens()) {
		
			temp.add(st.nextToken());
		}
		
		s = temp.toArray(new String[0]);
		
		return s;
	
	}
	
	static void ltorg(boolean isEnd) {
		
		Iterator<LitTuple> itr = littable.iterator();
		
		LitTuple lt = new LitTuple();
		boolean flag = false;
		
		while(itr.hasNext()) {
			
			lt = itr.next();
			
			if(lt.value == -1) {
				
				flag = true;
				break;
			
			}
		
		}
		if(!flag){
			
			return;
		
		}
		if(!isEnd) {
			while(lc % 8 != 0) {
				lc++;
			}
		}
		lt.value = lc;
		lc += 4;
		while(itr.hasNext()) {
			lt = itr.next();
			lt.value = lc;
			lc += 4;
		}
	
	}
	
	static void pass2() throws Exception {
		
		output_pass2 = new PrintWriter(new FileWriter("C:\\Users\\Admin\\workspace\\Learning_Java\\src\\precodes\\output_tuple_pass2.txt"), true);
		
		BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Admin\\workspace\\Learning_Java\\src\\precodes\\pass1_op.txt")));
		String s;

		while((s = input.readLine()) != null) {
			
			//start = System.nanoTime();

			StringTokenizer st = new StringTokenizer(s, " ", false);
			String s_arr[] = new String[st.countTokens()];
			
			for(int i = 0; i < s_arr.length; i++) {
				
				s_arr[i] = st.nextToken();
			
			}
			
			if(searchPotPass2(s_arr) == false) {
				
				searchMotPass2(s_arr);
			
			}
			
			line_no++;
		
			//end = System.nanoTime();
			//total = total + (end - total);

		}	
	
		System.out.println("Pass 2 output:");
		
		input = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Admin\\workspace\\Learning_Java\\src\\precodes\\output_tuple_pass2.txt")));
		
		while((s = input.readLine()) != null) {
			
			System.out.println(s);
		
		}
	
	}
	
	static boolean searchPotPass2(String[] s) {
		
		int i = 0;
		
		if(s.length == 3) {
			
			i = 1;
		
		}
		if(Collections.binarySearch(po_table, s[i]) >= 0) {
			
			if(s[i].equalsIgnoreCase("USING")) {
				
				s = tokenize(s);
				
				if(s[i+1].equals("*")) {
					
					s[i+1] = lclist.get(line_no) + "";
				
				} 
				else {
					
					for(int j = i + 1; j < s.length; j++) {
						
						int value = getSymbolValue(s[j]);
						
						if(value != -1)	
							s[j] = value + "";
					}
				
				}
				
				basetable.put(new Integer(s[i + 2].trim()), new Integer(s[i + 1].trim()));
			
			}
			
			return true;
		
		}
		
		return false;
	
	}
	
	static void searchMotPass2(String[] s) {
		
		Tuple t = new Tuple();
		
		int i = 0;
		int j;
		
		if(s.length == 3) {
			
			i = 1;
		
		}
		s = tokenize(s);
		
		for(Tuple x : mnem_table) {
			
			if(s[i].equals(x.mnemonic)) {
				
				t = x;
				break;
			
			}
		
		}
		
		String output_tuple = new String();
		String mask = new String();
		
		if(s[i].equals("BNE"))
			mask = "7";
		else if(s[i].equals("BR")) 
			mask = "15";
		else 
			mask = "0";
		
		if(s[i].startsWith("B")) {
			
			if(s[i].endsWith("R"))
				s[i] = "BR";
			else 
				s[i] = "BC";
				
			List<String> temp = new ArrayList<>();
			
			for(String x : s) 
				temp.add(x);
			
			temp.add(i + 1, mask);
			
			s = temp.toArray(new String[0]);
		
		}
		if(t.type.equals("RR")) {
			
			output_tuple = s[i];
			
			for(j = s[i].length(); j < 6; j++)
				output_tuple += " ";
			
			for(j = i + 1; j < s.length; j++) {
				int value = getSymbolValue(s[j]);
				if(value != -1) {
					s[j] = value + "";
				}
			}
			
			output_tuple += s[i+1];
			for(j = i + 2; j <s .length ; j++) {
				output_tuple += ", " + s[j];
			}
		} 
		else {
			
			output_tuple = s[i];
			
			for(j = s[i].length(); j < 6; j++)
				output_tuple += " ";
			
			for(j = i + 1; j < s.length - 1; j++) {
				int value = getSymbolValue(s[j]);
				if(value != -1) {
					s[j] = value + "";
				}
			}
			
			s[j] = createOffset(s[j]);
			
			output_tuple += s[i+1];
			
			for(j = i + 2; j < s.length; j++) {
				
				output_tuple += ", " + s[j];
			
			}
		}

		output_pass2.println(output_tuple);

	}

	static String createOffset(String s) {

		String original = s;
		Integer[] key = basetable.keySet().toArray(new Integer[0]);

		int offset, new_offset;
		int index = 0;
		int value = -1;
		int index_reg = 0;

		if(s.startsWith("="))
			value = getLiteralValue(s);
		else {

			int paranthesis = s.indexOf("(");

			String index_string = new String();

			if(paranthesis != -1) {

				s = s.substring(0, s.indexOf("("));
				index_string = original.substring(original.indexOf("(")+1, original.indexOf(")"));
				index_reg = getSymbolValue(index_string);

			}

			value = getSymbolValue(s);
		}

		offset = Math.abs(value - basetable.get(key[index]));

		for(int i = 1; i < key.length; i++) {

			new_offset = Math.abs(value - basetable.get(key[i]));

			if(new_offset < offset) {

				offset = new_offset;
				index = i;

			}
		}
		String result = offset + "(" + index_reg + ", " + key[index] + ")";
		return result;
	}

	static int getSymbolValue(String s) {

		for(SymTuple st : symtable) {

			if(s.equalsIgnoreCase(st.symbol))
				return st.value;


		}

		return -1;

	}

	static int getLiteralValue(String s) {

		s = s.substring(1, s.length());

		for(LitTuple lt : littable) {

			if(s.equalsIgnoreCase(lt.literal)) {

				return lt.value;

			}

		}

		return -1;

	}
	
}