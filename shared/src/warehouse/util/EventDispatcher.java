package warehouse.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by samtebbs on 24/02/2016.
 */
public class EventDispatcher {

    public static final EventDispatcher INSTANCE = new EventDispatcher();
    private final HashMap<Class, List<Pair<Object, List<Method>>>> subscribers = new HashMap<Class, List<Pair<Object, List<Method>>>>();
    private final List<Pair<Object, List<Method>>> multiSubscribers = new ArrayList<Pair<Object, List<Method>>>();

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
        if(subscribers.containsKey(obj.getClass())) {
            List<Pair<Object, List<Method>>> subscriberObjects = subscribers.get(obj.getClass());
            for (Pair<Object, List<Method>> pair : subscriberObjects)
                for (Method method : pair.e) invoke(method, pair.t, obj);
            /* subscriberObjects.forEach(pair -> {
                pair.e.forEach(method -> invoke(method, pair.t, obj));
            }); */
        }
        for (Pair<Object, List<Method>> pair : multiSubscribers)
            for (Method method : pair.e) invoke(method, pair.t, obj);
        //multiSubscribers.forEach(pair -> pair.e.forEach(mth  -> invoke(mth, pair.t, obj)));
    }

    private void invoke(Method method, Object subscriber, Object obj) {
        try {
            method.invoke(subscriber, obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Subscribe to events with an object.
     * All method annotated with Subscriber will be added to the subscriber list.
     * @param obj - Object of the class that has the subscriber methods
     */
    public void subscribe(Object obj) {
        Class cls;
        if(obj.getClass() == Class.class) cls = (Class) obj;
        else cls = obj.getClass();

        HashMap<Class, List<Method>> clsMethodMap = new HashMap<Class, List<Method>>();
        List<Method> methods = getMethods(Subscriber.class, cls.getMethods());
        for (Method method : methods) {
            Class methodParam = method.getParameterTypes()[0];
            if (clsMethodMap.containsKey(methodParam)) clsMethodMap.get(methodParam).add(method);
            else {
                ArrayList<Method> list = new ArrayList<Method>();
                clsMethodMap.put(methodParam, list);
                list.add(method);
            }
        }
        /* methods.forEach(method -> {
            Class methodParam = method.getParameters()[0].getType();
            if(clsMethodMap.containsKey(methodParam)) clsMethodMap.get(methodParam).add(method);
            else {
                ArrayList<Method> list = new ArrayList<Method>();
                clsMethodMap.put(methodParam, list);
                list.add(method);
            }
        }); */
        for (Class key : clsMethodMap.keySet()) {
            if(subscribers.containsKey(key)) subscribers.get(key).add(new Pair(obj, clsMethodMap.get(key)));
            else {
                ArrayList<Pair<Object, List<Method>>> list = new ArrayList<Pair<Object, List<Method>>>();
                subscribers.put(key, list);
                list.add(new Pair(obj, clsMethodMap.get(key)));
            }
        }
        /* clsMethodMap.keySet().forEach(key -> {
            if(subscribers.containsKey(key)) subscribers.get(key).add(new Pair(obj, clsMethodMap.get(key)));
            else {
                ArrayList<Pair<Object, List<Method>>> list = new ArrayList<Pair<Object, List<Method>>>();
                subscribers.put(key, list);
                list.add(new Pair(obj, clsMethodMap.get(key)));
            }LCD.clear();
		LCD.drawString("Msg: " + event.packet.toString(), 0, 0);
        });*/

        // Add the multi subscriber methods
        List<Method> multiMethods = getMethods(MultiSubscriber.class, cls.getMethods());
        if(multiMethods.size() > 0) multiSubscribers.add(new Pair(obj, multiMethods));
    }

    private List<Method> getMethods(Class annotationClass, Method[] methods) {
        ArrayList<Method> list = new ArrayList<Method>();
        for (Method mth : methods)
            if (mth.getParameterTypes().length == 1 && mth.getAnnotation(annotationClass) != null) list.add(mth);
        return list;
    }
}
