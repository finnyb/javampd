package org.bff.javampd.server;

import org.bff.javampd.Clock;
import org.bff.javampd.command.MPDCommandExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MPDServerStatusTest {

    @Mock
    private MPDCommandExecutor commandExecutor;

    @Mock
    private ServerProperties properties;

    @Mock
    private Clock clock;

    private MPDServerStatus serverStatus;

    private List<String> statusList;

    @BeforeEach
    void setUp() {
        statusList = new ArrayList<>();
        when(clock.min()).thenReturn(LocalDateTime.MIN);
        serverStatus = new MPDServerStatus(properties, commandExecutor, clock);
    }

    @Test
    void testLookupStatus() {
        assertEquals(Status.VOLUME, Status.lookup("volume:"));
    }

    @Test
    void testLookupUnknownStatus() {
        assertEquals(Status.UNKNOWN, Status.lookup("bogus:"));
    }

    @Test
    void testGetPlaylistVersion() {
        String version = "5";
        statusList.add("playlist: " + version);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(version), serverStatus.getPlaylistVersion());
    }

    @Test
    void testInvalidPlaylistVersion() {
        statusList.add("playlist: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getPlaylistVersion());
    }

    @Test
    void testEmptyPlaylistVersion() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getPlaylistVersion());
    }

    @Test
    void testGetState() {
        String state = "state";
        statusList.add("state: " + state);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(state, serverStatus.getState());
    }

    @Test
    void testGetXFade() {
        String xfade = "5";
        statusList.add("xfade: " + xfade);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(xfade), serverStatus.getXFade());
    }

    @Test
    void testInvalidXFade() {
        statusList.add("xfade: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getXFade());
    }

    @Test
    void testEmptyXFade() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());
        assertEquals(0, serverStatus.getXFade());
    }

    @Test
    void testGetAudio() {
        String audio = "audio";
        statusList.add("audio: " + audio);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(audio, serverStatus.getAudio());
    }

    @Test
    void testIsError() {
        String error = "true";
        statusList.add("error: " + error);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertTrue(serverStatus.isError());
    }

    @Test
    void testGetError() {
        String error = "true";
        statusList.add("error: " + error);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(error, serverStatus.getError());
    }

    @Test
    void testGetElapsedTime() {
        String time = "5:6";
        statusList.add("time: " + time);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(time.split(":")[0]), serverStatus.getElapsedTime());
    }

    @ParameterizedTest
    @ValueSource(strings = {"time: junk", "junk: 0", "time: junk:junk"})
    void testElapsedTimes(String input) {
        statusList.add(input);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @ParameterizedTest
    @ValueSource(strings = {"time: junk:junk", "time: junk", "junk: 0"})
    void testTotalTimeParseException(String input) {
        statusList.add(input);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getTotalTime());
    }

    @Test
    void getTotalTime() {
        String time = "5:6";
        statusList.add("time: " + time);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(time.split(":")[1]), serverStatus.getTotalTime());
    }

    @Test
    void testGetBitrate() {
        String bitrate = "5";
        statusList.add("bitrate: " + bitrate);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(bitrate), serverStatus.getBitrate());
    }

    @Test
    void testInvalidBitrate() {
        statusList.add("bitrate: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getBitrate());
    }

    @Test
    void testEmptyBitrate() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getBitrate());
    }

    @Test
    void testGetVolume() {
        String volume = "5";
        statusList.add("volume: " + volume);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(volume), serverStatus.getVolume());
    }

    @Test
    void testInvalidVolume() {
        statusList.add("volume: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getVolume());
    }

    @Test
    void testEmptyVolume() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getVolume());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1"})
    void testIsRepeat(String repeat) {
        statusList.add("repeat: " + repeat);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.isRepeat(), is("1".equals(repeat)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"anything", ""})
    void testIsDatabaseUpdating(String updating) {
        statusList.add("updating_db: " + updating);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.isDatabaseUpdating(), is(not("".equals(updating))));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1"})
    void testRandom(String random) {
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.isRandom(), is("1".equals(random)));
    }

    @Test
    void testInsideDefaultExpiry() {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        serverStatus.isRandom();
        serverStatus.isRandom();
        Mockito.verify(commandExecutor, times(1)).sendCommand(properties.getStatus());
    }

    @Test
    void testOutsideDefaultExpiry() {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());
        serverStatus.isRandom();
        when(clock.now()).thenReturn(LocalDateTime.now().plusMinutes(5));
        serverStatus.isRandom();
        Mockito.verify(commandExecutor, times(2)).sendCommand(properties.getStatus());
    }

    @Test
    void testSetExpiry() {
        long interval = 1;
        serverStatus.setExpiryInterval(interval);
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());
        serverStatus.isRandom();
        when(clock.now()).thenReturn(LocalDateTime.now().plusMinutes(interval * 2));
        serverStatus.isRandom();
        Mockito.verify(commandExecutor, times(2)).sendCommand(properties.getStatus());
    }

    @Test
    void testForceUpdate() {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());
        serverStatus.isRandom();
        serverStatus.forceUpdate();
        serverStatus.isRandom();
        Mockito.verify(commandExecutor, times(2)).sendCommand(properties.getStatus());
    }

    @Test
    void testGetStatus() {
        String random = "1";
        String volume = "5";
        statusList.add("volume: " + volume);
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());
        serverStatus.isRandom();
        serverStatus.forceUpdate();
        serverStatus.isRandom();

        assertEquals(serverStatus.getStatus().size(), statusList.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1"})
    void testSingle(String single) {
        statusList.add("single: " + single);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.isSingle(), is("1".equals(single)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1"})
    void testConsume(String consume) {
        statusList.add("consume: " + consume);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.isConsume(), is("1".equals(consume)));
    }

    @Test
    void testPlaylistSongNumber() {
        var id = 464;
        statusList.add(String.format("song: %s", id));
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        serverStatus.playlistSongNumber().ifPresentOrElse( s -> assertThat(s, is(id)),
                () -> fail("song was empty"));
    }

    @Test
    void testPlaylistSongNumberEmpty() {
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.playlistSongNumber(), is(Optional.empty()));
    }

    @Test
    void testPlaylistSongId() {
        var id = "464";
        statusList.add(String.format("songid: %s", id));
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        serverStatus.playlistSongId().ifPresentOrElse( s -> assertThat(s, is(id)),
                () -> fail("id was empty"));
    }

    @Test
    void testPlaylistSongIdEmpty() {
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.playlistSongId(), is(Optional.empty()));
    }

    @Test
    void testPlaylistNextSongNumber() {
        var id = 464;
        statusList.add(String.format("nextsong: %s", id));
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        serverStatus.playlistNextSongNumber().ifPresentOrElse( s -> assertThat(s, is(id)),
                () -> fail("number was empty"));
    }

    @Test
    void testPlaylistNextSongNumberEmpty() {
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.playlistNextSongNumber(), is(Optional.empty()));
    }

    @Test
    void testPlaylistNextSongId() {
        var id = "464";
        statusList.add(String.format("nextsongid: %s", id));
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        serverStatus.playlistNextSongId().ifPresentOrElse( s -> assertThat(s, is(id)),
                () -> fail("id was empty"));
    }

    @Test
    void testPlaylistNextSongIdEmpty() {
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.playlistNextSongId(), is(Optional.empty()));
    }

    @Test
    void testDurationCurrentSong() {
        var duration = 235;
        statusList.add(String.format("duration: %s", duration));
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        serverStatus.durationCurrentSong().ifPresentOrElse( s -> assertThat(s, is(duration)),
                () -> fail("duration was empty"));
    }

    @Test
    void testDurationCurrentSongEmpty() {
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.durationCurrentSong(), is(Optional.empty()));
    }

    @Test
    void testElapsedCurrentSong() {
        var duration = 235;
        statusList.add(String.format("elapsed: %s", duration));
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        serverStatus.elapsedCurrentSong().ifPresentOrElse( s -> assertThat(s, is(duration)),
                () -> fail("duration was empty"));
    }

    @Test
    void testElapsedCurrentSongEmpty() {
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.elapsedCurrentSong(), is(Optional.empty()));
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, 0, 5})
    void testMixRampDb(int db) {
        statusList.add(String.format("mixrampdb: %s", db));
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        serverStatus.getMixRampDb().ifPresentOrElse( s -> assertThat(s, is(db)),
                () -> fail("db was empty"));
    }

    @Test
    void testMixRampDbEmpty() {
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.getMixRampDb(), is(Optional.empty()));
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, 0, 5})
    void testMixRampDelay(int delay) {
        statusList.add(String.format("mixrampdelay: %s", delay));
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        serverStatus.getMixRampDelay().ifPresentOrElse( s -> assertThat(s, is(delay)),
                () -> fail("delay was empty"));
    }

    @Test
    void testMixRampDelayEmpty() {
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertThat(serverStatus.getMixRampDelay(), is(Optional.empty()));
    }
}
