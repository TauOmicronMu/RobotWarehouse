import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by samtebbs on 24/02/2016.
 */
public class EventDispatcher {

    private HashMap<Object, HashMap<Class, Method>> subscribers = new HashMap<>();

    public void onEvent(Object packet) {
        for (Object subscriber : subscribers.keySet()) {
            HashMap<Class, Method> subscriberMethods = subscribers.get(subscriber);
            if (subscriberMethods.containsKey(packet.getClass())) {
                try {
                    subscriberMethods.get(packet.getClass()).invoke(subscriber, packet);
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
     *
     * @param obj
     */
    public void subscribe(Object obj) {
        Class cls = obj.getClass();
        HashMap<Class, Method> map = new HashMap<>();
        subscribers.put(obj, map);
        // Search through the class' methods for those that should receive the packets
        for (Method mth : cls.getMethods()) {
            // If the method has the @Subscriber annotation and it takes one parameter, add it
            if (mth.getParameters().length == 1 && mth.getAnnotationsByType(Subscriber.class).length > 0) {
                map.put(mth.getParameters()[0].getType(), mth);
                break;
            }
        }
    }

}
