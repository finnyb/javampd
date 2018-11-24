package org.bff.javampd.server;

/**
 * The listener interface for receiving connection change events. The class
 * that is interested in processing a connection event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addConnectionChangeListener</CODE> method. When the
 * connection event occurs, that object's <CODE>connectionChangeEventReceived</CODE>
 * method is invoked.
 *
 * @author Bill
 * @version 1.0
 */
public interface ConnectionChangeListener {
    /**
     * Invoked when a connection change event occurs.
     *
     * @param event the event received
     */
    void connectionChangeEventReceived(ConnectionChangeEvent event);
}
