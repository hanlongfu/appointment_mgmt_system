package util;

import java.util.Locale;
import java.util.TimeZone;


/*
* This class obtains the locale of the system
* */
public class Location {

    public static String getUserLocation() {

        Locale currentLocale = Locale.getDefault();
        String country = currentLocale.getDisplayCountry();
        TimeZone timeZone = TimeZone.getDefault();
        String zoneId = timeZone.getDisplayName(true, 0);
        String location;
        location = country + " (" + zoneId + ")";
        return location;
    }

}
