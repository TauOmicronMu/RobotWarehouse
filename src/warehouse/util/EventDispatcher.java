package warehouse.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by samtebbs on 24/02/2016.
 */
public class EventDispatcher {

    public static final EventDispatcher INSTANCE = new EventDispatcher();
    private HashMap<Object, HashMap<Class, Method>> subscribers = new HashMap<>();

    /**
     * Static wrapper for INSTANCE.onEvent()
     *
     * @param eventObj
     */
    public static void onEvent2(Object eventObj) {
        INSTANCE.onEvent(eventObj);
    }

    /**
     * Static wrapper for INSTANCE.subscribe()
     *
     * @param obj
     */
    public static void subscribe2(Object obj) {
        INSTANCE.subscribe(obj);
    }

    /**
     * Called when an event occurs, and dispatches the event to all subscriber methods that have a parameter of the event's type.
     * @param obj - The event object
     **/
    public void onEvent(Object obj) {
        // Look through the subscribed objects and check if they have a method that accepts the packet's class
        for (Object subscriber : subscribers.keySet()) {
            HashMap<Class, Method> subscriberMethods = subscribers.get(subscriber);
            if (subscriberMethods.containsKey(obj.getClass())) {
                try {
                    // Invoke the method from the subscriber object with the obj as the argument
                    subscriberMethods.get(obj.getClass()).invoke(subscriber, obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Subscribe to events with an object.
     * All method annotated with Subscriber will be added to the subscriber list.
     * @param obj - Object of the class that has the subscriber methods
     */
    public void subscribe(Object obj) {
        Class cls = obj.getClass();
        HashMap<Class, Method> map = new HashMap<>();
        subscribers.put(obj, map);
        // Filter class' methods for those that have one parameter and have the subscriber annotation
        List<Method> methods = Arrays.stream(cls.getMethods()).filter(mth -> mth.getParameters().length == 0 && mth.getAnnotationsByType(Subscriber.class).length > 0).collect(Collectors.toList());
        methods.forEach(mth -> map.put(mth.getParameters()[0].getType(), mth));
    }

}
