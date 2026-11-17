package team.frostfield.xtrafight.utils;

public class StringUtils {

    public static final char SPACE = ' ';
    public static final char UNDERSCORE = '_';
    
    public static String toLowerCase(String s) {
        return s.toLowerCase();
    }

    public static String toLowerCaseReplaceSpace(String s) {
        return s.replace(SPACE, UNDERSCORE).toLowerCase();
    }

    public static String capitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
    }

    public static String toCamelCase(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (char c : s.toCharArray()) {
            if (c == SPACE || c == UNDERSCORE) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String truncate(String s, int maxLength) {
        if (s == null || s.length() <= maxLength) {
            return s;
        }
        return s.substring(0, maxLength) + "...";
    }
}

