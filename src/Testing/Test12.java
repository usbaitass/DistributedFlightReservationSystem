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

public class Test12 {
	
	static Passenger[] p = null;
	static final int NPASS = 10;
	
	@BeforeClass 
	public static void beforeClass() {
		System.out.println("Before Class Test12");		
	}
	@AfterClass 
	public static void  afterClass() {
		System.out.println("After Class Test12");
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
	public void testGetBookedCountFlightFlights() throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		System.out.println("testGetBookedCountFlightFlights()");
		
		String[] argsx = {"-ORBInitialPort","1050", "-ORBInitialHost", "localhost"};
		ORB orb = ORB.init(argsx, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("Montreal"));
		
		String r1 = aBooking.getBookedFlightCount("MTL1111;first").trim();
		String r2 = aBooking.getBookedFlightCount("MTL1111;business").trim();
		String r3 = aBooking.getBookedFlightCount("MTL1111;economy").trim();
		String r4 = aBooking.getBookedFlightCount("MTL1111;all").trim();
		
		r1 = r1.substring(0, 12)+r1.substring(r1.length()-6, r1.length()).trim();
		r2 = r2.substring(0, 12)+r2.substring(r2.length()-6, r2.length()).trim();
		r3 = r3.substring(0, 12)+r3.substring(r3.length()-6, r3.length()).trim();
		r4 = r4.substring(0, 12)+r4.substring(r4.length()-6, r4.length()).trim();
		assertEquals("MTL;1;WST;0;NDL;0;", r1);
		assertEquals("MTL;2;WST;2;NDL;0;", r2);
		assertEquals("MTL;3;WST;2;NDL;0;", r3);
		assertEquals("MTL;6;WST;4;NDL;0;", r4);		
	}
	
}
