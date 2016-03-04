package com.github.samtebbs33.func;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * Created by samtebbs on 01/02/2016.
 */
public class RMatcher<E, R> {

    private E[] objs;
    private BiPredicate<E, E> predicate;
    private Supplier<R> supplier;
    private R val;

    public RMatcher(R val, E... objs) {
        this.objs = objs;
        this.val = val;
    }

    public RMatcher(Supplier<R> supplier, E... objs) {
        this.supplier = supplier;
        this.objs = objs;
    }

    public void setPredicate(BiPredicate<E, E> predicate) {
        this.predicate = predicate;
    }

    public boolean matches(E obj) {
        for (E e : objs) if (predicate.test(e, obj)) return true;
        return false;
    }

    public R run() {
        return supplier == null ? val : supplier.get();
    }

}
