package fp;

import fp.functions.Function;

/**
 * User: jooyunghan
 * Date: 4/7/15 2:11 AM
 */
public class Strings {
    public static String take(int n, String s) {
        return s.length() > n ? s.substring(0, n) : s;
    }

    public static Function<String, Pair<String, String>> split(String regex) {
        return (s) -> Pair.fromArray(s.split(regex, 2));
    }
}
