package com.change.unlock.common.tuple;

/**
 * @author jone.sun on 2015/3/24.
 */
public class Tuple {
    public static <A, B> Tuple2<A, B> tuple(A a, B b) {
        return new Tuple2<A, B>(a, b);
    }
}