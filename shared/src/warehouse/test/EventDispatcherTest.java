package warehouse.test;

import warehouse.util.EventDispatcher;
import warehouse.util.Subscriber;

import static org.junit.Assert.assertEquals;

/**
 * Created by samtebbs on 06/03/2016.
 */
public class EventDispatcherTest {

    static Object string = "", integer = 0;

    @Subscriber
    public static void onString(String str) {
        string = str;
    }

    @Subscriber
    public static void onInteger(Integer i) {
        integer = i;
    }

    @org.junit.Before
    public void setUp() throws Exception {
        EventDispatcher.subscribe2(EventDispatcherTest.class);
    }

    @org.junit.Test
    public void testOnEvent2() throws Exception {
        EventDispatcher.onEvent2("Test123");
        assertEquals(string, "Test123");
        assertEquals(integer, 0);
        string = "";

        EventDispatcher.onEvent2(new Integer(1234));
        assertEquals(string, "");
        assertEquals(integer, 1234);
    }

}