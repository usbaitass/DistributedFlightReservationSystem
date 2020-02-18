package DFRS;

import java.io.Serializable;

/**
 * @author ulanbaitassov
 * This class used for Passenger records which contains relevant information about a passenger 
 * implements Serializable in order to permit saving class objects in file
 */
public class Passenger implements Serializable{
	private static final long serialVersionUID = 1L;
	private String firstName = "";
	private String lastName = "";
	private String address = "";
	private String phoneNumber = "";
	private String destination = "";
	private String classFlight = "";
	private String dateFlight = "";
	private String recordID = "";
	private String departure = "";
	
	/**
	 * Default constructor
	 */
	public Passenger(){}
	
	/**
	 * Constructor to create passenger record
	 * @param new_fname first name
	 * @param new_lname last name
	 * @param new_addr address
	 * @param new_phone phone number
	 * @param new_dep departure city
	 * @param new_dest destination city
	 * @param new_fclass flight class
	 * @param new_fdate date of flight
	 * @param new_r unique record ID 
	 */
	public Passenger(String new_fname, String new_lname, String new_addr, String new_phone, String new_dep, String new_dest, String new_fclass, String new_fdate, String new_r){	
		firstName = new_fname;
		lastName = new_lname;
		address = new_addr;
		phoneNumber = new_phone;
		destination = new_dest;
		departure = new_dep;
		classFlight = new_fclass;
		dateFlight = new_fdate;		
		recordID = new_r;
	}
	
	/**
	 * Get method which returns first name of the passenger
	 * @return first name
	 */
	public String getFirstName(){
		return firstName;
	}
	
	/**
	 * Get method which returns last name of the passenger
	 * @return last name
	 */
	public String getLastName(){
		return lastName;
	}
	
	/**
	 * Get method which returns address of the passenger
	 * @return address
	 */
	public String getAddress(){
		return address;
	}
	
	/**
	 * Get method which returns phone number of the passenger
	 * @return phone number
	 */
	public String getPhone(){
		return phoneNumber;
	}
	
	/**
	 * Get method which returns flight destination of the passenger
	 * @return destination
	 */
	public String getDestination(){
		return destination;
	}
	
	/**
	 * Get method which returns class flight of the passenger
	 * @return class flight
	 */
	public String getClassFlight(){
		return classFlight;
	}
	
	/**
	 * Get method which returns date of flight
	 * @return flight date
	 */
	public String getDateFlight(){
		return dateFlight;
	}
	
	/**
	 * Get method which returns passenger record ID associated with the flight
	 * @return record ID
	 */
	public String getRecordID(){
		return recordID;
	}
	
	/**
	 * Get method which returns departure city from where passenger is flying
	 * @return departure city
	 */
	public String getDeparture(){
		return departure;
	}
	
}
