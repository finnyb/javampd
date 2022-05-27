package org.bff.javampd.command;

import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bff.javampd.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executes commands to the {@link org.bff.javampd.server.MPD}. You <b>MUST</b> call {@link #setMpd}
 * before making any calls to the server
 *
 * @author bill
 */
@Singleton
@Slf4j
public class MPDCommandExecutor implements CommandExecutor {
  private static final Logger LOGGER = LoggerFactory.getLogger(MPDCommandExecutor.class);

  private MPDSocket mpdSocket;
  private MPD mpd;
  private String password;
  private final ServerProperties serverProperties;

  /** You <b>MUST</b> call {@link #setMpd} before making any calls to the server */
  public MPDCommandExecutor() {
    serverProperties = new ServerProperties();
  }

  @Override
  public synchronized List<String> sendCommand(String command) {
    return sendCommand(new MPDCommand(command));
  }

  @Override
  public synchronized List<String> sendCommand(String command, String... params) {
    return sendCommand(new MPDCommand(command, params));
  }

  @Override
  public synchronized List<String> sendCommand(String command, Integer... params) {
    var intParms = new String[params.length];
    for (var i = 0; i < params.length; ++i) {
      intParms[i] = Integer.toString(params[i]);
    }
    return new ArrayList<>(sendCommand(new MPDCommand(command, intParms)));
  }

  @Override
  public synchronized List<String> sendCommand(MPDCommand command) {
    try {
      checkSocket();
      log.debug("Sending command: {}", command);
      return new ArrayList<>(mpdSocket.sendCommand(command));
    } catch (MPDSecurityException se) {
      LOGGER.warn(
          "Connection exception while sending command {}, will retry", command.getCommand(), se);
      authenticate();
      return new ArrayList<>(mpdSocket.sendCommand(command));
    }
  }

  @Override
  public synchronized void sendCommands(List<MPDCommand> commandList) {
    try {
      checkSocket();
      mpdSocket.sendCommands(commandList);
    } catch (MPDSecurityException se) {
      LOGGER.warn("Connection exception while sending commands, will retry", se);
      authenticate();
      mpdSocket.sendCommands(commandList);
    }
  }

  @Override
  public String getMPDVersion() {
    checkSocket();
    return mpdSocket.getVersion();
  }

  @Override
  public void setMpd(MPD mpd) {
    this.mpd = mpd;
  }

  @Override
  public void authenticate() {
    if (password != null) {
      try {
        sendCommand(new MPDCommand(serverProperties.getPassword(), password));
      } catch (Exception e) {
        if (e.getMessage() != null && e.getMessage().contains("incorrect password")) {
          throw new MPDSecurityException("Incorrect password");
        }

        throw new MPDConnectionException("Could not authenticate", e);
      }
    }
  }

  @Override
  public void usePassword(String password) {
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Password cannot be null or empty");
    }

    this.password = password;
  }

  @Override
  public void close() {
    this.mpdSocket.close();
  }

  protected MPDSocket createSocket() {
    return new MPDSocket(mpd.getAddress(), mpd.getPort(), mpd.getTimeout());
  }

  private void checkSocket() {
    if (mpd == null) {
      throw new MPDConnectionException("Socket could not be established.  Was mpd set?");
    }

    if (mpdSocket == null) {
      mpdSocket = createSocket();
    }
  }
}
