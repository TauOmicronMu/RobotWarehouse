package warehouse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by samtebbs on 24/02/2016.
 */
public class EventDispatcher {

    private HashMap<Object, HashMap<Class, Method>> subscribers = new HashMap<>();
    public static final EventDispatcher INSTANCE = new EventDispatcher();

    /**
     * Called when an event occurs, and dispatches the event to all subscriber methods that have a parameter of the event's type.
     * @param obj - The event object
     **/
    public void onEvent(Object obj) {
        // Look through the subscribed objects and check if they have a method that accepts the packet's class
        for (Object subscriber : subscribers.keySet()) {
            HashMap<Class, Method> subscriberMethods = subscribers.get(subscriber);
            if (subscriberMethods.containsKey(obj)) {
                try {
                    // Invoke the method from the subscriber object with the obj as the argument
                    subscriberMethods.get(obj.getClass()).invoke(isClass(subscriber) ? null : subscriber, obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void onEvent(Object obj) {
        INSTANCE.onEvent(obj);
    }
    
    private static boolean isClass(Object obj) {
        return obj.getClass() == Class.class;
    }

    /**
     * Subscribe to events with an object or a class.
     * All method annotated with Subscriber will be added to the subscriber list.
     * @param obj - Object of the class that has the subscriber methods, or the class itself
     */
    public void subscribe(Object classObj) {
        // If classObj is an object of Class, then use that, else extract the class from the object.
        Class cls = isClass(classObj) ? (Class) classObj : obj.getClass();
        HashMap<Class, Method> map = new HashMap<>();
        subscribers.put(obj, map);
        // Filter class' methods for those that have one parameter and have the subscriber annotation
        List<Method> methods = Arrays.stream(cls.getMethods()).filter(mth -> mth.getParameters().length == 0 && mth.getAnnotationsByType(Subscriber.class).length > 0);
        methods.forEach(mth -> map.put(mth.getParameters()[0].getType(), mth))
    }
    
    public static void subscribe(Object obj) {
        INSTANCE.subscribe(obj);
    }

}
