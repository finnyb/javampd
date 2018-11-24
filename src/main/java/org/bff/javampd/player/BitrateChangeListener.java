package org.bff.javampd.player;

/**
 * The listener interface for receiving bitrate events. The class that is
 * interested in processing a bitrate event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addBitrateChangeListener</CODE> method. When the
 * bitrate event occurs, that object's <CODE>bitrateChanged</CODE> method is
 * invoked.
 *
 * @author Bill
 */
public interface BitrateChangeListener {
    /**
     * Invoked when a bitrate change event occurs.
     *
     * @param event the {@link BitrateChangeEvent} fired
     */
    void bitrateChanged(BitrateChangeEvent event);
}
