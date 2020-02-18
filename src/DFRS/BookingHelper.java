package DFRS;

/** 
 * Helper class for : Booking
 *  
 * @author OpenORB Compiler
 */ 
public class BookingHelper
{
    /**
     * Insert Booking into an any
     * @param a an any
     * @param t Booking value
     */
    public static void insert(org.omg.CORBA.Any a, DFRS.Booking t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract Booking from an any
     *
     * @param a an any
     * @return the extracted Booking value
     */
    public static DFRS.Booking extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return DFRS.BookingHelper.narrow( a.extract_Object() );
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the Booking TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "Booking" );
        }
        return _tc;
    }

    /**
     * Return the Booking IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:DFRS/Booking:1.0";

    /**
     * Read Booking from a marshalled stream
     * @param istream the input stream
     * @return the readed Booking value
     */
    public static DFRS.Booking read(org.omg.CORBA.portable.InputStream istream)
    {
        return(DFRS.Booking)istream.read_Object(DFRS._BookingStub.class);
    }

    /**
     * Write Booking into a marshalled stream
     * @param ostream the output stream
     * @param value Booking value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, DFRS.Booking value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to Booking
     * @param obj the CORBA Object
     * @return Booking Object
     */
    public static Booking narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof Booking)
            return (Booking)obj;

        if (obj._is_a(id()))
        {
            _BookingStub stub = new _BookingStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to Booking
     * @param obj the CORBA Object
     * @return Booking Object
     */
    public static Booking unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof Booking)
            return (Booking)obj;

        _BookingStub stub = new _BookingStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
