package org.bff.javampd;

import org.bff.javampd.events.*;
import org.bff.javampd.exception.MPDAdminException;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.properties.AdminProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * MPDAdmin represents a administrative controller to a MPD server.  To obtain
 * an instance of the class you must use the <code>getMPDAdmin</code> method from the
 * {@link MPD} connection class.  This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class MPDAdmin extends CommandExecutor {
    private List<MPDChangeListener> listeners =
            new ArrayList<MPDChangeListener>();
    private List<OutputChangeListener> outputListeners =
            new ArrayList<OutputChangeListener>();

    protected static final String OUTPUT_PREFIX_ID = "outputid:";
    protected static final String OUTPUT_PREFIX_NAME = "outputname:";
    protected static final String OUTPUT_PREFIX_ENABLED = "outputenabled:";

    private AdminProperties adminProperties;
    private MPDServerStatistics serverStatistics;

    /**
     * Constructor for MPDAdmin
     *
     * @param mpd the MPD Connection
     */
    MPDAdmin(MPD mpd) {
        super(mpd);
        this.serverStatistics = mpd.getMPDServerStatistics();
        this.adminProperties = new AdminProperties();
    }

    /**
     * Returns the information about all outputs
     *
     * @return a {@link Collection} of {@link MPDOutput}
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    public Collection<MPDOutput> getOutputs() throws MPDConnectionException, MPDResponseException {
        return new ArrayList<MPDOutput>(parseOutputs(sendMPDCommand(adminProperties.getOutputs())));
    }

    /**
     * Disables the passed {@link MPDOutput}
     *
     * @param output the output to disable
     * @return true if the output is disabled
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    public boolean disableOutput(MPDOutput output) throws MPDConnectionException, MPDResponseException {
        fireOutputChangeEvent(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED);
        return sendMPDCommand(adminProperties.getOutputDisable(), output.getId()).isEmpty();
    }

    /**
     * Enables the passed {@link MPDOutput}
     *
     * @param output the output to enable
     * @return true if the output is enabled
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    public boolean enableOutput(MPDOutput output) throws MPDConnectionException, MPDResponseException {
        fireOutputChangeEvent(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED);
        return sendMPDCommand(adminProperties.getOutputEnable(), output.getId()).isEmpty();
    }

    private Collection<MPDOutput> parseOutputs(Collection<String> response) {
        List<MPDOutput> outputs = new ArrayList<MPDOutput>();
        Iterator<String> iter = response.iterator();
        String line = null;

        while (iter.hasNext()) {
            if (line == null || (!line.startsWith(OUTPUT_PREFIX_ID))) {
                line = iter.next();
            }

            if (line.startsWith(OUTPUT_PREFIX_ID)) {
                MPDOutput output = new MPDOutput(Integer.parseInt(line.substring(OUTPUT_PREFIX_ID.length()).trim()));

                while (!(line = (String) iter.next()).startsWith(OUTPUT_PREFIX_ID)) {
                    if (line.startsWith(OUTPUT_PREFIX_NAME)) {
                        output.setName(line.replace(OUTPUT_PREFIX_NAME, "").trim());
                    } else if (line.startsWith(OUTPUT_PREFIX_ENABLED)) {
                        output.setEnabled("1".equals(line.replace(OUTPUT_PREFIX_ENABLED, "").trim()));
                    }
                    if (!iter.hasNext()) {
                        break;
                    }
                }
                outputs.add(output);
            }
        }
        return outputs;
    }

    /**
     * Adds a {@link MPDChangeListener} to this object to receive
     * {@link MPDChangeEvent}s.
     *
     * @param mcl the MPDChangeListener to add
     */
    public synchronized void addMPDChangeListener(MPDChangeListener mcl) {
        listeners.add(mcl);
    }

    /**
     * Removes a {@link MPDChangeListener} from this object.
     *
     * @param mcl the MPDChangeListener to remove
     */
    public synchronized void removePlayerChangedListener(MPDChangeListener mcl) {
        listeners.remove(mcl);
    }

    /**
     * Sends the appropriate {@link MPDChangeEvent} to all registered
     * {@link MPDChangeListener}s.
     *
     * @param event the {@link MPDChangeEvent.Event} to send
     */
    protected synchronized void fireMPDChangeEvent(MPDChangeEvent.Event event) {
        MPDChangeEvent mce = new MPDChangeEvent(this, event);

        for (MPDChangeListener mcl : listeners) {
            mcl.mpdChanged(mce);
        }
    }

    /**
     * Kills the mpd connection.
     *
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDAdminException      if the MPD response contains an error
     */
    public void killMPD() throws MPDConnectionException, MPDAdminException {
        try {
            sendMPDCommand(adminProperties.getKill());
            fireMPDChangeEvent(MPDChangeEvent.Event.MPD_KILLED);
        } catch (MPDResponseException re) {
            throw new MPDAdminException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDAdminException(e);
        }
    }

    /**
     * Updates the MPD database by searching the mp3 directory for new music and
     * removing the old music.
     *
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDAdminException      if the MPD response contains an error
     */
    public void updateDatabase() throws MPDConnectionException, MPDAdminException {
        try {
            sendMPDCommand(adminProperties.getRefresh());
            fireMPDChangeEvent(MPDChangeEvent.Event.MPD_REFRESHED);
        } catch (MPDResponseException re) {
            throw new MPDAdminException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDAdminException(e);
        }
    }

    /**
     * Updates the MPD database by searching a specific mp3 directory for new music and removing the old music.
     *
     * @param path the path
     * @throws MPDConnectionException the mPD connection exception
     * @throws MPDAdminException      the mPD admin exception
     */
    public void updateDatabase(String path) throws MPDConnectionException, MPDAdminException {
        try {
            sendMPDCommand(adminProperties.getRefresh(), path);
            fireMPDChangeEvent(MPDChangeEvent.Event.MPD_REFRESHED);
        } catch (MPDResponseException re) {
            throw new MPDAdminException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDAdminException(e);
        }
    }

    /**
     * Returns the daemon uptime in seconds.
     *
     * @return the daemon uptime in seconds
     * @throws MPDAdminException      if the MPD response contains an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public long getDaemonUpTime() throws MPDConnectionException, MPDAdminException {
        try {
            return serverStatistics.getUptime();
        } catch (MPDResponseException re) {
            throw new MPDAdminException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDAdminException(e);
        }
    }

    /**
     * Adds a {@link PlaylistChangeListener} to this object to receive
     * {@link PlaylistChangeEvent}s.
     *
     * @param pcl the PlaylistChangeListener to add
     */
    public synchronized void addOutputChangeListener(OutputChangeListener pcl) {
        outputListeners.add(pcl);
    }

    /**
     * Removes a {@link PlaylistChangeListener} from this object.
     *
     * @param pcl the PlaylistChangeListener to remove
     */
    public synchronized void removePlaylistStatusChangedListener(OutputChangeListener pcl) {
        outputListeners.remove(pcl);
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     *
     * @param event the event id to send
     */
    protected synchronized void fireOutputChangeEvent(OutputChangeEvent.OUTPUT_EVENT event) {
        OutputChangeEvent oce = new OutputChangeEvent(this, event);

        for (OutputChangeListener pcl : outputListeners) {
            pcl.outputChanged(oce);
        }
    }
}
