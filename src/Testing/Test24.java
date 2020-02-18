package Testing;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import DFRS.Booking;
import DFRS.BookingHelper;


//@RunWith(ConcurrentJunitRunner.class)
//@Concurrency(value = 3)
public class Test24 {
	
	
	@BeforeClass 
	public static void beforeClass() {
		System.out.println("Before Class Test24");		
	}
	@AfterClass 
	public static void  afterClass() {
		System.out.println("After Class Test24");
	}
	
	@Before 
	public void before() {
		System.out.print("inside ");
	}
	   
	@After
	public void after() {
		System.out.println("outside test ");
	}
	
	@Test
	public void test1() throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		System.out.println("test1()");
		
		String[] argsx = {"-ORBInitialPort","1050", "-ORBInitialHost", "localhost"};
		ORB orb = ORB.init(argsx, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("Montreal"));
		
		Scanner scanner;
		String passengerID = "";
		try {
			scanner = new Scanner(new File("id1.txt"));
			passengerID = scanner.nextLine();
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		passengerID = passengerID.substring(5);
		
		String result = aBooking.transferReservation(passengerID, "Montreal", "NewDehli").trim();
			
		assertTrue(!result.equalsIgnoreCase("nosuchrecord") && !result.equalsIgnoreCase("nospace") && !result.equalsIgnoreCase("noflight"));
		//assertTrue(result.equalsIgnoreCase("noflight"));		
	}
	
	@Test
	public void test2() throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		System.out.println("test2()");

		String[] argsx = {"-ORBInitialPort","1050", "-ORBInitialHost", "localhost"};
		ORB orb = ORB.init(argsx, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("Montreal"));
		
		Scanner scanner;
		String passengerID = "";
		try {
			scanner = new Scanner(new File("id1.txt"));
			passengerID = scanner.nextLine();
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		passengerID = passengerID.substring(5);
		
		String result = aBooking.transferReservation(passengerID, "Montreal", "NewDehli").trim();
			
		assertTrue(!result.equalsIgnoreCase("nosuchrecord") && !result.equalsIgnoreCase("nospace") && !result.equalsIgnoreCase("noflight"));
		//assertTrue(result.equalsIgnoreCase("noflight"));
	}
	
	@Test
	public void test3() throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		System.out.println("test3()");
		
		String[] argsx = {"-ORBInitialPort","1050", "-ORBInitialHost", "localhost"};
		ORB orb = ORB.init(argsx, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("Montreal"));
		
		Scanner scanner;
		String passengerID = "";
		try {
			scanner = new Scanner(new File("id1.txt"));
			passengerID = scanner.nextLine();
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		passengerID = passengerID.substring(5);
		
		String result = aBooking.transferReservation(passengerID, "Montreal", "NewDehli").trim();
			
		assertTrue(!result.equalsIgnoreCase("nosuchrecord") && !result.equalsIgnoreCase("nospace") && !result.equalsIgnoreCase("noflight"));
		//assertTrue(result.equalsIgnoreCase("noflight"));
	}
	
}
