package org.bff.javampd;

import com.google.inject.Inject;
import org.bff.javampd.events.*;
import org.bff.javampd.exception.MPDAdminException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.properties.AdminProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @author Bill
 */
public class MPDAdmin implements Admin {
    private final Logger logger = LoggerFactory.getLogger(MPDAdmin.class);

    private List<MPDChangeListener> listeners =
            new ArrayList<MPDChangeListener>();
    private List<OutputChangeListener> outputListeners =
            new ArrayList<OutputChangeListener>();

    protected static final String OUTPUT_PREFIX_ID = "outputid:";
    protected static final String OUTPUT_PREFIX_NAME = "outputname:";
    protected static final String OUTPUT_PREFIX_ENABLED = "outputenabled:";

    @Inject
    private AdminProperties adminProperties;
    @Inject
    private ServerStatistics serverStatistics;
    @Inject
    private CommandExecutor commandExecutor;

    @Override
    public Collection<MPDOutput> getOutputs() throws MPDAdminException {
        try {
            return new ArrayList<MPDOutput>(parseOutputs(commandExecutor.sendCommand(adminProperties.getOutputs())));
        } catch (MPDException e) {
            logger.error("Could not get outputs", e);
            throw new MPDAdminException(e);
        }
    }

    @Override
    public boolean disableOutput(MPDOutput output) throws MPDAdminException {
        fireOutputChangeEvent(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED);
        try {
            return commandExecutor.sendCommand(adminProperties.getOutputDisable(), output.getId()).isEmpty();
        } catch (MPDException e) {
            logger.error("Could not disable output {}", output, e);
            throw new MPDAdminException(e);
        }
    }

    @Override
    public boolean enableOutput(MPDOutput output) throws MPDAdminException {
        fireOutputChangeEvent(OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED);
        try {
            return commandExecutor.sendCommand(adminProperties.getOutputEnable(), output.getId()).isEmpty();
        } catch (MPDException e) {
            logger.error("Could not enable output {}", output, e);
            throw new MPDAdminException(e);
        }
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

    @Override
    public synchronized void addMPDChangeListener(MPDChangeListener mcl) {
        listeners.add(mcl);
    }

    @Override
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

    @Override
    public void killMPD() throws MPDAdminException {
        try {
            commandExecutor.sendCommand(adminProperties.getKill());
            fireMPDChangeEvent(MPDChangeEvent.Event.MPD_KILLED);
        } catch (MPDResponseException re) {
            throw new MPDAdminException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDAdminException(e);
        }
    }

    @Override
    public void updateDatabase() throws MPDAdminException {
        try {
            commandExecutor.sendCommand(adminProperties.getRefresh());
            fireMPDChangeEvent(MPDChangeEvent.Event.MPD_REFRESHED);
        } catch (MPDResponseException re) {
            throw new MPDAdminException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDAdminException(e);
        }
    }

    @Override
    public void updateDatabase(String path) throws MPDAdminException {
        try {
            commandExecutor.sendCommand(adminProperties.getRefresh(), path);
            fireMPDChangeEvent(MPDChangeEvent.Event.MPD_REFRESHED);
        } catch (MPDResponseException re) {
            throw new MPDAdminException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDAdminException(e);
        }
    }

    @Override
    public long getDaemonUpTime() throws MPDAdminException {
        try {
            return serverStatistics.getUptime();
        } catch (MPDResponseException re) {
            throw new MPDAdminException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDAdminException(e);
        }
    }

    @Override
    public synchronized void addOutputChangeListener(OutputChangeListener pcl) {
        outputListeners.add(pcl);
    }

    @Override
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
