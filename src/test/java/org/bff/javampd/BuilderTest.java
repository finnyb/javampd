package org.bff.javampd;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.bff.javampd.command.MPDCommandExecutor;
import org.bff.javampd.server.MPD;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.server.ServerProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BuilderTest {
  @Mock
  private MPDCommandExecutor mpdCommandExecutor;

  @Mock
  private ServerProperties serverProperties;

  @InjectMocks
  private MPD.Builder mpdBuilder;

  @Captor
  private ArgumentCaptor<String> commandArgumentCaptor;

  private static final int DEFAULT_PORT = 6600;
  private static final int DEFAULT_TIMEOUT = 0;
  private static final String DEFAULT_SERVER = "localhost";

  @Test
  public void testServer() throws UnknownHostException {
    MPD mpd = mpdBuilder.server("localhost").build();
    assertEquals(InetAddress.getByName("localhost"), mpd.getAddress());
  }

  @Test
  public void testBuilderException() {
    assertThrows(
      MPDConnectionException.class,
      () -> new MPD.Builder().server("bogusServer").build()
    );
  }

  @Test
  public void testPort() {
    MPD mpd = mpdBuilder.port(8080).build();
    assertEquals(mpd.getPort(), 8080);
  }

  @Test
  public void testTimeout() {
    MPD mpd = mpdBuilder.timeout(0).build();
    assertEquals(mpd.getTimeout(), 0);
  }

  @Test
  public void testPassword() {
    String password = "thepassword";

    MPD mpd = mpdBuilder.password(password).build();

    verify(mpdCommandExecutor).usePassword(commandArgumentCaptor.capture());
    assertNotNull(mpd);
    assertEquals(password, commandArgumentCaptor.getAllValues().get(0));
  }

  @Test
  public void testNullPassword() {
    String password = null;
    MPD mpd = mpdBuilder.password(password).build();

    verify(mpdCommandExecutor, never()).usePassword(password);
    assertNotNull(mpd);
  }

  @Test
  public void testBlankPassword() {
    String password = "";
    MPD mpd = mpdBuilder.password(password).build();

    verify(mpdCommandExecutor, never()).usePassword(password);
    assertNotNull(mpd);
  }

  @Test
  public void testBuild() {
    MPD mpd = mpdBuilder.build();
    assertNotNull(mpd);
  }

  @Test
  public void testDefaultServer() throws UnknownHostException {
    MPD mpd = mpdBuilder.build();
    assertEquals(InetAddress.getByName(DEFAULT_SERVER), mpd.getAddress());
  }

  @Test
  public void testDefaultPort() {
    MPD mpd = mpdBuilder.build();
    assertEquals(mpd.getPort(), DEFAULT_PORT);
  }

  @Test
  public void testDefaultTimeout() {
    MPD mpd = mpdBuilder.build();
    assertEquals(mpd.getTimeout(), DEFAULT_TIMEOUT);
  }
}
