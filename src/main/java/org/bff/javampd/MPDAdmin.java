/*
 * MPDAdmin.java
 *
 * Created on September 30, 2005, 5:57 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.javampd;

import org.bff.javampd.events.*;
import org.bff.javampd.exception.MPDAdminException;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;

import java.util.*;

/**
 * MPDAdmin represents a administrative controller to a MPD server.  To obtain
 * an instance of the class you must use the <code>getMPDAdmin</code> method from the
 * {@link MPD} connection class.  This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class MPDAdmin {

    private MPD mpd;
    private Properties prop;
    private List<MPDChangeListener> listeners =
            new ArrayList<MPDChangeListener>();
    private List<OutputChangeListener> outputListeners =
            new ArrayList<OutputChangeListener>();
    private static final String MPDPROPKILL = "MPD_ADMIN_KILL";
    private static final String MPDPROPREFRESH = "MPD_ADMIN_REFRESH";
    private static final String MPDPROPOUTPUTS = "MPD_ADMIN_OUTPUTS";
    private static final String MPDPROPOUTPUTENABLE = "MPD_ADMIN_ENABLE_OUT";
    private static final String MPDPROPOUTPUTDISABLE = "MPD_ADMIN_DISABLE_OUT";
    /**
     * output id prefix
     */
    protected static final String OUTPUT_PREFIX_ID = "outputid:";
    /**
     * output name prefix
     */
    protected static final String OUTPUT_PREFIX_NAME = "outputname:";
    /**
     * output enabled prefix
     */
    protected static final String OUTPUT_PREFIX_ENABLED = "outputenabled:";

    /**
     * Constructor for MPDAdmin
     *
     * @param mpd the MPD Connection
     */
    MPDAdmin(MPD mpd) {
        this.mpd = mpd;
        this.prop = mpd.getMPDProperties();
    }

    /**
     * Returns the information about all outputs
     *
     * @return a {@link Collection} of {@link MPDOutput}
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    public Collection<MPDOutput> getOutputs() throws MPDConnectionException, MPDResponseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPOUTPUTS));
        return new ArrayList<MPDOutput>(parseOutputs(mpd.sendMPDCommand(command)));
    }

    /**
     * Disables the passed {@link MPDOutput}
     *
     * @param output the output to disable
     * @return true if the output is disabled
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    public boolean disableOutput(MPDOutput output) throws MPDConnectionException, MPDResponseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPOUTPUTDISABLE), Integer.toString(output.getId()));
        fireOutputChangeEvent(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED);
        return mpd.sendMPDCommand(command).isEmpty();
    }

    /**
     * Enables the passed {@link MPDOutput}
     *
     * @param output the output to enable
     * @return true if the output is enabled
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    public boolean enableOutput(MPDOutput output) throws MPDConnectionException, MPDResponseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPOUTPUTENABLE), Integer.toString(output.getId()));
        fireOutputChangeEvent(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED);
        return mpd.sendMPDCommand(command).isEmpty();
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
     * @param id the event id to send
     */
    protected synchronized void fireMPDChangeEvent(int id) {
        MPDChangeEvent mce = new MPDChangeEvent(this, id);

        for (MPDChangeListener mcl : listeners) {
            mcl.mpdChanged(mce);
        }
    }

    /**
     * Kills the mpd connection.
     *
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     * @throws org.bff.javampd.exception.MPDAdminException
     *          if the MPD response contains an error
     */
    public void killMPD() throws MPDConnectionException, MPDAdminException {
        try {
            mpd.sendMPDCommand(new MPDCommand(prop.getProperty(MPDPROPKILL)));
            fireMPDChangeEvent(MPDChangeEvent.MPD_KILLED);
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
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     * @throws org.bff.javampd.exception.MPDAdminException
     *          if the MPD response contains an error
     */
    public void updateDatabase() throws MPDConnectionException, MPDAdminException {
        try {
            mpd.sendMPDCommand(new MPDCommand(prop.getProperty(MPDPROPREFRESH)));
            fireMPDChangeEvent(MPDChangeEvent.MPD_REFRESHED);
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
            mpd.sendMPDCommand(new MPDCommand(prop.getProperty(MPDPROPREFRESH), path));
            fireMPDChangeEvent(MPDChangeEvent.MPD_REFRESHED);
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
     * @throws org.bff.javampd.exception.MPDAdminException
     *          if the MPD response contains an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public int getDaemonUpTime() throws MPDConnectionException, MPDAdminException {
        try {
            return (Integer.parseInt(mpd.getServerStat(MPD.StatList.UPTIME)));
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
