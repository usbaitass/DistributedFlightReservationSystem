package Testing;

import static org.junit.Assert.assertEquals;

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
import DFRS.Passenger;

public class Test2 {
	
	static Passenger[] p = null;
	static final int NPASS = 10;
	
	@BeforeClass 
	public static void beforeClass() {
		System.out.println("Before Class Test2");		
	}
	@AfterClass 
	public static void  afterClass() {
		System.out.println("After Class Test2");
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
	public void testDeleteFlight() throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		System.out.println("testDeleteFlight()");
		
		String[] argsx = {"-ORBInitialPort","1050", "-ORBInitialHost", "localhost"};
		ORB orb = ORB.init(argsx, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("Montreal"));
		
		int r = Integer.parseInt(aBooking.editFlightRecord("MTL1111", "2;Washington;10October2016;20:30", ""));
		assertEquals(0, r);	
	}
}
