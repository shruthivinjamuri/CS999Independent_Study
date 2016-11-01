package AcquireGame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AcquireStatistics {
	public static final int sharesPerHotel = 25;
	public static final double playerFund = 6000;
	public static final int safeHotelSize = 11;
	public static final int rows = 9;
	public static final int columns = 12;
	public static final Set<String> hotels = new HashSet<String>(Arrays.asList(
			"Radisson", "Hilton", "Hyatt", "InterContinental", "Westin", "Sheraton", "Potawatomi"));
	
	public static boolean isValidRow(char row) {
		return (row >= 'A' && row <= 'F');
	}

	public static boolean isValidCol(int col) {
		return (col >= 1 && col <= 12);
	}

	public static boolean isValidHotelName(String hotelName) {
		return hotels.contains(hotelName);
	}

	public static double singleShareValue(String hotelName, int hotelSize) {
		switch (hotelName) {
		case "Radisson":
		case "Hilton":
			if (hotelSize >= 0 && hotelSize < 2)
				return 300.0;
			if (hotelSize >= 2 && hotelSize < 3)
				return 400.0;
			if (hotelSize >= 3 && hotelSize < 4)
				return 500.0;
			if (hotelSize >= 4 && hotelSize < 5)
				return 600.0;
			if (hotelSize >= 5 && hotelSize < 6)
				return 700.0;
			if (hotelSize >= 6 && hotelSize < 11)
				return 800.0;
			if (hotelSize >= 11 && hotelSize < 21)
				return 900.0;
			if (hotelSize >= 21 && hotelSize < 31)
				return 1000.0;
			if (hotelSize >= 31 && hotelSize < 41)
				return 1100.0;
			if (hotelSize == 41)
				return 1200.0;
		case "Hyatt":
		case "InterContinental":
		case "Westin":
			if (hotelSize >= 0 && hotelSize < 2)
				return 200.0;
			if (hotelSize >= 2 && hotelSize < 3)
				return 300.0;
			if (hotelSize >= 3 && hotelSize < 4)
				return 400.0;
			if (hotelSize >= 4 && hotelSize < 5)
				return 500.0;
			if (hotelSize >= 5 && hotelSize < 6)
				return 600.0;
			if (hotelSize >= 6 && hotelSize < 11)
				return 700.0;
			if (hotelSize >= 11 && hotelSize < 21)
				return 800.0;
			if (hotelSize >= 21 && hotelSize < 31)
				return 900.0;
			if (hotelSize >= 31 && hotelSize < 41)
				return 1000.0;
			if (hotelSize == 41)
				return 1100.0;
		case "Sheraton":
		case "Potawatomi":
			if (hotelSize >= 2 && hotelSize < 3)
				return 200.0;
			if (hotelSize >= 3 && hotelSize < 4)
				return 300.0;
			if (hotelSize >= 4 && hotelSize < 5)
				return 400.0;
			if (hotelSize >= 5 && hotelSize < 6)
				return 500.0;
			if (hotelSize >= 6 && hotelSize < 11)
				return 600.0;
			if (hotelSize >= 11 && hotelSize < 21)
				return 700.0;
			if (hotelSize >= 21 && hotelSize < 31)
				return 800.0;
			if (hotelSize >= 31 && hotelSize < 41)
				return 900.0;
			if (hotelSize == 41)
				return 1000.0;
		default:
			return -1.0;

		}
	}
	
	public static HashMap<Integer,Character> rowMapping(){
		HashMap<Integer,Character> output = new HashMap<>();
		output.put(1, 'A');
		output.put(2, 'B');
		output.put(3, 'C');
		output.put(4, 'D');
		output.put(5, 'E');
		output.put(6, 'F');
		output.put(7, 'G');
		output.put(8, 'H');
		output.put(9, 'I');
		return output;
		
	}
	
	public static HashMap<String,Integer> rowIdx(){
		HashMap<String, Integer> output = new HashMap<>();
		output.put("A", 1);
		output.put("B", 2);
		output.put("C", 3);
		output.put("D", 4);
		output.put("E", 5);
		output.put("F", 6);
		output.put("G", 7);
		output.put("H", 8);
		output.put("I", 9);
		return output;
		
	}

	public static enum TilePlacementType {
	     Growing, Merging, Singleton, Founding, Impossible, Error
	}
}
