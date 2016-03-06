package com.github.samtebbs33.func;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * Created by samtebbs on 01/02/2016.
 */
public class RMatch<E, R> {

    private RMatcher<E, R>[] matchers;

    public RMatch(BiPredicate<E, E> predicate, RMatcher<E, R>... matchers) {
        this.matchers = matchers;
        for (RMatcher<E, R> matcher : matchers) matcher.setPredicate(predicate);
    }

    public Optional<R> match(E obj) {
        for (RMatcher<E, R> matcher : matchers)
            if (matcher.matches(obj)) {
                return Optional.of(matcher.run());
            }
        return Optional.empty();
    }

    public R match(E obj, Supplier<R> def) {
        Optional<R> val = match(obj);
        return val.isPresent() ? val.get() : def.get();
    }

    public R match(E obj, R def) {
        Optional<R> val = match(obj);
        return val.isPresent() ? val.get() : def;
    }

}
