package org.bff.javampd.statistics;

import com.google.inject.Inject;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.server.ServerProperties;

/**
 * @author bill
 */
public class MPDServerStatistics implements ServerStatistics {

  private final ServerProperties serverProperties;
  private final CommandExecutor commandExecutor;
  private final StatsConverter converter;

  @Inject
  public MPDServerStatistics(
      ServerProperties serverProperties,
      CommandExecutor commandExecutor,
      StatsConverter converter) {
    this.serverProperties = serverProperties;
    this.commandExecutor = commandExecutor;
    this.converter = converter;
  }

  @Override
  public MPDStatistics getStatistics() {
    return converter.convertResponseToStats(
        commandExecutor.sendCommand(serverProperties.getStats()));
  }
}
