package warehouse.test;

import warehouse.util.MultiSubscriber;
import warehouse.util.Subscriber;

import static warehouse.util.EventDispatcher.onEvent2;
import static warehouse.util.EventDispatcher.subscribe2;
/**
 * Created by samtebbs on 06/03/2016.
 */
public class EventDispatcherTest {

    public static void main(String[] args) {
        subscribe2(new EventDispatcherTest());
        onEvent2(new String("1st"));
        onEvent2(new Integer(3));
        onEvent2(new Boolean(true));
    }

    @Subscriber
    public void onString(String str) {
        System.out.printf("String: %s%n", str);
    }

    @Subscriber
    public static void onStaticString(String str) {
        System.out.printf("Static String: %s%n", str);
    }

    @Subscriber
    public static void onStaticInteger(Integer i) {
        System.out.printf("Static Integer: %s%n", i);
    }

    @Subscriber
    public void onInteger(Integer i) {
        System.out.printf("Integer: %s%n", i);
    }

    @MultiSubscriber
    public void multi(Object obj) {
        System.out.printf("Multi: %s%n", obj);
    }

    @MultiSubscriber
    public static void staticMulti(Object obj) {
        System.out.printf("Static Multi: %s%n", obj);
    }

}