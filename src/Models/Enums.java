package Models;

/**
 * Contains all Enums.
 * @author SajjadAshrafCan
 *
 */
public class Enums {
	public enum Class {
		First, Business, Economy
	}

	public enum FlightCities {
		Montreal, Washington, NewDelhi
	}

	public enum UDPPort {
		Montreal(3030), Washington(3031), NewDelhi(3032), Wrapper(2020) ;
		private int numVal;

		UDPPort(int numVal) {
			this.numVal = numVal;
		}

		public int getNumVal() {
			return numVal;
		}
	}

	public enum FlightCitiesShrot {
		MTL, WST, NDL
	}

	public enum FlightFileds {
		flightDate, flightTime, destinaition, source, seatsInFirstClass, seatsInBusinessClass, seatsInEconomyClass, createFlight, deleteFlight, flightDetail, bookingDetail
	}
	
	public enum Operations {
		bookFlight, getBookedFlightCount, editFlightRecord, transferReservation, heatBeat, softwareFailure, hardwareFailure
	}
	
	public enum UDPMessageType {
		Request,Reply
	}
	
	public enum UDPSender {
		FrontEnd, Sequencer, RMUlan, RMSajjad, RMUmer, RMFeras, ReplicaUlan, ReplicaSajjad, ReplicaUmer, ReplicaFeras
	}

	public static FlightFileds getEnumFlightFiledsFromString(String filedsName) {
		try {
			return FlightFileds.valueOf(filedsName);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public static FlightCities getFlightCitiesFromString(String filedsName) {
		try {
			return FlightCities.valueOf(filedsName);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public static Class getClassFromString(String filedsName) {
		try {
			return Class.valueOf(filedsName);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public static FlightCitiesShrot getFlightCitiesShrotFromString(String filedsName) {
		try {
			return FlightCitiesShrot.valueOf(filedsName);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	public static Operations getOperationsFromString(String filedsName) {
		try {
			return Operations.valueOf(filedsName);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
}
