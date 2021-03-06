# RobotWarehouse
Repository for the RP Warehouse - Group 1.3

## Listening for events
If you want a method to listen for a certain event, give it any name and one parameter, whose type is the type of event you want to listen for. Annotate the method with `@Subscriber`.

```
@Subscriber
public void onJobUpdate(JobUpdateEvent event) {

}
```

If you want to make a method that listens to all event types, do the same but use the `MutliSubscriber` annotation and use `Object` as the parameter's type.

You then have to tell the EventDispatcher that this class has subscriber methods by *one* of the two methods below. If you have static subscriber methods, you can either use a static initialiser or use the constructor method. If you have non-static subscriber methods, then you must use the constructor method.
```
public class Test {

    // This method uses a static initialiser. https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
    static {
        EventDispatcher.subscribe2(Test.class)
    }

    // This method uses a constructor
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

## Pulling the latest commits from the dev branch
To get the latest changes, make sure you are in your role's repo and do:
```
git merge origin/dev
```
