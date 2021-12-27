package util;

import java.util.Locale;

/*
* This class obtains the system default language
* */
public class Lang {

    public static String getUserLanguage() {
        String currentLocale = Locale.getDefault().getLanguage();

        return currentLocale;
    }

}
