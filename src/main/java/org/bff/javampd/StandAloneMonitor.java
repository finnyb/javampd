package org.bff.javampd;

import org.bff.javampd.events.*;

/**
 * @author bill
 * @since: 11/24/13 4:27 PM
 */
public interface StandAloneMonitor extends Runnable {
    /**
     * Adds a {@link org.bff.javampd.events.PlayerBasicChangeListener} to this object to receive
     * {@link org.bff.javampd.events.PlayerChangeEvent}s.
     *
     * @param pcl the PlayerBasicChangeListener to add
     */
    void addPlayerChangeListener(PlayerBasicChangeListener pcl);

    /**
     * Removes a {@link org.bff.javampd.events.PlayerBasicChangeListener} from this object.
     *
     * @param pcl the PlayerBasicChangeListener to remove
     */
    void removePlayerChangeListener(PlayerBasicChangeListener pcl);

    /**
     * Adds a {@link org.bff.javampd.events.VolumeChangeListener} to this object to receive
     * {@link org.bff.javampd.events.VolumeChangeEvent}s.
     *
     * @param vcl the VolumeChangeListener to add
     */
    void addVolumeChangeListener(VolumeChangeListener vcl);

    /**
     * Removes a {@link org.bff.javampd.events.VolumeChangeListener} from this object.
     *
     * @param vcl the VolumeChangeListener to remove
     */
    void removeVolumeChangedListener(VolumeChangeListener vcl);

    /**
     * Adds a {@link org.bff.javampd.events.OutputChangeListener} to this object to receive
     * {@link org.bff.javampd.events.OutputChangeEvent}s.
     *
     * @param vcl the OutputChangeListener to add
     */
    void addOutputChangeListener(OutputChangeListener vcl);

    /**
     * Removes a {@link org.bff.javampd.events.OutputChangeListener} from this object.
     *
     * @param vcl the OutputChangeListener to remove
     */
    void removeOutputChangedListener(OutputChangeListener vcl);

    /**
     * Adds a {@link org.bff.javampd.events.PlaylistBasicChangeListener} to this object to receive
     * {@link org.bff.javampd.events.PlaylistChangeEvent}s.
     *
     * @param pcl the PlaylistChangeListener to add
     */
    void addPlaylistChangeListener(PlaylistBasicChangeListener pcl);

    /**
     * Removes a {@link org.bff.javampd.events.PlaylistBasicChangeListener} from this object.
     *
     * @param pcl the PlaylistBasicChangeListener to remove
     */
    void removePlaylistStatusChangedListener(PlaylistBasicChangeListener pcl);

    /**
     * Adds a {@link org.bff.javampd.events.MPDErrorListener} to this object to receive
     * {@link org.bff.javampd.events.MPDErrorEvent}s.
     *
     * @param el the MPDErrorListener to add
     */
    void addMPDErrorListener(MPDErrorListener el);

    /**
     * Removes a {@link org.bff.javampd.events.MPDErrorListener} from this object.
     *
     * @param el the MPDErrorListener to remove
     */
    void removeMPDErrorListener(MPDErrorListener el);

    /**
     * Implements the Runnable run method to monitor the MPD connection.
     */
    @Override
    void run();

    /**
     * Starts the monitor by creating and starting a thread using this instance
     * as the Runnable interface.
     */
    void start();

    /**
     * Stops the thread.
     */
    void stop();

    /**
     * Returns true if the monitor is stopped, false if the monitor is still running.
     *
     * @return true if monitor is running, false otherwise false
     */
    boolean isStopped();

    public enum PlayerResponse {
        PLAY("play"),
        STOP("stop"),
        PAUSE("pause");

        private String prefix;

        PlayerResponse(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return this.prefix;
        }
    }
}
