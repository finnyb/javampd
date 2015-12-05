package org.bff.javampd.playlist;

/**
 * The listener interface for receiving playlist events. The class that is
 * interested in processing a playlist event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addPlaylistChangeListener</CODE> method. When the
 * playlist event occurs, that object's <CODE>playlistChanged</CODE> method is
 * invoked.
 *
 * @author Bill
 * @version 1.0
 */
public interface PlaylistChangeListener {
    /**
     * Invoked when a playlist event occurs.
     *
     * @param event the event fired
     */
    void playlistChanged(PlaylistChangeEvent event);
}
