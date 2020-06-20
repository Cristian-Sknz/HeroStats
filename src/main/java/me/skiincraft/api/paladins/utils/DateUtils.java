package me.skiincraft.api.paladins.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static Date parseDate(String data) {
		// Format of the date defined in the input String
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
		// Desired format: 24 hour format: Change the pattern as per the need
		DateFormat outputformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		Date date = null;
		String output = null;
		try {
			// Converting the input String to Date
			date = df.parse(data);
			// Changing the format of date and storing it in String
			output = outputformat.format(date);
			// Displaying the date
			return outputformat.parse(output);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return date;
	}

}
