package lab;

//Java Program to illustrate the Hashmap Class

//Importing required classes
import java.util.*;

//Main class
public class GFG {

	// Main driver method
	public static void main(String[] args)
	{

		// Creating an empty HashMap
		Map<String, Integer> map = new HashMap<>();

		// Inserting entries in the Map
		// using put() method
		map.put("vishal", 10);
		map.put("sachin", 30);
		map.put("vaibhav", 20);

		// Iterating over Map
		for (Map.Entry<String, Integer> e : map.entrySet())

			// Printing key-value pairs
			System.out.println(e.getKey() + " "
							+ e.getValue());
	}
}

