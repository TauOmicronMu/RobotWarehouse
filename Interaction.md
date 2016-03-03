## Interaction
This file serves as a preliminary guide to how interaction should take place between components. It is written from my (Sam's) point of view as a system integrator and with my java experience in mind.
If you have any suggestions, please let me know and we can discuss changes to this document.

## Events
All interaction between components of the system should take place with events. I will use `onEvent()` as a shortcut for `EventDispatcher.onEvent2()` and `subscribe()` as a shortcut for `EventDispatcher.subscribe()`.
Event dispatching is the most maintainable and scalable way of handling interaction. If you are at all confused about how to dispatch and listen for events, please see the [README](README.md).

## Networking
There are 3 different ways that we could go about this. Depending on the method chose, I'll submit a proposed plan.

### Martin's code
This is what I think is the current state of the networking system, from what Jakubas has said and from what I have seen in the git repo.
I consider this to the least favourable option, as Martin's code is bloated (there are a lot of redundant classes and it is somewhat specialised for the softweare workshop assignment).
Martin also has this obsession with creating threads for everything, which is unnecessary and just worsens maintainability and scalability.

### Starting from scratch
Those working on the networking system could create a new system from scratch that is specialised for this project, but this would be the most time-consuming and may take some time to bug fix.

### Using my library
This will be the quickest and trouble-free method as my library makes it really easy to create a client and a server. 
This would however mean that those working on networking won't have much to do (except dispatching and listening to events), although, this means that they could then be moved to help with a different role.
I would need to make some minor changes to the library's source that I copied into this repo, but I'm happy to do that.

## Robot interface
The robot interface should listen for these events:
    * DropoffReached - occurs when the robot reaches the dropoff location, sent by the route execution component. This will have an ItemPickup object and should launch the item interface.

And it should send these events:
    * JobCancelled - Dispatched when the user chooses to cancel a job via the interface, the client should listen for this and alert the server
    * WrongPlace - Dispatched when the user tells the robot (via the interface) that it is in the wrong place, the client should listen to this and alert the server
    * JobCompleted - Dispatched when the robot interface detects that the job has been completed, the client should listen for this and alert the server
    
These events are based on the contents of Communication.java of the robot-interface branch. This file should be replaced by methods that listen for events and methods that dispatch the events.
    
