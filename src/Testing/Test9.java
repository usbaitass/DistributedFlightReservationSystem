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

public class Test9 {
	
	static Passenger[] p1 = null;
	static final int NPASS = 10;
	static Passenger[] p2 = null;
	
	@BeforeClass 
	public static void beforeClass() {
		System.out.println("Before Class Test9");		
	}
	@AfterClass 
	public static void  afterClass() {
		System.out.println("After Class Test9");
	}
	
	@Before 
	public void before() {
		System.out.print("inside ");
		p1 = new Passenger[NPASS];
		p1[0] = new Passenger(0 + "_Ulan", "Baitassov", "Verdun", "514", "Washington", "NewDehli", "first", "10October2016", "123");
		p1[1] = new Passenger(1 + "_Ulan", "Saitassov", "Verdun", "514", "Washington", "NewDehli", "first", "10October2016", "123");
		p1[2] = new Passenger(2 + "_Ulan", "Baitassov", "Verdun", "514", "Washington", "NewDehli", "business", "10October2016", "123");
		p1[3] = new Passenger(3 + "_Ulan", "Vaitassov", "Verdun", "514", "Washington", "NewDehli", "business", "10October2016", "123");
		p1[4] = new Passenger(4 + "_Ulan", "Aaitassov", "Verdun", "514", "Washington", "NewDehli", "business", "10October2016", "123");
		p1[5] = new Passenger(5 + "_Ulan", "Aaitassov", "Verdun", "514", "Washington", "NewDehli", "economy", "10October2016", "123");
		p1[6] = new Passenger(6 + "_Ulan", "Caitassov", "Verdun", "514", "Washington", "NewDehli", "economy", "10October2016", "123");
		p1[7] = new Passenger(7 + "_Ulan", "Baitassov", "Verdun", "514", "Washington", "NewDehli", "economy", "10October2016", "123");
		p1[8] = new Passenger(8 + "_Ulan", "Saitassov", "Verdun", "514", "Washington", "NewDehli", "economy", "10October2016", "123");
		p1[9] = new Passenger(9 + "_Ulan", "Vaitassov", "Verdun", "514", "Washington", "NewDehli", "economy", "10October2016", "123");
		p2 = new Passenger[NPASS];
		p2[0] = new Passenger(0 + "_Ulan", "Baitassov", "Verdun", "514", "NewDehli", "Montreal", "first", "10October2016", "123");
		p2[1] = new Passenger(1 + "_Ulan", "Saitassov", "Verdun", "514", "NewDehli", "Montreal", "first", "10October2016", "123");
		p2[2] = new Passenger(2 + "_Ulan", "Baitassov", "Verdun", "514", "NewDehli", "Montreal", "business", "10October2016", "123");
		p2[3] = new Passenger(3 + "_Ulan", "Vaitassov", "Verdun", "514", "NewDehli", "Montreal", "business", "10October2016", "123");
		p2[4] = new Passenger(4 + "_Ulan", "Aaitassov", "Verdun", "514", "NewDehli", "Montreal", "business", "10October2016", "123");
		p2[5] = new Passenger(5 + "_Ulan", "Aaitassov", "Verdun", "514", "NewDehli", "Montreal", "economy", "10October2016", "123");
		p2[6] = new Passenger(6 + "_Ulan", "Caitassov", "Verdun", "514", "NewDehli", "Montreal", "economy", "10October2016", "123");
		p2[7] = new Passenger(7 + "_Ulan", "Baitassov", "Verdun", "514", "NewDehli", "Montreal", "economy", "10October2016", "123");
		p2[8] = new Passenger(8 + "_Ulan", "Saitassov", "Verdun", "514", "NewDehli", "Montreal", "economy", "10October2016", "123");
		p2[9] = new Passenger(9 + "_Ulan", "Vaitassov", "Verdun", "514", "NewDehli", "Montreal", "economy", "10October2016", "123");
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
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("Washington"));
		Booking aBooking2 = (Booking)BookingHelper.narrow(ncRef.resolve_str("NewDehli"));
		
		int res1 = Integer.parseInt(aBooking.editFlightRecord("WST1111", "0;NewDehli;10October2016;22:00", ""));
		assertEquals(1, res1);
		int res2 = Integer.parseInt(aBooking2.editFlightRecord("NDL1111", "0;Montreal;10October2016;13:45", ""));
		assertEquals(1, res2);
			
		String r0 = aBooking.bookFlight(p1[2].getFirstName(), p1[2].getLastName(), p1[2].getAddress(), p1[2].getPhone(), p1[2].getDestination(), p1[2].getDateFlight(), p1[2].getClassFlight());
		String r1 = aBooking.bookFlight(p1[3].getFirstName(), p1[3].getLastName(), p1[3].getAddress(), p1[3].getPhone(), p1[3].getDestination(), p1[3].getDateFlight(), p1[3].getClassFlight());
		String r2 = aBooking.bookFlight(p1[8].getFirstName(), p1[8].getLastName(), p1[8].getAddress(), p1[8].getPhone(), p1[8].getDestination(), p1[8].getDateFlight(), p1[8].getClassFlight());
		String r3 = aBooking.bookFlight(p1[9].getFirstName(), p1[9].getLastName(), p1[9].getAddress(), p1[9].getPhone(), p1[9].getDestination(), p1[9].getDateFlight(), p1[9].getClassFlight());
			
		String r5 = aBooking2.bookFlight(p2[0].getFirstName(), p2[0].getLastName(), p2[0].getAddress(), p2[0].getPhone(), p2[0].getDestination(), p2[0].getDateFlight(), p2[0].getClassFlight());
		String r6 = aBooking2.bookFlight(p2[6].getFirstName(), p2[6].getLastName(), p2[6].getAddress(), p2[6].getPhone(), p2[6].getDestination(), p2[6].getDateFlight(), p2[6].getClassFlight());
		String r7 = aBooking2.bookFlight(p2[7].getFirstName(), p2[7].getLastName(), p2[7].getAddress(), p2[7].getPhone(), p2[7].getDestination(), p2[7].getDateFlight(), p2[7].getClassFlight());
		String r8 = aBooking2.bookFlight(p2[8].getFirstName(), p2[8].getLastName(), p2[8].getAddress(), p2[8].getPhone(), p2[8].getDestination(), p2[8].getDateFlight(), p2[8].getClassFlight());
			
		assertTrue(r0!="nospace" && r0!="noflight");
		assertTrue(r1!="nospace" && r1!="noflight");
		assertTrue(r2!="nospace" && r2!="noflight");
		assertTrue(r3!="nospace" && r3!="noflight");
		assertTrue(r5!="nospace" && r5!="noflight");
		assertTrue(r6!="nospace" && r6!="noflight");
		assertTrue(r7!="nospace" && r7!="noflight");
		assertTrue(r8!="nospace" && r8!="noflight");	
	}
	
}
