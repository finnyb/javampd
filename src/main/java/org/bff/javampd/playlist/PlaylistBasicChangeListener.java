package org.bff.javampd.playlist;

/**
 * The listener interface for receiving basic playlist events. The class that
 * is interested in processing a basic playlist event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addPlaylistChangeListener</CODE> method. When the
 * playlist event occurs, that object's <CODE>playlistBasicChange</CODE> method is
 * invoked.
 *
 * @author Bill
 * @version 1.0
 */
public interface PlaylistBasicChangeListener {
    /**
     * Invoked when a playlist event occurs.
     *
     * @param event the event fired
     */
    void playlistBasicChange(PlaylistBasicChangeEvent event);
}
