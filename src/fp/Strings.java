package fp;

/**
 * User: jooyunghan
 * Date: 4/7/15 2:11 AM
 */
public class Strings {
    public static String take(int n, String s) {
        return s.length() > n ? s.substring(0, n) : s;
    }
}
