package org.bff.javampd.admin;

import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.output.OutputChangeListener;

import java.util.Collection;

/**
 * Performs {@link org.bff.javampd.server.MPD} administrative tasks
 *
 * @author bill
 */
public interface Admin {
    /**
     * Returns the information about all outputs
     *
     * @return a <code>Collection</code> of {@link org.bff.javampd.output.MPDOutput}
     */
    Collection<MPDOutput> getOutputs();

    /**
     * Disables the passed {@link MPDOutput}
     *
     * @param output the output to disable
     * @return true if the output is disabled
     */
    boolean disableOutput(MPDOutput output);

    /**
     * Enables the passed {@link MPDOutput}
     *
     * @param output the output to enable
     * @return true if the output is enabled
     */
    boolean enableOutput(MPDOutput output);

    /**
     * Adds a {@link MPDChangeListener} to this object to receive
     * {@link MPDChangeEvent}s.
     *
     * @param mcl the MPDChangeListener to add
     */
    void addMPDChangeListener(MPDChangeListener mcl);

    /**
     * Removes a {@link MPDChangeListener} from this object.
     *
     * @param mcl the MPDChangeListener to remove
     */
    void removeMPDChangeListener(MPDChangeListener mcl);

    /**
     * Kills the mpd connection.
     */
    void killMPD();

    /**
     * Updates the MPD database by searching the mp3 directory for new music and
     * removing the old music.
     */
    void updateDatabase();

    /**
     * Updates the MPD database by searching a specific mp3 directory for new music and removing the old music.
     *
     * @param path the path
     */
    void updateDatabase(String path);

    /**
     * Same as update, but also rescans unmodified files.
     */
    void rescanDatabase();

    /**
     * Returns the daemon uptime in seconds.
     *
     * @return the daemon uptime in seconds
     */
    long getDaemonUpTime();

    /**
     * Adds a {@link OutputChangeListener} to this object to receive
     * {@link org.bff.javampd.playlist.PlaylistChangeEvent}s.
     *
     * @param pcl the PlaylistChangeListener to add
     */
    void addOutputChangeListener(OutputChangeListener pcl);

    /**
     * Removes a {@link OutputChangeListener} from this object.
     *
     * @param pcl the PlaylistChangeListener to remove
     */
    void removeOutputChangeListener(OutputChangeListener pcl);
}
