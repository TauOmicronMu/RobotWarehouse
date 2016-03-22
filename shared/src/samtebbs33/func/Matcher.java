package samtebbs33.func;

import java.util.function.BiPredicate;

/**
 * Created by samtebbs on 01/02/2016.
 */
public class Matcher<E> {

    private E[] objs;
    private BiPredicate<E, E> predicate;
    private Runnable consumer;

    public Matcher(Runnable consumer, E... objs) {
        this.consumer = consumer;
        this.objs = objs;
    }

    public void setPredicate(BiPredicate<E, E> predicate) {
        this.predicate = predicate;
    }

    public boolean matches(E obj) {
        for (E e : objs) if (predicate.test(e, obj)) return true;
        return false;
    }

    public void run() {
        consumer.run();
    }
}
