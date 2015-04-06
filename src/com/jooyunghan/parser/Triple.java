package com.jooyunghan.parser;

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
}
