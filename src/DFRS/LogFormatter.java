package DFRS;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author ulanbaitassov
 * This class responsible for reformating the log messages in my desired format 
 */
public class LogFormatter extends Formatter{	
	
	@Override
	public String format(LogRecord record) {
		return new Date(record.getMillis())+"::"+record.getMessage()+"\n";
	}
	
}
