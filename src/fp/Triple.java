package fp;

/**
 * Created by jooyung.han on 4/6/15.
 */
public class Triple<T, S, R> {
    public final T _1;
    public final S _2;
    public final R _3;

    public Triple(T t, S s, R r) {
        this._1 = t;
        this._2 = s;
        this._3 = r;
    }

    public static <T,S,R> Triple<T,S,R> triple(T t, S s, R r) {
        return new Triple<>(t,s,r);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;

        if (_1 != null ? !_1.equals(triple._1) : triple._1 != null) return false;
        if (_2 != null ? !_2.equals(triple._2) : triple._2 != null) return false;
        return !(_3 != null ? !_3.equals(triple._3) : triple._3 != null);

    }

    @Override
    public int hashCode() {
        int result = _1 != null ? _1.hashCode() : 0;
        result = 31 * result + (_2 != null ? _2.hashCode() : 0);
        result = 31 * result + (_3 != null ? _3.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Triple{" +
                "_1=" + _1 +
                ", _2=" + _2 +
                ", _3=" + _3 +
                '}';
    }
}
