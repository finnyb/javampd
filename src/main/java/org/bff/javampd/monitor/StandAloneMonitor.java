package org.bff.javampd.monitor;

import org.bff.javampd.output.OutputChangeListener;
import org.bff.javampd.player.PlayerBasicChangeListener;
import org.bff.javampd.player.TrackPositionChangeListener;
import org.bff.javampd.player.VolumeChangeListener;
import org.bff.javampd.playlist.PlaylistBasicChangeListener;
import org.bff.javampd.server.ConnectionChangeListener;
import org.bff.javampd.server.MPDErrorListener;

/**
 * @author bill
 */
public interface StandAloneMonitor extends Runnable {
    /**
     * Adds a {@link org.bff.javampd.player.TrackPositionChangeListener} to this object to receive
     * {@link org.bff.javampd.player.PlayerChangeEvent}s.
     *
     * @param tpcl the TrackPositionChangeListener to add
     */
    void addTrackPositionChangeListener(TrackPositionChangeListener tpcl);

    /**
     * Removes a {@link org.bff.javampd.player.TrackPositionChangeListener} from this object.
     *
     * @param tpcl the TrackPositionChangeListener to remove
     */
    void removeTrackPositionChangeListener(TrackPositionChangeListener tpcl);

    /**
     * Adds a {@link org.bff.javampd.server.ConnectionChangeListener} to this object to receive
     * {@link org.bff.javampd.player.PlayerChangeEvent}s.
     *
     * @param ccl the ConnectionChangeListener to add
     */
    void addConnectionChangeListener(ConnectionChangeListener ccl);

    /**
     * Removes a {@link org.bff.javampd.server.ConnectionChangeListener} from this object.
     *
     * @param ccl the ConnectionChangeListener to remove
     */
    void removeConnectionChangeListener(ConnectionChangeListener ccl);

    /**
     * Adds a {@link org.bff.javampd.player.PlayerBasicChangeListener} to this object to receive
     * {@link org.bff.javampd.player.PlayerChangeEvent}s.
     *
     * @param pcl the PlayerBasicChangeListener to add
     */
    void addPlayerChangeListener(PlayerBasicChangeListener pcl);

    /**
     * Removes a {@link org.bff.javampd.player.PlayerBasicChangeListener} from this object.
     *
     * @param pcl the PlayerBasicChangeListener to remove
     */
    void removePlayerChangeListener(PlayerBasicChangeListener pcl);

    /**
     * Adds a {@link org.bff.javampd.player.VolumeChangeListener} to this object to receive
     * {@link org.bff.javampd.player.VolumeChangeEvent}s.
     *
     * @param vcl the VolumeChangeListener to add
     */
    void addVolumeChangeListener(VolumeChangeListener vcl);

    /**
     * Removes a {@link org.bff.javampd.player.VolumeChangeListener} from this object.
     *
     * @param vcl the VolumeChangeListener to remove
     */
    void removeVolumeChangedListener(VolumeChangeListener vcl);

    /**
     * Adds a {@link org.bff.javampd.output.OutputChangeListener} to this object to receive
     * {@link org.bff.javampd.output.OutputChangeEvent}s.
     *
     * @param vcl the OutputChangeListener to add
     */
    void addOutputChangeListener(OutputChangeListener vcl);

    /**
     * Removes a {@link org.bff.javampd.output.OutputChangeListener} from this object.
     *
     * @param vcl the OutputChangeListener to remove
     */
    void removeOutputChangedListener(OutputChangeListener vcl);

    /**
     * Adds a {@link org.bff.javampd.playlist.PlaylistBasicChangeListener} to this object to receive
     * {@link org.bff.javampd.playlist.PlaylistChangeEvent}s.
     *
     * @param pcl the PlaylistChangeListener to add
     */
    void addPlaylistChangeListener(PlaylistBasicChangeListener pcl);

    /**
     * Removes a {@link org.bff.javampd.playlist.PlaylistBasicChangeListener} from this object.
     *
     * @param pcl the PlaylistBasicChangeListener to remove
     */
    void removePlaylistStatusChangedListener(PlaylistBasicChangeListener pcl);

    /**
     * Adds a {@link org.bff.javampd.server.MPDErrorListener} to this object to receive
     * {@link org.bff.javampd.server.MPDErrorEvent}s.
     *
     * @param el the MPDErrorListener to add
     */
    void addMPDErrorListener(MPDErrorListener el);

    /**
     * Removes a {@link org.bff.javampd.server.MPDErrorListener} from this object.
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
