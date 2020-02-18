package Testing;

import static org.junit.Assert.*;

import java.util.Scanner;

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

public class Test34 {
	
	@BeforeClass 
	public static void beforeClass() {
		System.out.println("Before Class Test34");		
	}
	@AfterClass 
	public static void  afterClass() {
		System.out.println("After Class Test34");
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
	public void testTransferReservation() throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		System.out.println("testTransferReservation()");
		
		String[] argsx = {"-ORBInitialPort","1050", "-ORBInitialHost", "localhost"};
		ORB orb = ORB.init(argsx, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("Montreal"));
		
		String passengerID = "14451144";
		
		String result = aBooking.transferReservation(passengerID, "Montreal", "NewDehli").trim();
		//System.err.println(result);
		assertTrue(result.equalsIgnoreCase("nosuchrecord"));
		
	}
	
}
