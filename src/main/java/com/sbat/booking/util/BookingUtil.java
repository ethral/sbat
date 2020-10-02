package com.sbat.booking.util;

import org.springframework.stereotype.Component;


public class BookingUtil {
	
	
	public static String normalizeString(String input) {
		
		String output= "";
		
		if (input.length() == 1) {
			output = output.concat("0").concat(input);
			
			return output;
		}
		
		return input;
	}

}
