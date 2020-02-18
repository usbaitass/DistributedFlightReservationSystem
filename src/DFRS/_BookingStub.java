package DFRS;

/**
 * Interface definition: Booking.
 * 
 * @author OpenORB Compiler
 */
public class _BookingStub extends org.omg.CORBA.portable.ObjectImpl
        implements Booking
{
    static final String[] _ids_list =
    {
        "IDL:DFRS/Booking:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = DFRS.BookingOperations.class;

    /**
     * Operation bookFlight
     */
    public String bookFlight(String firstName, String lastName, String address, String phone, String destination, String date, String classFlight)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("bookFlight",true);
                    _output.write_string(firstName);
                    _output.write_string(lastName);
                    _output.write_string(address);
                    _output.write_string(phone);
                    _output.write_string(destination);
                    _output.write_string(date);
                    _output.write_string(classFlight);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("bookFlight",_opsClass);
                if (_so == null)
                   continue;
                DFRS.BookingOperations _self = (DFRS.BookingOperations) _so.servant;
                try
                {
                    return _self.bookFlight( firstName,  lastName,  address,  phone,  destination,  date,  classFlight);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getBookedFlightCount
     */
    public String getBookedFlightCount(String recordType)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getBookedFlightCount",true);
                    _output.write_string(recordType);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getBookedFlightCount",_opsClass);
                if (_so == null)
                   continue;
                DFRS.BookingOperations _self = (DFRS.BookingOperations) _so.servant;
                try
                {
                    return _self.getBookedFlightCount( recordType);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation editFlightRecord
     */
    public String editFlightRecord(String recordID, String fieldName, String newValue)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("editFlightRecord",true);
                    _output.write_string(recordID);
                    _output.write_string(fieldName);
                    _output.write_string(newValue);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("editFlightRecord",_opsClass);
                if (_so == null)
                   continue;
                DFRS.BookingOperations _self = (DFRS.BookingOperations) _so.servant;
                try
                {
                    return _self.editFlightRecord( recordID,  fieldName,  newValue);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation transferReservation
     */
    public String transferReservation(String passengerID, String currentCity, String otherCity)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("transferReservation",true);
                    _output.write_string(passengerID);
                    _output.write_string(currentCity);
                    _output.write_string(otherCity);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("transferReservation",_opsClass);
                if (_so == null)
                   continue;
                DFRS.BookingOperations _self = (DFRS.BookingOperations) _so.servant;
                try
                {
                    return _self.transferReservation( passengerID,  currentCity,  otherCity);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
