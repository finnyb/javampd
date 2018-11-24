package org.bff.javampd.admin;

/**
 * The listener interface for receiving MPD administrative change events. The class
 * that is interested in processing a change event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addMPDChangeListener</CODE> method. When the change
 * event occurs, that object's connectionChangeEventReceived method is invoked.
 *
 * @author Bill
 */
public interface MPDChangeListener {
    /**
     * Invoked when a mpd administrative change event occurs.
     *
     * @param event the {@link MPDChangeEvent} received
     */
    void mpdChanged(MPDChangeEvent event);
}
