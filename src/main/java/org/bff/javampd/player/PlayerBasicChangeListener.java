package org.bff.javampd.player;

/**
 * The listener interface for receiving basic player events. The class that is
 * interested in processing a player event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addBasicPlayerChangeListener</CODE> method. When
 * the player event occurs, that object's <CODE>playerBasicChange</CODE> method
 * is invoked.
 *
 * @author Bill
 * @version 1.0
 */
public interface PlayerBasicChangeListener {
    /**
     * Invoked when a player change event occurs.
     *
     * @param event the event fired
     */
    void playerBasicChange(PlayerBasicChangeEvent event);
}
