package org.bff.javampd;

import org.bff.javampd.events.MPDChangeListener;
import org.bff.javampd.events.OutputChangeListener;
import org.bff.javampd.exception.MPDAdminException;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;

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
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    Collection<MPDOutput> getOutputs() throws MPDConnectionException, MPDResponseException;

    /**
     * Disables the passed {@link MPDOutput}
     *
     * @param output the output to disable
     * @return true if the output is disabled
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    boolean disableOutput(MPDOutput output) throws MPDConnectionException, MPDResponseException;

    /**
     * Enables the passed {@link MPDOutput}
     *
     * @param output the output to enable
     * @return true if the output is enabled
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    boolean enableOutput(MPDOutput output) throws MPDConnectionException, MPDResponseException;

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
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDAdminException      if the MPD response contains an error
     */
    void killMPD() throws MPDConnectionException, MPDAdminException;

    /**
     * Updates the MPD database by searching the mp3 directory for new music and
     * removing the old music.
     *
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDAdminException      if the MPD response contains an error
     */
    void updateDatabase() throws MPDConnectionException, MPDAdminException;

    /**
     * Updates the MPD database by searching a specific mp3 directory for new music and removing the old music.
     *
     * @param path the path
     * @throws MPDConnectionException the MPD connection exception
     * @throws MPDAdminException      the MPD admin exception
     */
    void updateDatabase(String path) throws MPDConnectionException, MPDAdminException;

    /**
     * Returns the daemon uptime in seconds.
     *
     * @return the daemon uptime in seconds
     * @throws MPDAdminException      if the MPD response contains an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    long getDaemonUpTime() throws MPDConnectionException, MPDAdminException;

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
