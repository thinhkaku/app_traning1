package aero.pilotlog.utilities;

/**
 * Created by ngoc.dh on 7/20/2015.
 */
public class StringMatcher {
    private final static char KOREAN_UNICODE_START = '?';
    private final static char KOREAN_UNICODE_END = '?';
    private final static char KOREAN_UNIT = '?' - '?';
    private final static char[] KOREAN_INITIAL = {'?', '?', '?', '?', '?', '?', '?', '?', '?', '?'
            , '?', '?', '?', '?', '?', '?', '?', '?', '?'};

    public static boolean match(String value, String keyword) {
        if (value == null || keyword == null)
            return false;
        if (keyword.length() > value.length())
            return false;

        int i = 0, j = 0;
        do {
            if (isKorean(value.charAt(i)) && isInitialSound(keyword.charAt(j))) {
                if (keyword.charAt(j) == getInitialSound(value.charAt(i))) {
                    i++;
                    j++;
                } else if (j > 0)
                    break;
                else
                    i++;
            } else {
                if (keyword.charAt(j) == value.charAt(i)) {
                    i++;
                    j++;
                } else if (j > 0)
                    break;
                else
                    i++;
            }
        } while (i < value.length() && j < keyword.length());

        return j == keyword.length();
    }

    private static boolean isKorean(char c) {
        if (c >= KOREAN_UNICODE_START && c <= KOREAN_UNICODE_END)
            return true;
        return false;
    }

    private static boolean isInitialSound(char c) {
        for (char i : KOREAN_INITIAL) {
            if (c == i)
                return true;
        }
        return false;
    }

    private static char getInitialSound(char c) {

        if(!isKorean(c)){
            return c;
        }

        return KOREAN_INITIAL[(c - KOREAN_UNICODE_START) / KOREAN_UNIT];
    }
}
