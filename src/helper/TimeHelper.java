package helper;

import java.time.*;

/**TimeHelper class provides methods to assist with time conversions.
 *
 * @author Joseph Merritt
 * */
public abstract class TimeHelper {


    static ZoneOffset offset = OffsetDateTime.now().getOffset();
    static LocalTime open;
    static LocalTime close;
    public static ZonedDateTime userTimeUTC;

    /**Sets static variables for user time and converted open and close times for business hours.*/
    public static void setUserTime(){
        System.out.println(offset.getTotalSeconds());
        open = LocalTime.of(12,0);
        close = LocalTime.of(1,45);
        open = open.plusSeconds(offset.getTotalSeconds());
        close = close.plusSeconds(offset.getTotalSeconds());
        System.out.println(open);
        userTimeUTC = ZonedDateTime.now();
    }

    /**
     * @return open Converted open time for displaying appointment times.
     */
    public static LocalTime getOpen(){return open;}

    /**
     * @return close Converted close time for displaying appointment times.
     */
    public static LocalTime getClose(){return close;}

}
