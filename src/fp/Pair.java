package fp;

/**
 * Created by jooyung.han on 4/6/15.
 */
public class Pair<T, S> {
    public final T _1;
    public final S _2;
    public Pair(T _1, S _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public static <T, S> Pair<T, S> pair(T k, S v) {
        return new Pair<>(k, v);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (_1 != null ? !_1.equals(pair._1) : pair._1 != null) return false;
        return !(_2 != null ? !_2.equals(pair._2) : pair._2 != null);

    }

    @Override
    public int hashCode() {
        int result = _1 != null ? _1.hashCode() : 0;
        result = 31 * result + (_2 != null ? _2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ")";
    }
}
