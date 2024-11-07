public interface PlayerMoveEventListener extends java.util.EventListener //  java.util.EventListener is a marker interface used to indicate that a class is a listener for events in Java’s event-handling system.
{
    public void eventOccured(PlayerMoveEvent evt) throws java.io.IOException;
    // This interface specifies a single method, eventOccured, which must be implemented by any class that wants to handle ExeterWriteEvent notifications
}