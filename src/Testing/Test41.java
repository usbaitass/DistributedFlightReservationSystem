package Testing;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

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


//@RunWith(ConcurrentJunitRunner.class)
//@Concurrency(value = 3)
public class Test41 {
	
	String DATE = "07";
	
	@BeforeClass 
	public static void beforeClass() {
		System.out.println("Before Class Test41");		
	}
	@AfterClass 
	public static void  afterClass() {
		System.out.println("After Class Test41");
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
		
		Passenger p = new Passenger("Ulan", "Baitassov", "Verdun", "514", "Montreal", "Washington", "business", DATE+"November2016", "123");
		
		String[] argsx = {"-ORBInitialPort","1050", "-ORBInitialHost", "localhost"};
		ORB orb = ORB.init(argsx, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("Montreal"));
		
		String r0 = aBooking.bookFlight(p.getFirstName(), p.getLastName(), p.getAddress(), p.getPhone(), p.getDestination(), p.getDateFlight(), p.getClassFlight());
		assertTrue(!r0.equalsIgnoreCase("nospace") && !r0.equalsIgnoreCase("noflight"));
		
		PrintWriter file;
		try {
			file = new PrintWriter("id1.txt");
			file.println(r0);
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test2() throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		System.out.println("test2()");

		Passenger p = new Passenger("Sajjad", "Ashraf", "Verdun", "514", "NewDehli", "Washington", "business", DATE+"November2016", "123");
		
		String[] argsx = {"-ORBInitialPort","1050", "-ORBInitialHost", "localhost"};
		ORB orb = ORB.init(argsx, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		Booking aBooking = (Booking)BookingHelper.narrow(ncRef.resolve_str("NewDehli"));
		
		String r0 = aBooking.bookFlight(p.getFirstName(), p.getLastName(), p.getAddress(), p.getPhone(), p.getDestination(), p.getDateFlight(), p.getClassFlight());
		assertTrue(!r0.equalsIgnoreCase("nospace") && !r0.equalsIgnoreCase("noflight"));
		
		PrintWriter file;
		try {
			file = new PrintWriter("id2.txt");
			file.println(r0);
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
