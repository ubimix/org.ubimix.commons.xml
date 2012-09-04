package org.ubimix.commons.xml.atom;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class contains utility methods used to transform dates
 * (serialization/deserialization and so on).
 * 
 * @author kotelnikov
 */
public class DateUtil {

    // "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
    private static SimpleDateFormat DATE_FORMAT = newDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /**
     * One day in milliseconds.
     */
    public final static int DAY;

    /**
     * Date-time formats
     */
    private final static SimpleDateFormat[] FORMATS = newDateFormats(
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "EEE, d MMM yyyy HH:mm:ss z",
        "EEE, d MMM yyyy HH:mm:ss Z",
        "EEE, d MMM yyyy HH:mm:ss");

    /**
     * One hour in milliseconds.
     */
    public final static int HOUR;

    /**
     * One minute in milliseconds.
     */
    public final static int MIN;

    /**
     * One second == 1000 milliseconds
     */
    public final static int SEC;

    // Internal fields initialization
    static {
        SEC = 1000;
        MIN = SEC * 60;
        HOUR = MIN * 60;
        DAY = HOUR * 24;

    }

    /**
     * Returns the string representation of the specified date.
     * 
     * @param date the date to format
     * @return the string representation of the specified date.
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * Returns the string representation of the specified date.
     * 
     * @param date the date to format
     * @return the string representation of the specified date.
     */
    public static String formatDate(long date) {
        return DATE_FORMAT.format(date);
    }

    public static SimpleDateFormat newDateFormat(String template) {
        SimpleDateFormat format = new SimpleDateFormat(template);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    public static SimpleDateFormat[] newDateFormats(String... templates) {
        SimpleDateFormat[] result = new SimpleDateFormat[templates.length];
        int i = 0;
        for (String template : templates) {
            result[i++] = newDateFormat(template);
        }
        return result;
    }

    /**
     * Returns the number representation of the date corresponding to the given
     * string
     * 
     * @param str the serialized form of the datetime
     * @return the long representation of the date corresponding to the given
     *         string
     */
    public static long parseDate(String str) {
        long result = -1;
        for (SimpleDateFormat format : FORMATS) {
            try {
                Date date = format.parse(str);
                result = date.getTime();
                break;
            } catch (Exception e) {
                // Just skip it and try with the next format
            }
        }
        return result;
    }

}
