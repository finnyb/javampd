package org.bff.javampd.admin;

import static org.bff.javampd.output.OutputChangeEvent.OUTPUT_EVENT;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.output.OutputChangeEvent;
import org.bff.javampd.output.OutputChangeListener;

/**
 * MPDAdmin represents a administrative controller to a MPD server. To obtain an instance of the
 * class you must use the <code>getMPDAdmin</code> method from the {@link
 * org.bff.javampd.server.MPD} connection class. This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill
 */
@Slf4j
public class MPDAdmin implements Admin {

  private final List<MPDChangeListener> listeners = new ArrayList<>();
  private final List<OutputChangeListener> outputListeners = new ArrayList<>();

  protected static final String OUTPUT_PREFIX_ID = "outputid:";
  protected static final String OUTPUT_PREFIX_NAME = "outputname:";
  protected static final String OUTPUT_PREFIX_ENABLED = "outputenabled:";
  protected static final String UPDATE_PREFIX = "updating_db:";

  private final AdminProperties adminProperties;
  private final CommandExecutor commandExecutor;

  @Inject
  public MPDAdmin(AdminProperties adminProperties, CommandExecutor commandExecutor) {
    this.adminProperties = adminProperties;
    this.commandExecutor = commandExecutor;
  }

  @Override
  public Collection<MPDOutput> getOutputs() {
    return new ArrayList<>(parseOutputs(commandExecutor.sendCommand(adminProperties.getOutputs())));
  }

  @Override
  public boolean disableOutput(MPDOutput output) {
    fireOutputChangeEvent(OUTPUT_EVENT.OUTPUT_CHANGED);
    return commandExecutor
        .sendCommand(adminProperties.getOutputDisable(), output.getId())
        .isEmpty();
  }

  @Override
  public boolean enableOutput(MPDOutput output) {
    fireOutputChangeEvent(OUTPUT_EVENT.OUTPUT_CHANGED);
    return commandExecutor.sendCommand(adminProperties.getOutputEnable(), output.getId()).isEmpty();
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
   * Sends the appropriate {@link MPDChangeEvent} to all registered {@link MPDChangeListener}s.
   *
   * @param event the {@link MPDChangeEvent.Event} to send
   */
  protected synchronized void fireMPDChangeEvent(MPDChangeEvent.Event event) {
    var mce = new MPDChangeEvent(this, event);

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
  public int updateDatabase() {
    var response = commandExecutor.sendCommand(adminProperties.getRefresh());
    fireMPDChangeEvent(MPDChangeEvent.Event.REFRESHED);

    return parseUpdate(response);
  }

  @Override
  public int updateDatabase(String path) {
    var response = commandExecutor.sendCommand(adminProperties.getRefresh(), path);
    fireMPDChangeEvent(MPDChangeEvent.Event.REFRESHED);

    return parseUpdate(response);
  }

  @Override
  public int rescan() {
    var response = commandExecutor.sendCommand(adminProperties.getRescan());
    fireMPDChangeEvent(MPDChangeEvent.Event.REFRESHED);

    return parseUpdate(response);
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
    var oce = new OutputChangeEvent(this, event);

    for (OutputChangeListener pcl : outputListeners) {
      pcl.outputChanged(oce);
    }
  }

  private int parseUpdate(List<String> response) {
    var iterator = response.iterator();
    String line = null;

    while (iterator.hasNext()) {
      if (line == null || (!line.startsWith(UPDATE_PREFIX))) {
        line = iterator.next();
      }

      if (line.startsWith(UPDATE_PREFIX)) {
        try {
          return Integer.parseInt(line.replace(UPDATE_PREFIX, "").trim());
        } catch (NumberFormatException e) {
          log.error("Unable to parse jod id from update", e);
        }
      }
    }

    log.warn("No update jod id returned");
    return -1;
  }

  private static Collection<MPDOutput> parseOutputs(Collection<String> response) {
    List<MPDOutput> outputs = new ArrayList<>();
    var iterator = response.iterator();
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
    var output =
        MPDOutput.builder(
                Integer.parseInt(startingLine.substring(OUTPUT_PREFIX_ID.length()).trim()))
            .build();

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
}
