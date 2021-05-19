package org.bff.javampd.admin;

import com.google.inject.Inject;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.output.OutputChangeEvent;
import org.bff.javampd.output.OutputChangeListener;
import org.bff.javampd.statistics.ServerStatistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.bff.javampd.output.OutputChangeEvent.OUTPUT_EVENT;

/**
 * MPDAdmin represents a administrative controller to a MPD server.  To obtain
 * an instance of the class you must use the <code>getMPDAdmin</code> method from the
 * {@link org.bff.javampd.server.MPD} connection class.  This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill
 */
public class MPDAdmin implements Admin {

    private List<MPDChangeListener> listeners =
            new ArrayList<>();
    private List<OutputChangeListener> outputListeners =
            new ArrayList<>();

    protected static final String OUTPUT_PREFIX_ID = "outputid:";
    protected static final String OUTPUT_PREFIX_NAME = "outputname:";
    protected static final String OUTPUT_PREFIX_ENABLED = "outputenabled:";

    private AdminProperties adminProperties;
    private ServerStatistics serverStatistics;
    private CommandExecutor commandExecutor;

    @Inject
    public MPDAdmin(AdminProperties adminProperties,
                    ServerStatistics serverStatistics,
                    CommandExecutor commandExecutor) {
        this.adminProperties = adminProperties;
        this.serverStatistics = serverStatistics;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public Collection<MPDOutput> getOutputs() {
        return new ArrayList<>(parseOutputs(commandExecutor.sendCommand(adminProperties.getOutputs())));
    }

    @Override
    public boolean disableOutput(MPDOutput output) {
        fireOutputChangeEvent(OUTPUT_EVENT.OUTPUT_CHANGED);
        return commandExecutor.sendCommand(adminProperties.getOutputDisable(), output.getId()).isEmpty();
    }

    @Override
    public boolean enableOutput(MPDOutput output) {
        fireOutputChangeEvent(OUTPUT_EVENT.OUTPUT_CHANGED);
        return commandExecutor.sendCommand(adminProperties.getOutputEnable(), output.getId()).isEmpty();
    }

    private static Collection<MPDOutput> parseOutputs(Collection<String> response) {
        List<MPDOutput> outputs = new ArrayList<>();
        Iterator<String> iterator = response.iterator();
        String line = null;

        while (iterator.hasNext()) {
            if (line == null || (!line.startsWith(OUTPUT_PREFIX_ID))) {
                line = iterator.next();
            }

            if (line.startsWith(OUTPUT_PREFIX_ID)) {
                outputs.add(parseOutput(line, iterator));
            }
        }
        return outputs;
    }

    private static MPDOutput parseOutput(String startingLine, Iterator<String> iterator) {
        MPDOutput output = new MPDOutput(Integer.parseInt(startingLine.substring(OUTPUT_PREFIX_ID.length()).trim()));

        String line = iterator.next();

        while (!line.startsWith(OUTPUT_PREFIX_ID)) {

            if (line.startsWith(OUTPUT_PREFIX_NAME)) {
                output.setName(line.replace(OUTPUT_PREFIX_NAME, "").trim());
            } else if (line.startsWith(OUTPUT_PREFIX_ENABLED)) {
                output.setEnabled("1".equals(line.replace(OUTPUT_PREFIX_ENABLED, "").trim()));
            }
            if (!iterator.hasNext()) {
                break;
            }
            line = iterator.next();
        }

        return output;
    }

    @Override
    public synchronized void addMPDChangeListener(MPDChangeListener mcl) {
        listeners.add(mcl);
    }

    @Override
    public synchronized void removeMPDChangeListener(MPDChangeListener mcl) {
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
    public void killMPD() {
        commandExecutor.sendCommand(adminProperties.getKill());
        fireMPDChangeEvent(MPDChangeEvent.Event.KILLED);
    }

    @Override
    public void updateDatabase() {
        commandExecutor.sendCommand(adminProperties.getRefresh());
        fireMPDChangeEvent(MPDChangeEvent.Event.REFRESHED);
    }

    @Override
    public void updateDatabase(String path) {
        commandExecutor.sendCommand(adminProperties.getRefresh(), path);
        fireMPDChangeEvent(MPDChangeEvent.Event.REFRESHED);
    }

    @Override
    public void rescanDatabase() {
        commandExecutor.sendCommand(adminProperties.getRescan());
        fireMPDChangeEvent(MPDChangeEvent.Event.REFRESHED);
    }

    @Override
    public long getDaemonUpTime() {
        return serverStatistics.getUptime();
    }

    @Override
    public synchronized void addOutputChangeListener(OutputChangeListener pcl) {
        outputListeners.add(pcl);
    }

    @Override
    public synchronized void removeOutputChangeListener(OutputChangeListener pcl) {
        outputListeners.remove(pcl);
    }

    /**
     * Sends the appropriate {@link org.bff.javampd.playlist.PlaylistChangeEvent} to all registered
     * {@link org.bff.javampd.playlist.PlaylistChangeListener}.
     *
     * @param event the event id to send
     */
    protected synchronized void fireOutputChangeEvent(OUTPUT_EVENT event) {
        OutputChangeEvent oce = new OutputChangeEvent(this, event);

        for (OutputChangeListener pcl : outputListeners) {
            pcl.outputChanged(oce);
        }
    }
}
