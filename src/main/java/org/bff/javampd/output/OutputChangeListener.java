package org.bff.javampd.output;

/**
 * The listener interface for receiving output change events. The class that is
 * interested in processing a output change event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addOutputChangeListener</CODE> method.
 * When the event occurs, that object's <CODE>outputChanged</CODE> method
 * is invoked.
 *
 * @author Bill
 * @version 1.0
 */
public interface OutputChangeListener {
    /**
     * Invoked when a mpd {@link OutputChangeEvent} occurs.
     *
     * @param event the event received
     */
    void outputChanged(OutputChangeEvent event);
}
