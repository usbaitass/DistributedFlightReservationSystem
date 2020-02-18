package Testing;

import static org.junit.Assert.*;

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

public class Test32 {
	
	@BeforeClass 
	public static void beforeClass() {
		System.out.println("Before Class Test32");		
	}
	@AfterClass 
	public static void  afterClass() {
		System.out.println("After Class Test32");
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
	public void testCreateFlight() throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		System.out.println("testCreateFlight()");
		
		String[] argsx = {"-ORBInitialPort","1050", "-ORBInitialHost", "localhost"};
		ORB orb = ORB.init(argsx, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("NewDehli"));
		
		int r = Integer.parseInt(aBooking.editFlightRecord("NDL1111", "0;Washington;13October2016;20:30", ""));
		assertEquals(1, r);
			
	}
	
}
