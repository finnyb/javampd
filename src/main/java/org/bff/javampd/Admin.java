package org.bff.javampd;

import org.bff.javampd.events.MPDChangeListener;
import org.bff.javampd.events.OutputChangeListener;
import org.bff.javampd.exception.MPDAdminException;

import java.util.Collection;

/**
 * Performs {@link MPD} administrative tasks
 *
 * @author bill
 * @since: 11/24/13 9:40 AM
 */
public interface Admin {
    /**
     * Returns the information about all outputs
     *
     * @return a <code>Collection</code> of {@link MPDOutput}
     * @throws MPDAdminException if there is a problem sending the command to the server
     */
    Collection<MPDOutput> getOutputs() throws MPDAdminException;

    /**
     * Disables the passed {@link MPDOutput}
     *
     * @param output the output to disable
     * @return true if the output is disabled
     * @throws MPDAdminException if there is a problem sending the command to the server
     */
    boolean disableOutput(MPDOutput output) throws MPDAdminException;

    /**
     * Enables the passed {@link MPDOutput}
     *
     * @param output the output to enable
     * @return true if the output is enabled
     * @throws MPDAdminException if there is a problem sending the command to the server
     */
    boolean enableOutput(MPDOutput output) throws MPDAdminException;

    /**
     * Adds a {@link MPDChangeListener} to this object to receive
     * {@link org.bff.javampd.events.MPDChangeEvent}s.
     *
     * @param mcl the MPDChangeListener to add
     */
    void addMPDChangeListener(MPDChangeListener mcl);

    /**
     * Removes a {@link MPDChangeListener} from this object.
     *
     * @param mcl the MPDChangeListener to remove
     */
    void removePlayerChangedListener(MPDChangeListener mcl);

    /**
     * Kills the mpd connection.
     *
     * @throws MPDAdminException if there is a problem sending the command
     */
    void killMPD() throws MPDAdminException;

    /**
     * Updates the MPD database by searching the mp3 directory for new music and
     * removing the old music.
     *
     * @throws MPDAdminException if there is a problem sending the command
     */
    void updateDatabase() throws MPDAdminException;

    /**
     * Updates the MPD database by searching a specific mp3 directory for new music and removing the old music.
     *
     * @param path the path
     * @throws MPDAdminException the MPD connection exception
     */
    void updateDatabase(String path) throws MPDAdminException;

    /**
     * Returns the daemon uptime in seconds.
     *
     * @return the daemon uptime in seconds
     * @throws MPDAdminException if the MPD response contains an error
     */
    long getDaemonUpTime() throws MPDAdminException;

    /**
     * Adds a {@link OutputChangeListener} to this object to receive
     * {@link org.bff.javampd.events.PlaylistChangeEvent}s.
     *
     * @param pcl the PlaylistChangeListener to add
     */
    void addOutputChangeListener(OutputChangeListener pcl);

    /**
     * Removes a {@link OutputChangeListener} from this object.
     *
     * @param pcl the PlaylistChangeListener to remove
     */
    void removePlaylistStatusChangedListener(OutputChangeListener pcl);
}
