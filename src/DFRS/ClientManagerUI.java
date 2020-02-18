package DFRS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;


/**
 * 
 * @author ulanbaitassov
 * Client - Manager User Interface program
 * 
 */
public class ClientManagerUI {
	private static Logger logger;
	private static String[] argsx;
	
	/**
	 * Main method
	 * @param args program argument parameters
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 * @throws InvalidName 
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName{
		argsx = args;
		Scanner scanner = new Scanner(System.in);
		int n = 1;
		while (n != 0) {
			//Main menu
			printMainMenu();
			boolean valid = false;
			//ask to reenter input if invalid input was entered 
			while (!valid) {
				try {
					n = scanner.nextInt();
					valid = true;
				} catch (Exception e) {
					System.out.println("Invalid Input, please enter an Integer.");
					valid = false;
					scanner.nextLine();
				}
			}
			//run next menu according to selection in previous menu
			switch (n) {
			case 1:
				System.out.println(".............................................................");
				System.out.println("Welcome to client menu!");
				runClientMenu(scanner);
				break;
			case 2:
				System.out.println(".............................................................");
				System.out.println("Welcome to manager menu!");
				runManagerMenu(scanner);
				break;
			case 0:
				System.out.println("Thank you for using our services.");
				System.exit(0);
				break;
			default:
				System.out.println("You have entered choice that is not present, please repeat your selection.");
				break;
			}
		}
		scanner.close();
	}
	
	/**
	 * Get method which reads server information from common repository and returns it to caller 
	 * @param line the number of word to look for
	 * @param new_sn comparator string
	 * @return the whole line on which it found the word
	 */
	public static String getServerInfoFromCommonRep(int line, String new_sn){
		File file = new File("common.repository");
		String str = "none";
		if(file.exists()){
			try {
				Scanner input = new Scanner(file);
				while(input.hasNextLine()){
					String temp = input.nextLine();
					String[] temp2 = temp.split(";");
					String sn = temp2[line];
					if(new_sn.equalsIgnoreCase(sn)){
						str = temp;
						break;
					}
				}
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * Client menu
	 * @param new_scanner input console
	 * @throws InvalidName 
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 * @throws RemoteException 
	 */
	public static void runClientMenu(Scanner new_scanner) throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName{
		try {
			System.out.println("Select the depatrure city:");
			System.out.println("1. Montreal");
			System.out.println("2. Washington");
			System.out.println("3. New Dehli");
			String departure = "";	
			int choice = new_scanner.nextInt();
			if(choice==1){
				departure = "Montreal";				
			}else if(choice==2){
				departure = "Washington";
			}else if(choice==3){
				departure = "NewDehli";
			}
			//read user information from console
			System.out.println("Enter first name:");
			String fname = new_scanner.next();
			System.out.println("Enter last name:");
			String lname = new_scanner.next();
			System.out.println("Enter address:");
			String addr = new_scanner.next();
			System.out.println("Enter telephone number:");
			String tnumber = new_scanner.next();
			System.out.println("Enter destination:");
			String dest = new_scanner.next();
			System.out.println("Select flight class:");
			System.out.println("1. First class");
			System.out.println("2. Business class");
			System.out.println("3. Economy class");
			int fclassN = new_scanner.nextInt();
			String fclass = "";
			if(fclassN==1){
				fclass = "first";
			}else if(fclassN==2){
				fclass = "business";
			}else if(fclassN==3){
				fclass = "economy";
			}
			System.out.println("Enter date: (13October2016)");
			String d = new_scanner.next();
			d = d.trim();
			Passenger p = new Passenger(fname, lname, addr, tnumber, departure, dest, fclass, d, "xyz");
			//open and configure logger
			logger =  Logger.getLogger(p.getFirstName()+p.getLastName());
			logger.setLevel(Level.FINE);
			logger.setUseParentHandlers(false);
			FileHandler fileHandler = new FileHandler("log"+p.getFirstName()+p.getLastName()+".txt",true);
			fileHandler.setFormatter(new LogFormatter());
			logger.addHandler(fileHandler);
			//ORB config
			ORB orb = ORB.init(argsx, null);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str(departure));
			//invoke book flight function on remote server and save returned result in string
			String answerStr = aBooking.bookFlight(p.getFirstName(), p.getLastName(), p.getAddress(), p.getPhone(), p.getDestination(), p.getDateFlight(), p.getClassFlight());
			if(answerStr.equalsIgnoreCase("nospace")){
				logger.info("BOOKING FAILED Reason: NO SPACE AVAILABLE to "+p.getDestination()+" on "+p.getDateFlight()+" date on flightclass: "+p.getClassFlight());
				System.out.println("There are flights on this day,");
				System.out.println("However, no space available for "+p.getClassFlight()+" class.");
			}else if(answerStr.equalsIgnoreCase("noflight")){
				logger.info("BOOKING FAILED Reason: NO FLIGHTS to "+p.getDestination()+" on "+p.getDateFlight()+" date.");
				System.out.println("No flights on this day.");
			}else{
				String time = answerStr.substring(0, 5);
				String r_id = answerStr.substring(5);
				System.out.println(".............................................................");
				System.out.println("Booked successfully!");
				System.out.println("Your record id: "+r_id);
				System.out.println("Name: "+p.getFirstName()+" "+p.getLastName());
				System.out.println("Departure-Destination: "+departure+" - "+p.getDestination());
				System.out.println("Date: "+p.getDateFlight()+" Time: "+time);
				System.out.println("Class: "+p.getClassFlight());
				logger.info("BOOKING SUCCESSFULL: Your record id: "+r_id+" Name: "+p.getFirstName()+" "+p.getLastName()+" Departure-Destination: "+departure+" - "+p.getDestination()+" Date: "+p.getDateFlight()+" Time: "+time+" flightclass: "+p.getClassFlight());
			}
			fileHandler.close();
			logger.removeHandler(fileHandler);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Manager menu
	 * @param scanner input console
	 * @throws InvalidName 
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 */
	public static void runManagerMenu(Scanner scanner) throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		try {
			System.out.println("1. Sign in with existing manager id");
			System.out.println("2. Sign up as new manager");
			int choice = scanner.nextInt();
			switch (choice) {
			case 1:
				System.out.println("Enter manager id:");
				String managerID = scanner.next();
				if (checkManagerID(managerID)) {
					//open log file for current manager
					logger =  Logger.getLogger(managerID);
					logger.setLevel(Level.FINE);
					logger.setUseParentHandlers(false);
					FileHandler fileHandler = new FileHandler("log"+managerID+".txt",true);
					fileHandler.setFormatter(new LogFormatter());
					logger.addHandler(fileHandler);				
					String hostPrefix = managerID.substring(0, 3);
					String[] strarr = getServerInfoFromCommonRep(2,hostPrefix).split(";");
					String hostname = strarr[0];
					//ORB CONFIG
					ORB orb = ORB.init(argsx, null);
					org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
					NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
					Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str(hostname));
					logger.info("LOGGED INTO server: "+hostname);
					//manager operation menu
					int choice2 = 1;
					while (choice2 != 0) {
						System.out.println(".............................................................");
						System.out.println("Manage flights:");
						System.out.println("1. Get flight count");
						System.out.println("2. Create new flight");
						System.out.println("3. Edit the flight");
						System.out.println("4. Delete the flight");
						System.out.println("5. View all the flights");
						System.out.println("6. Transfer passenger's flight");
						System.out.println("0. back");
						choice2 = scanner.nextInt();
						String dest="";
						String date="";
						String time="";
						int result = 0;
						switch (choice2) {
						case 1:
							//Get booked flight count from all servers
							System.out.println("Select class:");
							System.out.println("1. First");
							System.out.println("2. Business");
							System.out.println("3. Economy");
							System.out.println("4. All");
							int choice3 = scanner.nextInt();
							String recordType = "";
							if(choice3==1){
								recordType = managerID+";first";
							}else if(choice3==2){
								recordType = managerID+";business";
							}else if(choice3==3){
								recordType = managerID+";economy";
							}else if(choice3==4){
								recordType = managerID+";all";
							}
							System.out.println("flight count: "+aBooking.getBookedFlightCount(recordType).replaceAll(";", " "));
							logger.info("FLIGHT COUNT: "+aBooking.getBookedFlightCount(recordType).replaceAll(";", " "));
							break;
						case 2:
							//create new flight
							System.out.println("Enter destination: (Washington)");
							dest = scanner.next();
							System.out.println("Enter date: (13October2016)");
							date = scanner.next();
							System.out.println("Enter time: (20:30)");
							time = scanner.next();
							result = Integer.parseInt(aBooking.editFlightRecord(managerID, "0;"+dest+";"+date+";"+time, ""));
							if(result==1){
								logger.info("CREATED flight from "+hostname+" to "+dest+" on "+date+" "+time+".");
								System.out.println("The flight was successfully created.");
							}else if(result==0){
								logger.info("FAILED TO CREATE flight from "+hostname+" to "+dest+" on "+date+" "+time+".");
								System.out.println("Failed to created the flight from "+hostname+" to "+dest+" on "+date+" "+time+".");
							}
							break;
						case 3:
							//edit the flight
							System.out.println("Enter destination: (Washington)");
							dest = scanner.next();
							System.out.println("Enter date: (13October2016)");
							date = scanner.next();
							System.out.println("Enter time: (20:30)");
							time = scanner.next();
							System.out.println("Enter new date: (13October2016)");
							String date2 = scanner.next();
							System.out.println("Enter new time: (20:30)");
							String time2 = scanner.next();
							System.out.println("Enter new first class capacity:");
							String first2 = scanner.next();
							System.out.println("Enter new business class capacity:");
							String business2 = scanner.next();
							System.out.println("Enter new economy class capacity:");
							String economy2 = scanner.next();
							result= Integer.parseInt(aBooking.editFlightRecord(managerID, "1;"+dest+";"+date+";"+time, date2+";"+time2+";"+first2+";"+business2+";"+economy2));
							if(result==1){
								logger.info("EDITED flight from "+hostname+" to "+dest+" on "+date+" "+time+" TO "+date2+" "+time2+" "+first2+" "+business2+" "+economy2+".");
								System.out.println("The flight was successfully edited.");
							}else if(result==0){
								logger.info("FAILED TO EDIT flight from "+hostname+" to "+dest+" on "+date+" "+time+" REASON: no such flight.");
								System.out.println("No such flight");
							}
							break;
						case 4:
							//delete the flight
							System.out.println("Enter destination: (Washington)");
							dest = scanner.next();
							System.out.println("Enter date: (13October2016)");
							date = scanner.next();
							System.out.println("Enter time: (20:30)");
							time = scanner.next();
							result = Integer.parseInt(aBooking.editFlightRecord(managerID, "2;"+dest+";"+date+";"+time, ""));
							if(result==1){
								logger.info("DELETED flight from "+hostname+" to "+dest+" on "+date+" "+time+".");
								System.out.println("The flight was successfully deleted.");
							}else if(result==0){
								logger.info("FAILED TO DELETE flight from "+hostname+" to "+dest+" on "+date+" "+time+" REASON: no such flight.");
								System.out.println("No such flight.");
							}
							break;
						case 5:
							//show all available flights
							String[] allflights = aBooking.editFlightRecord(managerID, "3", "").split(";");
							for(int i=0; i<allflights.length; i++){
								System.out.println(allflights[i]);
							}
							break;
						case 6:
							System.out.println("Enter passenger record: ");
							String rID = scanner.next().toString();
							System.out.println("Enter passenger current city: ");
							String currentCity = scanner.next();
							System.out.println("Enter passenger other city: ");
							String otherCity = scanner.next();
							if(currentCity.equalsIgnoreCase(hostname)){
								String resultTransfer = aBooking.transferReservation(rID, currentCity, otherCity);
								System.out.println(resultTransfer);
							}else{
								System.out.println("Manager doesn't have access to "+currentCity +" server.");
							//	Booking aBooking2 = (Booking)BookingHelper.narrow(ncRef.resolve_str(currentCity));
							//	String resultTransfer = aBooking2.transferReservation(rID, currentCity, otherCity);
							//	System.out.println(resultTransfer);
							}
							break;
						default:
							choice2 = 0;
							break;
						}
					}
					logger.info("SIGN OUT from server: "+hostname);
					fileHandler.close();
					logger.removeHandler(fileHandler);
				} else {
					System.out.println("No such manager id on server database.");
				}
				break;
			case 2:
				//get next new free unique manager ID
				System.out.println("Select the server:");
				System.out.println("1. Montreal");
				System.out.println("2. Washington");
				System.out.println("3. New-Dehli");
				int choice2 = scanner.nextInt();
				String sname = "";
				if(choice2==1){
					sname = "Montreal";
				}else if(choice2==2){
					sname = "Washington";
				}else if(choice2==3){
					sname = "NewDehli";
				}
				String[] strarr = getServerInfoFromCommonRep(0, sname).split(";");
				String mname = createManager(strarr[2]);
				System.out.println("Your manager id: " + mname);
				//create log for new manager ID
				logger =  Logger.getLogger(mname);
				logger.setLevel(Level.FINE);
				logger.setUseParentHandlers(false);
				FileHandler fileHandler = new FileHandler("log"+mname+".txt",true);
				fileHandler.setFormatter(new LogFormatter());
				logger.addHandler(fileHandler);
				logger.info("Manager id: "+mname+" was created.");
				fileHandler.close();
				logger.removeHandler(fileHandler);
				break;
			default:
				break;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method checks whether manager ID exists in server's manager database
	 * @param idStr manager unique ID number
	 * @return true if exists, false if doesn't
	 */
	public static boolean checkManagerID(String idStr) {
		try {
			File file = new File(idStr.substring(0, 3) + "managers.txt");
			if (file.exists()) {
				Scanner input = new Scanner(file);
				int id = Integer.parseInt(idStr.substring(3, 7));
				int id2 = 0;
				while (input.hasNextLine()) {
					id2 = Integer.parseInt(input.nextLine());
					if (id == id2) {
						input.close();
						return true;
					}
				}
				input.close();
				return false;
			} else {
				return false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This method creates new unique manager id according to on which server logged in
	 * @param new_abrname server number chosen by manager
	 * @return new manager ID
	 */
	public static String createManager(String new_abrname) {
		String prefixID = new_abrname.trim();
		File file = new File(prefixID + "managers.txt");
		int id = 0;
		try {
			if (file.exists()) {
				Scanner input = new Scanner(file);
				while (input.hasNextLine()) {
					id = Integer.parseInt(input.nextLine());
				}
				id++;
				input.close();
			} else {
				file.createNewFile();
				id = 1111;
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.println(Integer.toString(id));
			pw.close();
			bw.close();
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prefixID + id;
	}

	/**
	 * Initial Client-Manager user interface menu 
	 */
	public static void printMainMenu() {
		System.out.println(".............................................................");
		System.out.println("Welcome to Canada Airlines!");
		System.out.println("Enter to system as:");
		System.out.println("1. Client");
		System.out.println("2. Manager");
		System.out.println("0. Quit");
	}

}

