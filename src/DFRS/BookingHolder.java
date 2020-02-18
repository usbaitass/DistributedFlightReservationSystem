package DFRS;

/**
 * Holder class for : Booking
 * 
 * @author OpenORB Compiler
 */
final public class BookingHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal Booking value
     */
    public DFRS.Booking value;

    /**
     * Default constructor
     */
    public BookingHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public BookingHolder(DFRS.Booking initial)
    {
        value = initial;
    }

    /**
     * Read Booking from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = BookingHelper.read(istream);
    }

    /**
     * Write Booking into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        BookingHelper.write(ostream,value);
    }

    /**
     * Return the Booking TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return BookingHelper.type();
    }

}
