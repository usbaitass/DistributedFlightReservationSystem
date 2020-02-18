package DFRS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author ulanbaitassov
 * This class used for Flight objects and which contains relevant information about a flight 
 * implements Serializable in order to permit saving class objects in file
 */
public class Flight implements Serializable{
	private static final long serialVersionUID = 1L;
	private int firstClass = 3; // maximum seat capacity for first class
	private int businessClass = 3; // maximum seat capacity for business class
	private int economyClass = 3; // maximum seat capacity for economy class
	private String departure = "";
	private String destination = "";
	private String dateFlight = "";
	private String timeFlight = "";	
	private List<ArrayList<String>> records;
	
	/**
	 * Default constructor
	 */
	public Flight(){
		records = Collections.synchronizedList(new ArrayList<ArrayList<String>>());
		records.add(new ArrayList<String>());
		records.add(new ArrayList<String>());
		records.add(new ArrayList<String>());
	}
	
	/**
	 * Constructor
	 * @param new_dep departure city
	 * @param new_dest destination city
	 * @param new_d date of flight
	 * @param new_t time of flight
	 */
	public Flight(String new_dep, String new_dest, String new_d, String new_t){
		departure = new_dep;
		destination = new_dest;
		dateFlight = new_d;
		timeFlight = new_t;
		records = Collections.synchronizedList(new ArrayList<ArrayList<String>>());
		records.add(new ArrayList<String>());
		records.add(new ArrayList<String>());
		records.add(new ArrayList<String>());
	}
	
	/**
	 * Get method which returns maximum seats number for first class
	 * @return maximum seats number for first class
	 */
	public int getFirstClass(){
		return firstClass;
	}
	
	/**
	 * Get method which returns maximum seats number for business class
	 * @return maximum seats number for business class
	 */
	public int getBusinessClass(){
		return businessClass;
	}
	
	/**
	 * Get method which returns maximum seats number for economy class
	 * @return maximum seats number for economy class
	 */
	public int getEconomyClass(){
		return economyClass;
	}
	
	/**
	 * Get method which returns total maximum seats number for all classes
	 * @return total maximum seats number for all classes
	 */
	public int getAllClass(){
		return firstClass+businessClass+economyClass;
	}

	/**
	 * Get method which returns destination city name 
	 * @return destination city name
	 */
	public String getDestinationFlight(){
		return destination;
	}
	
	/**
	 * Get method which returns date of flight 
	 * @return date of flight
	 */
	public String getDateFlight(){
		return dateFlight;
	}
	
	/**
	 * Get method which returns time of flight
	 * @return time of flight
	 */
	public String getTimeFlight(){
		return timeFlight;
	}	
	
	/**
	 * Get method which returns departure city
	 * @return departure city
	 */
	public String getDepartureCity(){
		return departure;
	}
	
	/**
	 * Get method which returns number of occupied seats in first class
	 * @return number of occupied seats in first class
	 */
	public int getFirstClassOccupied(){
		return records.get(0).size();
	}
	
	/**
	 * Get method which returns number of occupied seats in business class
	 * @return number of occupied seats in business class
	 */
	public int getBusinessClassOccupied(){
		return records.get(1).size();
	}
	
	/**
	 * Get method which returns number of occupied seats in economy class
	 * @return number of occupied seats in economy class
	 */
	public int getEconomyClassOccupied(){
		return records.get(2).size();
	}
	
	/**
	 * Method which inserts passenger record ID in flight record list
	 * @param new_class class number 0=first 1=business 2=economy
	 * @param new_r record ID
	 * @return true if successfully added in the list
	 */
	public synchronized boolean insertRecord(int new_class, String new_r){
		records.get(new_class).add(new_r);
		return true;
	}
	
	/**
	 * Method which changes the flight information to new values
	 * @param new_date new date of flight
	 * @param new_t new time of flight
	 * @param new_f new maximum capacity limit for first class
	 * @param new_b new maximum capacity limit for business class
	 * @param new_e new maximum capacity limit for economy class
	 */
	public synchronized void changeFlightData(String new_date, String new_t, int new_f, int new_b, int new_e){
		dateFlight = new_date;
		timeFlight = new_t;	
		if(records.get(0).size()>new_f){
			shrinkRecords(0, new_f);
		}
		if(records.get(1).size()>new_b){
			shrinkRecords(1, new_b);
		}
		if(records.get(2).size()>new_e){
			shrinkRecords(2, new_e);
		}
		firstClass = new_f;
		businessClass = new_b;
		economyClass = new_e;
	}
	
	/**
	 * This method deletes most recent passenger records if the flight size is reduced the minimum limit capacity
	 * @param new_class class name such as first, business, economy in integer value 0,1,2 respectively
	 * @param new_n new maximum capacity limit
	 */
	public synchronized void shrinkRecords(int new_class, int new_n){
		synchronized(records.get(new_class)){
			for(int i=records.get(new_class).size(); i>new_n; i--){
				records.get(new_class).remove(i-1);
			}
		}
	}
	
	/**
	 * Get method which returns the number of occupied seats in a given class
	 * @param recordType class name
	 * @return number of occupied seats in given class, returns zero if no proper class found
	 */
	public int getNumberRecords(String recordType){
		recordType = recordType.trim();
		if(recordType.equalsIgnoreCase("first")){
			return this.getFirstClassOccupied();
		}else if(recordType.equalsIgnoreCase("business")){
			return this.getBusinessClassOccupied();
		}else if(recordType.equalsIgnoreCase("economy")){
			return this.getEconomyClassOccupied();
		}else if(recordType.equalsIgnoreCase("all")){
			return this.getFirstClassOccupied()+this.getBusinessClassOccupied()+this.getEconomyClassOccupied();
		}else{
			return 0;
		}
	}
	
	public String findRecordInFlight(String new_id){
		for(int i=0; i<records.size(); i++){
			synchronized(records.get(i)){
				for(int j=0; j<records.get(i).size(); j++){
					if(new_id.equalsIgnoreCase(records.get(i).get(j))){
						return i+";"+j;
					}
				}
			}
		}
		return "no";
	}
	
	public synchronized boolean deleteRecord(int new_class, String new_id){
		for(int i=0; i<records.get(new_class).size(); i++){
			synchronized(records.get(new_class)){
				if(new_id.equalsIgnoreCase(records.get(new_class).get(i))){
					records.get(new_class).remove(i);
					return true;
				}
			}
		}
		return false;
	}
	
}
