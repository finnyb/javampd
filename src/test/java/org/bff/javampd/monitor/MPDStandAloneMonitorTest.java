package org.bff.javampd.monitor;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.bff.javampd.output.OutputChangeListener;
import org.bff.javampd.player.*;
import org.bff.javampd.playlist.PlaylistBasicChangeListener;
import org.bff.javampd.server.ConnectionChangeListener;
import org.bff.javampd.server.ErrorListener;
import org.bff.javampd.server.ServerStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MPDStandAloneMonitorTest {
  @Mock
  private OutputMonitor outputMonitor;

  @Mock
  private ErrorMonitor errorMonitor;

  @Mock
  private ConnectionMonitor connectionMonitor;

  @Mock
  private PlayerMonitor playerMonitor;

  @Mock
  private TrackMonitor trackMonitor;

  @Mock
  private PlaylistMonitor playlistMonitor;

  @Mock
  private ServerStatus serverStatus;

  @InjectMocks
  private MPDStandAloneMonitor standAloneMonitor;

  @BeforeEach
  public void setup() {
    Awaitility.setDefaultPollInterval(5, TimeUnit.MILLISECONDS);
  }

  @AfterEach
  public void tearDown() {
    standAloneMonitor.stop();
  }

  @Test
  public void testAddTrackPositionChangeListener() {
    TrackPositionChangeListener tpl = event -> {};
    standAloneMonitor.addTrackPositionChangeListener(tpl);
    verify(trackMonitor, times(1)).addTrackPositionChangeListener(tpl);
  }

  @Test
  public void testRemoveTrackPositionChangeListener() {
    TrackPositionChangeListener tpl = event -> {};
    standAloneMonitor.removeTrackPositionChangeListener(tpl);
    verify(trackMonitor, times(1)).removeTrackPositionChangeListener(tpl);
  }

  @Test
  public void testAddConnectionChangeListener() {
    ConnectionChangeListener ccl = event -> {};
    standAloneMonitor.addConnectionChangeListener(ccl);
    verify(connectionMonitor, times(1)).addConnectionChangeListener(ccl);
  }

  @Test
  public void testRemoveConnectionChangeListener() {
    ConnectionChangeListener ccl = event -> {};
    standAloneMonitor.removeConnectionChangeListener(ccl);
    verify(connectionMonitor, times(1)).removeConnectionChangeListener(ccl);
  }

  @Test
  public void testAddPlayerChangeListener() {
    PlayerBasicChangeListener pbcl = event -> {};
    standAloneMonitor.addPlayerChangeListener(pbcl);
    verify(playerMonitor, times(1)).addPlayerChangeListener(pbcl);
  }

  @Test
  public void testRemovePlayerChangeListener() {
    PlayerBasicChangeListener pbcl = event -> {};
    standAloneMonitor.removePlayerChangeListener(pbcl);
    verify(playerMonitor, times(1)).removePlayerChangeListener(pbcl);
  }

  @Test
  public void testAddVolumeChangeListener() {
    VolumeChangeListener vcl = event -> {};
    standAloneMonitor.addVolumeChangeListener(vcl);
    verify(playerMonitor, times(1)).addVolumeChangeListener(vcl);
  }

  @Test
  public void testRemoveVolumeChangeListener() {
    VolumeChangeListener vcl = event -> {};
    standAloneMonitor.removeVolumeChangeListener(vcl);
    verify(playerMonitor, times(1)).removeVolumeChangeListener(vcl);
  }

  @Test
  public void testAddBitrateChangeListener() {
    BitrateChangeListener bcl = event -> {};
    standAloneMonitor.addBitrateChangeListener(bcl);
    verify(playerMonitor, times(1)).addBitrateChangeListener(bcl);
  }

  @Test
  public void testRemoveBitrateChangeListener() {
    BitrateChangeListener bcl = event -> {};
    standAloneMonitor.removeBitrateChangeListener(bcl);
    verify(playerMonitor, times(1)).removeBitrateChangeListener(bcl);
  }

  @Test
  public void testAddOutputChangeListener() {
    OutputChangeListener ocl = event -> {};
    standAloneMonitor.addOutputChangeListener(ocl);
    verify(outputMonitor, times(1)).addOutputChangeListener(ocl);
  }

  @Test
  public void testRemoveOutputChangeListener() {
    OutputChangeListener ocl = event -> {};
    standAloneMonitor.removeOutputChangeListener(ocl);
    verify(outputMonitor, times(1)).removeOutputChangeListener(ocl);
  }

  @Test
  public void testAddPlaylistChangeListener() {
    PlaylistBasicChangeListener pbcl = event -> {};
    standAloneMonitor.addPlaylistChangeListener(pbcl);
    verify(playlistMonitor, times(1)).addPlaylistChangeListener(pbcl);
  }

  @Test
  public void testRemovePlaylistStatusChangeListener() {
    PlaylistBasicChangeListener pbcl = event -> {};
    standAloneMonitor.removePlaylistChangeListener(pbcl);
    verify(playlistMonitor, times(1)).removePlaylistChangeListener(pbcl);
  }

  @Test
  public void testAddErrorListener() {
    ErrorListener el = event -> {};
    standAloneMonitor.addErrorListener(el);
    verify(errorMonitor, times(1)).addErrorListener(el);
  }

  @Test
  public void testRemoveErrorListener() {
    ErrorListener el = event -> {};
    standAloneMonitor.removeErrorListener(el);
    verify(errorMonitor, times(1)).removeErrorListener(el);
  }

  @Test
  public void testStart() {
    standAloneMonitor.start();
    assertFalse(standAloneMonitor.isDone());
  }

  @Test
  public void testLoaded() {
    List<String> status = new ArrayList<>();
    status.add("volume: 2");

    when(serverStatus.getStatus()).thenReturn(status);

    standAloneMonitor.start();
    await().until(() -> standAloneMonitor.isLoaded());
  }

  @Test
  public void testStop() {
    standAloneMonitor.start();
    await().until(() -> !standAloneMonitor.isDone());
    standAloneMonitor.stop();
    await().until(() -> standAloneMonitor.isDone());
  }

  @Test
  public void testPlayerBasicChangeStopped() {
    PlayerBasicChangeEvent event = new PlayerBasicChangeEvent(
      this,
      PlayerBasicChangeEvent.Status.PLAYER_STOPPED
    );
    standAloneMonitor.playerBasicChange(event);
    verify(trackMonitor, times(1)).resetElapsedTime();
    verify(playlistMonitor, times(1)).playerStopped();
  }

  @Test
  public void testPlayerBasicChangeNotStopped() {
    PlayerBasicChangeEvent event = new PlayerBasicChangeEvent(
      this,
      PlayerBasicChangeEvent.Status.PLAYER_STARTED
    );
    standAloneMonitor.playerBasicChange(event);
    verify(trackMonitor, never()).resetElapsedTime();
    verify(playlistMonitor, never()).playerStopped();
  }
}
