package org.bff.javampd;

import org.bff.javampd.events.*;

/**
 * @author bill
 * @since: 11/24/13 4:33 PM
 */
public interface EventRelayer extends Runnable, PlayerChangeListener, PlaylistChangeListener, MPDChangeListener, OutputChangeListener {
    /**
     * Invoked when a player change event occurs.
     *
     * @param event the {@link org.bff.javampd.events.PlayerChangeEvent.Event} fired
     */
    @Override
    void playerChanged(PlayerChangeEvent event);

    /**
     * Invoked when a mpd administrative change action occurs.
     *
     * @param event the event received
     */
    @Override
    void mpdChanged(MPDChangeEvent event);

    /**
     * Invoked when a mpd playlist change event occurs.
     *
     * @param event the event received
     */
    @Override
    void playlistChanged(PlaylistChangeEvent event);

    /**
     * Adds a {@link org.bff.javampd.events.PlayerChangeListener} to this object to receive
     * {@link org.bff.javampd.events.PlayerChangeEvent}s.
     *
     * @param pcl the PlayerChangeListener to add
     */
    void addPlayerChangeListener(PlayerChangeListener pcl);

    /**
     * Removes a {@link org.bff.javampd.events.PlayerChangeListener} from this object.
     *
     * @param pcl the PlayerChangeListener to remove
     */
    void removePlayerChangedListener(PlayerChangeListener pcl);

    /**
     * Adds a {@link org.bff.javampd.events.MPDChangeListener} to this object to receive
     * {@link org.bff.javampd.events.MPDChangeEvent}s.
     *
     * @param mcl the MPDChangeListener to add
     */
    void addMPDChangeListener(MPDChangeListener mcl);

    /**
     * Removes a {@link org.bff.javampd.events.MPDChangeListener} from this object.
     *
     * @param mcl the MPDChangeListener to remove
     */
    void removeMPDChangedListener(MPDChangeListener mcl);

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

    @Override
    void outputChanged(OutputChangeEvent event);

    void setServer(Server server);
}
