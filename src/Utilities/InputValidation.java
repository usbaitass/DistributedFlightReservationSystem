package Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import Models.Enums;
import StaticContent.StaticContent;

/**
 * Validate User inputs 
 * @author SajjadAshrafCan
 *
 */
public class InputValidation {

	private static final Pattern NUMBERS = Pattern.compile("\\d+");
	//private static final Pattern LETTERS_WITHOUT_SPACES = Pattern.compile("\\p{Alpha}+");//
	private static final Pattern LETTERS_WITHOUT_SPACES = Pattern.compile("\\w+$");//
	private static final Pattern LETTERS_WITH_SPACES = Pattern.compile("[a-zA-Z ]*");
	
	/**
	 * input Integer
	 * @param scanner
	 * @return
	 */
	public static int inputInteger(Scanner scanner) {
		int i = 0;
		while (!scanner.hasNextInt()) {
			System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_NUMBER);
			scanner.next();
		}
		i = scanner.nextInt();
		return i;
	}
	
	/**
	 * input Phone Number 
	 * @param scanner
	 * @return
	 */
	public static String inputPhoneNumber(Scanner scanner) {
		String value = "";
		value = scanner.next().trim();
		while (!isPhoneNumber(value)) {
			System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_PHONE);
			value = scanner.next().trim();
		}
		return value;
	}
	
	/**
	 * input String
	 * @param scanner
	 * @return
	 */
	public static String inputString(Scanner scanner) {
		String value = "";
		value = scanner.nextLine().trim();
		while (!isAlphaNoSpace(value) || value.contains(StaticContent.VALUES_SEPARATOR) || value.length() == 0) {
			
			if(value.contains(StaticContent.VALUES_SEPARATOR))
				System.out.println(StaticContent.ERROR_SEPARATOR_FIELD_MSG);
			else if(value.length() == 0)
				System.out.println(StaticContent.ERROR_REQUIRED_MSG);
			else
				System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_ALPHA_NUMERIC_NO_SPACES);
			
			value = scanner.nextLine().trim();
		}
		return value;

	}
	
	/**
	 * input String With Spaces
	 * @param scanner
	 * @return
	 */
	public static String inputStringWithSpaces(Scanner scanner) {
		String value = "";
		value = scanner.nextLine().trim();
		while (!isAlphaWithSpace(value) || value.contains(StaticContent.VALUES_SEPARATOR) || value.length() == 0) {
			
			if(value.contains(StaticContent.VALUES_SEPARATOR))
				System.out.println(StaticContent.ERROR_SEPARATOR_FIELD_MSG);
			else if(value.length() == 0)
				System.out.println(StaticContent.ERROR_REQUIRED_MSG);
			else
				System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_ALPHA_NUMERIC_WITH_SPACES);
			
			value = scanner.nextLine().trim();
		}
		return value;
	}
	
	/**
	 * input Full City
	 * @param scanner
	 * @return
	 */
	public static String inputFullCity(Scanner scanner) {
		String value = scanner.next().trim();
		while (!isValidFlightCity(value)) {
			System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_CITY_NAME);
			value = scanner.next().trim();
		}
		return value;
	}
	
	/**
	 * input Short City
	 * @param scanner
	 * @return
	 */
	public static String inputShortCity(Scanner scanner) {

		String value = scanner.next().trim();
		while (!isValidShortCity(value)) {
			System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_SHORTCITY);
			value = scanner.next().trim();
		}
		return value;
	}

	/**
	 * 	input Flight Class
	 * @param scanner
	 * @return
	 */
	public static String inputFlightClass(Scanner scanner) {

		String value = scanner.next().trim();
		while (!isValidFlightClass(value)) {
			System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_FLIGHT_CLASS);
			value = scanner.next().trim();
		}
		return value;
	}

	/**
	 * input Field Name
	 * @param scanner
	 * @return
	 */
	public static String inputFieldName(Scanner scanner) {
		String value = scanner.next().trim();
		while (!isValidFieldName(value)) {
			System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_FLIGHT_FIELDS);
			value = scanner.next().trim();
		}
		return value;
	}

	public static String inputRecordTypeFlightCount(Scanner scanner) {
		String value = scanner.next().trim();
		if (!value.toLowerCase().equals("all")) {
			while (!isValidFlightClass(value)) {
				System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_RECORD_TYPE);
				value = scanner.next().trim();
			}
		}
		return value;
	}
	
	/**
	 * input New Value Of Field
	 * @param scanner
	 * @param fieldName
	 * @return
	 */
	public static String inputNewValueOfField(Scanner scanner, String fieldName) {

		String newValue = scanner.next().trim();

		while (!isValidNewValue(newValue, fieldName)) {
			newValue = scanner.next().trim();
		}
		return newValue;
	}
	
	/**
	 * input Date
	 * @param scanner
	 * @return
	 */
	public static String inputDate(Scanner scanner) {

		String value = scanner.next().trim();
		while (!isStringHasValidDateTime(value, StaticContent.dateFormat)) {
			System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_DATE);
			value = scanner.next().trim();
		}
		return value;
	}
	
	/**
	 * input Time
	 * @param scanner
	 * @return
	 */
	public static String inputTime(Scanner scanner) {

		String value = scanner.next().trim();
		while (!isStringHasValidDateTime(value, StaticContent.timeFormat)) {
			System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_TIME);
			value = scanner.next().trim();
		}
		return value;
	}
	
	/**
	 * is Numeric 
	 * @param text
	 * @return
	 */
	public static final boolean isNumeric(String text) {
		return NUMBERS.matcher(text).matches();
	}
	
	/**
	 * is Phone Number
	 * @param text
	 * @return
	 */
	public static final boolean isPhoneNumber(String text) {
		return NUMBERS.matcher(text).matches() && text.length() == 10;
	}
	
	/**
	 * is Alpha numeric string with No Space
	 * @param text
	 * @return
	 */
	public static final boolean isAlphaNoSpace(String text) {
		return LETTERS_WITHOUT_SPACES.matcher(text).matches();
	}
	
	/**
	 * is Alpha numeric string with Space
	 * @param text
	 * @return
	 */
	public static final boolean isAlphaWithSpace(String text) {
		return LETTERS_WITH_SPACES.matcher(text).matches();
	}
	
	/**
	 * is Valid Flight City
	 * @param filedsName
	 * @return
	 */
	public static boolean isValidFlightCity(String filedsName) {
		try {
			return Enums.FlightCities.valueOf(filedsName) != null && !Enums.FlightCities.valueOf(filedsName).equals("");
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	public static boolean isValidShortCity(String filedsName) {
		try {
			return Enums.FlightCitiesShrot.valueOf(filedsName) != null
					&& !Enums.FlightCitiesShrot.valueOf(filedsName).equals("");
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	/**
	 * is Valid Flight Class
	 * @param filedsName
	 * @return
	 */
	public static boolean isValidFlightClass(String filedsName) {
		try {
			return Enums.Class.valueOf(filedsName) != null && !Enums.Class.valueOf(filedsName).equals("");
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	/**
	 * is Valid Field Name
	 * @param filedsName
	 * @return
	 */
	public static boolean isValidFieldName(String filedsName) {
		try {
			return Enums.FlightFileds.valueOf(filedsName) != null && !Enums.FlightFileds.valueOf(filedsName).equals("");
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	/**
	 * is String Has Valid DateTime
	 * @param strDate
	 * @param format
	 * @return
	 */
	public static boolean isStringHasValidDateTime(String strDate, String format) {
		boolean status = false;
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			Date output = dateFormat.parse(strDate);
			String value = dateFormat.format(output);
			status = strDate.equals(value);
		} catch (Exception ignore) {
		}

		return status;
	}

	/**
	 * check String Has Valid Date Time
	 * @param strDate
	 * @param format
	 * @return
	 */
	public static boolean checkStringHasValidDateTime(String strDate, String format) {
		boolean status = false;
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			Date output = dateFormat.parse(strDate);
			String value = dateFormat.format(output);
			status = strDate.equals(value);
		} catch (Exception ignore) {
		}

		return status;
	}
	
	/**
	 * is Valid New Value
	 * @param newValue
	 * @param _fieldName
	 * @return
	 */
	public static boolean isValidNewValue(String newValue, String _fieldName) {
		boolean status = false;
		try {
			Enums.FlightFileds fieldName = Enums.FlightFileds.valueOf(_fieldName);
			switch (fieldName) {

			case flightDate:
				status = isStringHasValidDateTime(newValue, StaticContent.dateFormat);
				if (!status)
					System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_DATE);
				break;
			case flightTime:
				status = isStringHasValidDateTime(newValue, StaticContent.timeFormat);
				if (!status)
					System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_TIME);
				break;
			case destinaition:
			case source:
				status = isValidFlightCity(newValue);
				if (!status)
					System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_CITY_NAME);
				break;
			case seatsInFirstClass:
			case seatsInBusinessClass:
			case seatsInEconomyClass:
				status = isNumeric(newValue);
				if (!status)
					System.out.println(StaticContent.ERROR_GENERAL_MSG + StaticContent.MSG_NUMBER);
				break;

			default:
				break;
			}

		} catch (Exception ignore) {
		}

		return status;
	}

}
