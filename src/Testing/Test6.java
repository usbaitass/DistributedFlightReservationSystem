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

public class Test6 {
	
	static Passenger[] p = null;
	static final int NPASS = 10;
	
	@BeforeClass 
	public static void beforeClass() {
		System.out.println("Before Class Test6");		
	}
	@AfterClass 
	public static void  afterClass() {
		System.out.println("After Class Test6");
	}
	
	@Before 
	public void before() {
		System.out.print("inside ");
		p = new Passenger[NPASS];
		p[0] = new Passenger(0 + "_Ulan", "Baitassov", "Verdun", "514", "Montreal", "Washington", "first", "10October2016", "123");
		p[1] = new Passenger(1 + "_Ulan", "Saitassov", "Verdun", "514", "Montreal", "Washington", "first", "10October2016", "123");
		p[2] = new Passenger(2 + "_Ulan", "Baitassov", "Verdun", "514", "Montreal", "Washington", "business", "10October2016", "123");
		p[3] = new Passenger(3 + "_Ulan", "Vaitassov", "Verdun", "514", "Montreal", "Washington", "business", "10October2016", "123");
		p[4] = new Passenger(4 + "_Ulan", "Aaitassov", "Verdun", "514", "Montreal", "Washington", "business", "10October2016", "123");
		p[5] = new Passenger(5 + "_Ulan", "Aaitassov", "Verdun", "514", "Montreal", "Washington", "economy", "10October2016", "123");
		p[6] = new Passenger(6 + "_Ulan", "Caitassov", "Verdun", "514", "Montreal", "Washington", "economy", "10October2016", "123");
		p[7] = new Passenger(7 + "_Ulan", "Baitassov", "Verdun", "514", "Montreal", "Washington", "economy", "10October2016", "123");
		p[8] = new Passenger(8 + "_Ulan", "Saitassov", "Verdun", "514", "Montreal", "Washington", "economy", "10October2016", "123");
		p[9] = new Passenger(9 + "_Ulan", "Vaitassov", "Verdun", "514", "Montreal", "Washington", "economy", "10October2016", "123");
	}
	   
	@After
	public void after() {
		System.out.println("outside test ");
	}
	
	@Test
	public void testBookFlight() throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		System.out.println("testBookFlights()");
		
		String[] argsx = {"-ORBInitialPort","1050", "-ORBInitialHost", "localhost"};
		ORB orb = ORB.init(argsx, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("Montreal"));
		
		String r0 = aBooking.bookFlight(p[0].getFirstName(), p[0].getLastName(), p[0].getAddress(), p[0].getPhone(), p[0].getDestination(), p[0].getDateFlight(), p[0].getClassFlight());
		String r1 = aBooking.bookFlight(p[1].getFirstName(), p[1].getLastName(), p[1].getAddress(), p[1].getPhone(), p[1].getDestination(), p[1].getDateFlight(), p[1].getClassFlight());
		String r2 = aBooking.bookFlight(p[2].getFirstName(), p[2].getLastName(), p[2].getAddress(), p[2].getPhone(), p[2].getDestination(), p[2].getDateFlight(), p[2].getClassFlight());
		String r3 = aBooking.bookFlight(p[3].getFirstName(), p[3].getLastName(), p[3].getAddress(), p[3].getPhone(), p[3].getDestination(), p[3].getDateFlight(), p[3].getClassFlight());
		String r4 = aBooking.bookFlight(p[4].getFirstName(), p[4].getLastName(), p[4].getAddress(), p[4].getPhone(), p[4].getDestination(), p[4].getDateFlight(), p[4].getClassFlight());
		String r5 = aBooking.bookFlight(p[5].getFirstName(), p[5].getLastName(), p[5].getAddress(), p[5].getPhone(), p[5].getDestination(), p[5].getDateFlight(), p[5].getClassFlight());
		String r6 = aBooking.bookFlight(p[6].getFirstName(), p[6].getLastName(), p[6].getAddress(), p[6].getPhone(), p[6].getDestination(), p[6].getDateFlight(), p[6].getClassFlight());
		String r7 = aBooking.bookFlight(p[7].getFirstName(), p[7].getLastName(), p[7].getAddress(), p[7].getPhone(), p[7].getDestination(), p[7].getDateFlight(), p[7].getClassFlight());
		String r8 = aBooking.bookFlight(p[8].getFirstName(), p[8].getLastName(), p[8].getAddress(), p[8].getPhone(), p[8].getDestination(), p[8].getDateFlight(), p[8].getClassFlight());
		String r9 = aBooking.bookFlight(p[9].getFirstName(), p[9].getLastName(), p[9].getAddress(), p[9].getPhone(), p[9].getDestination(), p[9].getDateFlight(), p[9].getClassFlight());
			
		assertTrue(r0!="nospace" && r1!="noflight");
		assertEquals("nospace", r1);
		assertTrue(r2!="nospace" && r2!="noflight");
		assertTrue(r3!="nospace" && r3!="noflight");
		assertEquals("nospace", r4);
		assertTrue(r5!="nospace" && r5!="noflight");
		assertTrue(r6!="nospace" && r6!="noflight");
		assertTrue(r7!="nospace" && r7!="noflight");
		assertEquals("nospace", r8);
		assertEquals("nospace", r9);
	}
	
}
