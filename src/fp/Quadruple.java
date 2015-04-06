package fp;

/**
 * User: jooyunghan
 * Date: 4/7/15 4:31 AM
 */
public class Quadruple<A, B, C, D> {
    public final A _1;
    public final B _2;
    public final C _3;
    public final D _4;

    public Quadruple(A a, B b, C c, D d) {
        this._1 = a;
        this._2 = b;
        this._3 = c;
        this._4 = d;
    }

    public static <A, B, C, D> Quadruple<A, B, C, D> quadruple(A a, B b, C c, D d) {
        return new Quadruple<>(a, b, c, d);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quadruple<?, ?, ?, ?> quadruple = (Quadruple<?, ?, ?, ?>) o;

        if (_1 != null ? !_1.equals(quadruple._1) : quadruple._1 != null) return false;
        if (_2 != null ? !_2.equals(quadruple._2) : quadruple._2 != null) return false;
        if (_3 != null ? !_3.equals(quadruple._3) : quadruple._3 != null) return false;
        return !(_4 != null ? !_4.equals(quadruple._4) : quadruple._4 != null);

    }

    @Override
    public int hashCode() {
        int result = _1 != null ? _1.hashCode() : 0;
        result = 31 * result + (_2 != null ? _2.hashCode() : 0);
        result = 31 * result + (_3 != null ? _3.hashCode() : 0);
        result = 31 * result + (_4 != null ? _4.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Quadruple{" +
                "_1=" + _1 +
                ", _2=" + _2 +
                ", _3=" + _3 +
                ", _4=" + _4 +
                '}';
    }
}
