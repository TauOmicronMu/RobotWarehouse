/**
 * Created by samtebbs on 24/02/2016.
 */
public class EventExample {

    public static void main(String[] args) {
        EventDispatcher dispatcher = new EventDispatcher();
        dispatcher.subscribe(new EventExample());
        // As we are sending a String to the onEvent method, our subscriber method with a String parameter will be called
        dispatcher.onPacket("Hi!");
        // Now the subscriber method with the Integer parameter will be called
        dispatcher.onPacket(new Integer(3));
    }

    @Subscriber
    public void onStringEvent(String str) {
        System.out.println("String: " + str);
    }

    @Subscriber
    public void onIntegerEvent(Integer i) {
        System.out.println("Integer: " + i);
    }

}
