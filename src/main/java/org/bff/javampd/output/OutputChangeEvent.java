package org.bff.javampd.output;

/**
 * Represents a change in the outputs of a {@link MPDOutput}.
 *
 * @author Bill
 * @version 1.0
 */
public class OutputChangeEvent
        extends java.util.EventObject {

    private Event event;

    public enum Event {
        OUTPUT_ADDED,
        OUTPUT_DELETED,
        OUTPUT_CHANGED
    }

    /**
     * Creates a new instance of OutputChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param event  the output event
     */
    public OutputChangeEvent(Object source, Event event) {
        super(source);
        this.event = event;
    }


    /**
     * Returns the {@link org.bff.javampd.output.OutputChangeEvent.Event} for this event.
     *
     * @return the event
     */
    public Event getEvent() {
        return event;
    }
}
