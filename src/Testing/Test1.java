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

public class Test1 {
	
	static Passenger[] p = null;
	static final int NPASS = 10;
	
	@BeforeClass 
	public static void beforeClass() {
		System.out.println("Before Class Test1");		
	}
	@AfterClass 
	public static void  afterClass() {
		System.out.println("After Class Test1");
	}
	
	@Before 
	public void before() {
		System.out.print("inside ");
		p = new Passenger[NPASS];
		p[0] = new Passenger(0 + "_Ulan", "Baitassov", "Verdun", "514", "Montreal", "Washington", "first", "10October2016", "123");
		p[1] = new Passenger(1 + "_Ulan", "Saitassov", "Verdun", "514", "Montreal", "Washington", "first", "10October2016", "123");
		p[2] = new Passenger(2 + "_Ulan", "Baitassov", "Verdun", "514", "Montreal", "Washington", "first", "10October2016", "123");
		p[3] = new Passenger(3 + "_Ulan", "Vaitassov", "Verdun", "514", "Montreal", "Washington", "business", "10October2016", "123");
		p[4] = new Passenger(4 + "_Ulan", "Aaitassov", "Verdun", "514", "Montreal", "Washington", "business", "10October2016", "123");
		p[5] = new Passenger(5 + "_Ulan", "Aaitassov", "Verdun", "514", "Montreal", "Washington", "business", "10October2016", "123");
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
	public void testBookWithNoFlightYet() throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		System.out.println("testBookWithNoFlightYet()");
		
		String[] argsx = {"-ORBInitialPort","1050", "-ORBInitialHost", "localhost"};
		ORB orb = ORB.init(argsx, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("Montreal"));
		
		for(int i=0; i<NPASS; i++){
			String result = aBooking.bookFlight(p[i].getFirstName(), p[i].getLastName(), p[i].getAddress(), p[i].getPhone(), p[i].getDestination(), p[i].getDateFlight(), p[i].getClassFlight());
			assertEquals("noflight", result);
		}
	}
	
}
