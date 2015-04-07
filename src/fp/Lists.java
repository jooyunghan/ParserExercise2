package fp;

import fp.functions.Function;

import java.util.*;

/**
 * Created by jooyung.han on 4/6/15.
 */
public class Lists {
    public static <T, R> List<R> map(Function<? super T, ? extends R> f, List<T> lists) {
        List<R> result = new ArrayList<>();
        for (T t : lists) {
            result.add(f.apply(t));
        }
        return result;
    }

    public static <T> List<T> cons(T t, List<T> ts) {
        List<T> result = new ArrayList<>();
        result.add(t);
        result.addAll(ts);
        return result;
    }

    public static <T> String toString(Collection<T> ts) {
        StringBuilder sb = new StringBuilder();
        ts.forEach(sb::append);
        return sb.toString();
    }

    public static <T> String toString(Collection<T> ts, String sep) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (T t : ts) {
            if (first) {
                first = false;
            } else {
                sb.append(sep);
            }
            sb.append(t);
        }
        return sb.toString();
    }

    public static <K, V> Map<K, V> toMap(Collection<Pair<K, V>> pairs) {
        Map<K, V> result = new HashMap<>();
        pairs.forEach(pair -> result.put(pair._1, pair._2));
        return result;
    }
}
