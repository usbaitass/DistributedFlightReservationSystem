package DFRS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class BookingImpService {
	
	private Logger logger;	
	private HashMap<Character, List<Passenger>> hmap; //passengers hash map
	private HashMap<String, List<Flight>> hmapFlights; //flights hash map
	private String nameServer = "";
	private String nameServerABR = "";
	private int idCur = 1;
	public int countFlight = 1;
	
	
	
	public BookingImpService(String new_sn, String new_abr){
		nameServer = new_sn;
		nameServerABR = new_abr;
	}
	
	public Logger getLogger(){
		return logger;
	}
	
	public void setLogger(Logger new_log){
		logger = new_log;
	}
	
	public String getNameServer(){
		return nameServer;
	}	
	
	/**
	 * Get method which returns the server's passengers hash map
	 * @return passenger hash map
	 */
	public HashMap<Character, List<Passenger>> getHashMap() {
		return hmap;
	}
	
	/**
	 * Get method which returns the server's flights hash map
	 * @return flight hash map
	 */
	public HashMap<String, List<Flight>> getHashMapFlight() {
		return hmapFlights;
	}
	
	/**
	 * Sets the loaded passenger hash map from the file to server hash map
	 * @param new_hmap passenger hash map reference
	 */
	public void setHashMap(HashMap<Character, List<Passenger>> new_hmap) {
		hmap = new_hmap;
	}

	/**
	 * Sets the loaded flight hash map from the file to server hash map
	 * @param new_hmap flight hash map reference
	 */
	public void setHashMapFlight(HashMap<String, List<Flight>> new_hmap) {
		hmapFlights = new_hmap;
	}

	/**
	 * This method creates new flight in flight hash map
	 * @param recordID manager ID
	 * @param destination flight destination
	 * @param date flight date
	 * @param time flight time
	 * @return true if successfully created
	 */
	public boolean createNewFlight(String recordID, String destination, String date, String time){
		Flight flight = new Flight(nameServer, destination, date, time);
		if (hmapFlights.get(destination + date) == null) {
			//create new key if no key present in hash map
			synchronized(hmapFlights){
				hmapFlights.put(destination + date, Collections.synchronizedList(new ArrayList<Flight>()));
			}
		}
		synchronized(hmapFlights.get(destination + date)){
			hmapFlights.get(destination + date).add(flight);
		}
		logger.info("CREATED FLIGHT from "+nameServer+" to "+ flight.getDestinationFlight()+" on " + flight.getDateFlight() + " at " + flight.getTimeFlight()+" first class: " + flight.getFirstClassOccupied() + "/" + flight.getFirstClass()+" business class: " + flight.getBusinessClassOccupied() + "/" + flight.getBusinessClass()+" economy class: " + flight.getEconomyClassOccupied() + "/" + flight.getEconomyClass()+" by manager: "+recordID);
		//System.out.print("CREATED FLIGHT from "+nameServer+" to "+ flight.getDestinationFlight()+" on " + flight.getDateFlight() + " at " + flight.getTimeFlight()+" first class: " + flight.getFirstClassOccupied() + "/" + flight.getFirstClass()+" business class: " + flight.getBusinessClassOccupied() + "/" + flight.getBusinessClass()+" economy class: " + flight.getEconomyClassOccupied() + "/" + flight.getEconomyClass()+" by manager: "+recordID);
		return true;
	}
	

	/**
	 * This method re-implements the edit of the flight information based on new values
	 * @param recordID manager ID
	 * @param dest destination
	 * @param date current flight date
	 * @param time current flight time
	 * @param new_date new flight date to be assigned
	 * @param new_time new flight time to be assigned
	 * @param new_f new maximum capacity limit for flight's first class
	 * @param new_b new maximum capacity limit for flight's business class
	 * @param new_e new maximum capacity limit for flight's economy class
	 * @return 1 if successful, 0 if failed
	 */
	public String editFlight(String recordID, String dest, String date, String time, String new_date, String new_time, String new_f, String new_b, String new_e){
		if(hmapFlights.get(dest + date) != null){
			//find value in hash map using key which is composition of destination and date of flight
			List<Flight> temp = hmapFlights.get(dest + date);
			int k=-1;
			for(int i=0; i<temp.size(); i++){
				//find position of the flight in hash map by fight time value
		    	if(time.equalsIgnoreCase(temp.get(i).getTimeFlight())){
		    		k=i;
		    		break;
		    	}
		    }
			if(k!=-1){
				//perform EDIT method on Flight
				logger.info("EDITED FLIGHT Previous: "+temp.get(k).getDestinationFlight()+" "+temp.get(k).getDateFlight()+" "+temp.get(k).getTimeFlight()+" "+temp.get(k).getFirstClass()+" "+temp.get(k).getBusinessClass()+" "+temp.get(k).getEconomyClass()+" by manager: "+recordID);
				//System.out.println("Previous: "+temp.get(k).getDestinationFlight()+" "+temp.get(k).getDateFlight()+" "+temp.get(k).getTimeFlight()+" "+temp.get(k).getFirstClass()+" "+temp.get(k).getBusinessClass()+" "+temp.get(k).getEconomyClass()+" by manager: "+recordID);
				temp.get(k).changeFlightData(new_date, new_time, Integer.parseInt(new_f), Integer.parseInt(new_b), Integer.parseInt(new_e));
				logger.info("EDITED FLIGHT New: "+temp.get(k).getDestinationFlight()+" "+temp.get(k).getDateFlight()+" "+temp.get(k).getTimeFlight()+" "+temp.get(k).getFirstClass()+" "+temp.get(k).getBusinessClass()+" "+temp.get(k).getEconomyClass()+" by manager: "+recordID);
				//System.out.println("New: "+temp.get(k).getDestinationFlight()+" "+temp.get(k).getDateFlight()+" "+temp.get(k).getTimeFlight()+" "+temp.get(k).getFirstClass()+" "+temp.get(k).getBusinessClass()+" "+temp.get(k).getEconomyClass()+" by manager: "+recordID);
				if(!date.equalsIgnoreCase(new_date)){
					Flight flight = temp.get(k);
					if (hmapFlights.get(dest + new_date) == null) {
						//create new key if no key present in hash map
						synchronized(hmapFlights){
							hmapFlights.put(dest + new_date, Collections.synchronizedList(new ArrayList<Flight>()));
						}
					}
					synchronized(temp){
						hmapFlights.get(dest + new_date).add(flight);
						temp.remove(k);		
					}
				}
				return "true";
			}else{
				return "false";
			}
		}else{
			return "false";
		}
	}
	
	public String showAllFlight(){
		String str = "";
		Set set = hmapFlights.entrySet();
	    Iterator iterator = set.iterator();
	    while(iterator.hasNext()) {
	       Map.Entry mentry = (Map.Entry)iterator.next();
	       List<Flight> temp = (List<Flight>) mentry.getValue();
	       for(int i=0; i<temp.size(); i++){
	    	   str += temp.get(0).getDepartureCity()+" "+temp.get(0).getDestinationFlight()+" "+temp.get(0).getDateFlight()+" "+temp.get(0).getTimeFlight()+" fclass="+temp.get(0).getFirstClassOccupied()+"/"+temp.get(0).getFirstClass()+" bclass="+temp.get(0).getBusinessClassOccupied()+"/"+temp.get(0).getBusinessClass()+" eclass="+temp.get(0).getEconomyClassOccupied()+"/"+temp.get(0).getEconomyClass()+";";
	       }
	    }
		return str;
	}
	
	/**
	 * This method performs the deletion of the flight from flight hash map
	 * @param recordID manager ID
	 * @param dest flight destination
	 * @param date flight date
	 * @param time flight time
	 * @return 1 if successful, 0 if failed
	 */
	public String deleteFlight(String recordID, String dest, String date, String time){
		if(hmapFlights.get(dest + date) != null){
			//find value in hash map using key which is composition of destination and date of flight
			List<Flight> temp = hmapFlights.get(dest + date);
			int k=-1;
			for(int i=0; i<temp.size(); i++){
				//find position of the flight in hash map by fight time value
		    	if(time.equalsIgnoreCase(temp.get(i).getTimeFlight())){
		    		k=i;
		    		break;
		    	}
		    }
			if(k!=-1){
				//perform DELETION
				logger.info("DELETED FLIGHT: "+temp.get(k).getDestinationFlight()+" "+temp.get(k).getDateFlight()+" "+temp.get(k).getTimeFlight()+" "+temp.get(k).getFirstClass()+" "+temp.get(k).getBusinessClass()+" "+temp.get(k).getEconomyClass()+" by manager: "+recordID);
				//System.out.println("DELETED: "+temp.get(k).getDestinationFlight()+" "+temp.get(k).getDateFlight()+" "+temp.get(k).getTimeFlight()+" "+temp.get(k).getFirstClass()+" "+temp.get(k).getBusinessClass()+" "+temp.get(k).getEconomyClass()+" by manager: "+recordID);
				synchronized(temp){
					temp.remove(k);
				}
				if(temp.isEmpty()){
					//delete key from hash map if no values in hash map with such key
					synchronized(hmapFlights){
						hmapFlights.remove(dest+date);
					}
				}
				return "true";
			}else{
				return "false";
			}
		} 
		return "false";
	}
	
	/**
	 * This method adds passenger record into the passenger hash map
	 * @param firstName passenger's first name
	 * @param lastName passenger's last name
	 * @param address passenger's home address 
	 * @param phone passenger's phone number
	 * @param destination flight destination
	 * @param classFlight flight class
	 * @param date flight date
	 * @return unique passenger record ID
	 */
	public long addPassengerRecord(String firstName, String lastName, String address, String phone, String destination, String classFlight, String date) {
			long id = idCur;
			String r_id = Long.toString(id);
			Passenger p = new Passenger(firstName, lastName, address, phone, nameServer, destination, classFlight, date, r_id);
			if (hmap.get(p.getLastName().charAt(0)) == null) {
				//create the hash map key if doesn't exist
				synchronized(hmap){
					hmap.put(p.getLastName().charAt(0), Collections.synchronizedList(new ArrayList<Passenger>()));
				}
			}
			synchronized(hmap.get(p.getLastName().charAt(0))){
				hmap.get(p.getLastName().charAt(0)).add(p);
			}
			logger.info("PASSENGER BOOKED FLIGHT Record: "+r_id+" "+p.getFirstName()+" "+p.getLastName()+" "+p.getDestination()+" "+p.getDateFlight()+" "+p.getAddress()+" "+p.getClassFlight()+" "+p.getPhone());
			//System.out.println("Record: "+r_id+" "+p.getFirstName()+" "+p.getLastName()+" "+p.getDestination()+" "+p.getDateFlight()+" "+p.getAddress()+" "+p.getClassFlight()+" "+p.getPhone()+" was added to Passenger Record DataBase.");
			idCur++;
			return id;
		}
	
	/**
	 * Get method which returns number of passengers who booked the flight on this server
	 * @param recordType flight class
	 * @return number of passengers with server abbreviation name
	 */
	public String getBookedCountFlight2(String recordType){
		int count = 0;
		Set set = hmapFlights.entrySet();
		Iterator iterator = set.iterator();
		//traverse whole flight hash map
	    while(iterator.hasNext()) {
	       Map.Entry mentry = (Map.Entry)iterator.next();
	       //System.out.println("key is: "+ mentry.getKey() + " & Value is: "+mentry.getValue());
	       List<Flight> temp = (List<Flight>)mentry.getValue();
	       if(!temp.isEmpty()){
	    	   for(int i=0; i<temp.size(); i++){
	    		   count+=temp.get(i).getNumberRecords(recordType);
		       }  
	       }
	    }
	    //System.out.println("HERE "+serverNameABR+";"+count+";");
	    return nameServerABR+" "+count+",";
	}
	
	/**
	 * This method deletes the passenger record from flight hash map
	 * @param key hash map key
	 * @param new_class passenger flight class
	 * @param new_id passenger record id
	 * @return true if deletion is succeeded
	 */
	public boolean deleteRecordFromFlightHMap(String key, int new_class, String new_id){
	       List<Flight> temp = hmapFlights.get(key);
	       if(temp!=null){
	    	   if(!temp.isEmpty()){
	    		   for(int i=0; i<temp.size(); i++){
	    			   synchronized(temp.get(i)){
	    				   return temp.get(i).deleteRecord(new_class, new_id);
	    			   }
	    		   }  
	    	   }
	       }
		return false;
	}
	
	/**
	 * This method finds the passenger record in passenger hashmap using record id
	 * @param new_recordID passenger record id
	 * @return passenger object
	 */
	public Passenger findPassengerInPassengerHMap(String new_recordID){
		Set set = hmap.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()) {
	       Map.Entry mentry = (Map.Entry)iterator.next();
	       List<Passenger> temp = (List<Passenger>)mentry.getValue();
	       if(!temp.isEmpty()){
	    	   for(int i=0; i<temp.size(); i++){
	    		   String tempStr = temp.get(i).getRecordID();
	    		   //System.out.println(new_recordID+" == "+temp.get(i).getRecordID());
	    		   if(tempStr.equalsIgnoreCase(new_recordID)){
	    			  // return temp.get(i).getLastName()+";"+i;
	    			   return temp.get(i);
	    		   }
	    	   }  
	       }
	    }
		return null;
		//return "-1";
	}
	
	/**
	 * This method deletes the passenger record from passenger hashmap
	 * @param new_id passenger record id
	 * @param new_ln passenger last name
	 * @return true if deletion is succeeded
	 */
	public boolean deletePassenger(String new_id, String new_ln){
		List<Passenger> temp = hmap.get(new_ln.charAt(0));
		if(!temp.isEmpty()){
			for(int i=0; i<temp.size(); i++){
				String tempStr = temp.get(i).getRecordID();
	    		if(tempStr.equalsIgnoreCase(new_id)){
	    			synchronized(temp){
	    				temp.remove(i);
	    			}
	    			return true;
	    		}
			}			
		}
		return false;
	}

}
