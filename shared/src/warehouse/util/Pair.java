package warehouse.util;

public class Pair<T, E> {
    public T t;
    public E e;

    public Pair(T t, E e) {
        this.t = t;
        this.e = e;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "t=" + t +
                ", e=" + e +
                '}';
    }
}