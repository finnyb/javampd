package org.bff.javampd.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.bff.javampd.command.MPDCommandExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MPDServerTest {
  @Mock private MPDCommandExecutor mockCommandExecutor;

  @Mock private ServerProperties mockServerProperties;

  @Captor private ArgumentCaptor<String> commandArgumentCaptor;

  private static final int DEFAULT_PORT = 6600;
  private static final int DEFAULT_TIMEOUT = 0;
  private static final String DEFAULT_SERVER = "localhost";

  @Test
  void testClearError() {
    when(mockServerProperties.getClearError()).thenReturn(new ServerProperties().getClearError());

    TestMPD mpd = new TestMPD();
    mpd.clearError();

    verify(mockCommandExecutor).sendCommand(commandArgumentCaptor.capture());
    assertEquals(mockServerProperties.getClearError(), commandArgumentCaptor.getAllValues().get(0));
  }

  @Test
  void testClose() {
    new TestMPD().close();

    verify(mockCommandExecutor).sendCommand(commandArgumentCaptor.capture());
    assertEquals(mockServerProperties.getClose(), commandArgumentCaptor.getAllValues().get(0));
  }

  @Test
  void testServer() throws UnknownHostException {
    MPD mpd = MPD.builder().server("localhost").build();
    assertEquals(InetAddress.getByName("localhost"), mpd.getAddress());
  }

  @Test
  void testBuilderException() {
    var mpd = MPD.builder().server("bogusServer");
    assertThrows(MPDConnectionException.class, mpd::build);
  }

  @Test
  void testPort() {
    MPD mpd = MPD.builder().port(8080).build();
    assertEquals(8080, mpd.getPort());
  }

  @Test
  void testTimeout() {
    MPD mpd = MPD.builder().timeout(0).build();
    assertEquals(0, mpd.getTimeout());
  }

  @Test
  void testPassword() {
    String password = "thepassword";

    TestMPD mpd = new TestMPD(password);

    verify(mockCommandExecutor).usePassword(commandArgumentCaptor.capture());
    assertNotNull(mpd);
    assertEquals(password, commandArgumentCaptor.getAllValues().get(0));
  }

  @Test
  void testNullPassword() {
    String password = null;
    MPD mpd = MPD.builder().password(password).build();

    verify(mockCommandExecutor, never()).usePassword(password);
    assertNotNull(mpd);
  }

  @Test
  void testBlankPassword() {
    String password = "";
    TestMPD mpd = new TestMPD(password);

    verify(mockCommandExecutor, never()).usePassword(password);
    assertNotNull(mpd);
  }

  @Test
  void testBuild() {
    TestMPD mpd = new TestMPD();
    assertNotNull(mpd);
  }

  @Test
  void testDefaultServer() throws UnknownHostException {
    TestMPD mpd = new TestMPD();
    assertEquals(InetAddress.getByName(DEFAULT_SERVER), mpd.getAddress());
  }

  @Test
  void testDefaultPort() {
    TestMPD mpd = new TestMPD();
    assertEquals(DEFAULT_PORT, mpd.getPort());
  }

  @Test
  void testDefaultTimeout() {
    TestMPD mpd = new TestMPD();
    assertEquals(DEFAULT_TIMEOUT, mpd.getTimeout());
  }

  @Test
  void testIsClosed() {
    TestMPD mpd = new TestMPD();
    assertFalse(mpd.isClosed());
  }

  @Test
  void testIsNotClosed() {
    TestMPD mpd = new TestMPD();
    mpd.close();
    assertTrue(mpd.isClosed());
  }

  @Test
  void testGetVersion() {
    String theVersion = "testVersion";
    when(mockCommandExecutor.getMPDVersion()).thenReturn(theVersion);

    TestMPD mpd = new TestMPD();
    assertEquals(theVersion, mpd.getVersion());
  }

  @Test
  void testIsConnected() {
    when(mockServerProperties.getPing()).thenReturn(new ServerProperties().getPing());
    when(mockCommandExecutor.sendCommand(mockServerProperties.getPing()))
        .thenReturn(new ArrayList<>());

    TestMPD mpd = new TestMPD();
    assertTrue(mpd.isConnected());
  }

  @Test
  void testIsNotConnected() {
    when(mockServerProperties.getPing()).thenReturn(new ServerProperties().getPing());
    when(mockCommandExecutor.sendCommand(mockServerProperties.getPing()))
        .thenThrow(new MPDConnectionException());

    TestMPD mpd = new TestMPD();
    assertFalse(mpd.isConnected());
  }

  @Test
  void testAuthenticate() {
    assertDoesNotThrow(() -> MPD.builder().build());
  }

  @Test
  void testFailedAuthenticate() {
    String password = "password";
    doThrow(new MPDSecurityException("incorrect password"))
        .when(mockCommandExecutor)
        .authenticate();

    assertThrows(MPDConnectionException.class, () -> new TestMPD(password));
  }

  private class TestMPD extends MPD {
    public TestMPD() {
      super("localhost", 6600, 0, "");
    }

    public TestMPD(String password) {
      super("localhost", 6600, 0, password);
    }

    @Override
    void init() {
      commandExecutor = mockCommandExecutor;
      serverProperties = mockServerProperties;
    }
  }
}
