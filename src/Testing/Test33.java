package Testing;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.Test;

import DFRS.Booking;
import DFRS.BookingHelper;
import DFRS.Passenger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class Test33 {
	
	@BeforeClass 
	public static void beforeClass() {
		System.out.println("Before Class Test33");		
	}
	@AfterClass 
	public static void  afterClass() {
		System.out.println("After Class Test33");
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
	public void testBookFlight() throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		System.out.println("testBookFlights()");
		
		Passenger p = new Passenger(0 + "Ulan", "Baitassov", "Verdun", "514", "Montreal", "Washington", "business", "13October2016", "123");
		
		String[] argsx = {"-ORBInitialPort","1050", "-ORBInitialHost", "localhost"};
		ORB orb = ORB.init(argsx, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("Montreal"));
		
		String r0 = aBooking.bookFlight(p.getFirstName(), p.getLastName(), p.getAddress(), p.getPhone(), p.getDestination(), p.getDateFlight(), p.getClassFlight());
			
		assertTrue(r0!="nospace" && r0!="noflight");
		
		PrintWriter file;
		try {
			file = new PrintWriter("id.txt");
			file.println(r0);
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
