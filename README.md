# RobotWarehouse
Repository for the RP Warehouse - Group 1.3

## Listening for events
If you want a method to listen for a certain event, give it any name and one parameter, whose type is the type of event you want to listen for. Annotate the method with `@Subscriber`.

```
@Subscriber
public void onJobUpdate(JobUpdateEvent event) {

}
```

You then have to tell the EventDispatcher that this class has subscriber methods, to do so, you can either use a static initialiser or a constructor.
```
public class Test {

    static {
        EventDispatcher.subscribe2(Test.class)
    }

    public Test() {
        EventDispatcher.subscribe2(this)
    }

}
```

## Triggering events
If you have an event that you want to dispatch, you have to alert the EventDispatcher, which will then alert all of the event's subscribers.
```
EventDispatcher.onEvent2(eventOj)
```