package aero.pilotlog.utilities;

import android.text.TextUtils;

/**
 * Created by tuan.na on 8/24/2015.
 */
public class ValidationUtils {

    public static boolean isNumeric(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                str = str.replace(",", ".");
            }
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public final static char MIN_DIGIT = '0';
    public final static char MAX_DIGIT = '9';
    public final static char MIN_LETTER = 'A';
    public final static char MAX_LETTER = 'Z';

    public static String incrementedAlpha(String original) {
        StringBuilder buf = new StringBuilder(original);
        //int index = buf.length() -1;
        int i = buf.length() - 1;
        //while(index >= 0) {
        while (i >= 0) {
            char c = buf.charAt(i);
            c++;
            // revisar si es numero
            if ((c - 1) >= MIN_LETTER && (c - 1) <= MAX_LETTER) {
                if (c > MAX_LETTER) { // overflow, carry one
                    buf.setCharAt(i, MIN_LETTER);
                    i--;
                    continue;
                }

            } else {
                if (c > MAX_DIGIT) { // overflow, carry one
                    buf.setCharAt(i, MIN_DIGIT);
                    i--;
                    continue;
                }
            }
            // revisar si es numero
            buf.setCharAt(i, c);
            return buf.toString();
        }
        // overflow at the first "digit", need to add one more digit
        buf.insert(0, MIN_DIGIT);
        return buf.toString();
    }

    public static String toDisplayCase(String s) {

        final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following
        // to be capitalized

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
        }
        return sb.toString();
    }
}
