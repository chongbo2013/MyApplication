package com.change.unlock.common.tuple;

/**
 * @author jone.sun on 2015/3/24.
 */
public class Tuple2<A, B> {
    public final A v1;
    public final B v2;

    public Tuple2(A a, B b) {
        v1 = a;
        v2 = b;
        v1.getClass().getName();
    }

    public String toString() {
        return "(" + v1 + ", " + v2 + ")";
    }
}

