package org.bff.javampd.player;

/**
 * The listener interface for receiving volume change events. The class that is
 * interested in processing a volume change event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addVolumeChangeListener</CODE> method.
 * When the position event occurs, that object's <CODE>volumeChanged</CODE> method
 * is invoked.
 *
 * @author Bill
 * @version 1.0
 */
public interface VolumeChangeListener {
    /**
     * Invoked when a mpd volume change event occurs.
     *
     * @param event the event received
     */
    void volumeChanged(VolumeChangeEvent event);
}
