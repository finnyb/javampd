package org.bff.javampd.command;

import org.bff.javampd.BaseTest;
import org.bff.javampd.TestProperties;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.playlist.PlaylistProperties;
import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.AfterEach;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MPDCommandExecutorTestIT extends BaseTest {
    private MPDCommandExecutor commandExecutor;
    private PlaylistProperties playlistProperties;

    @BeforeEach
    public void setUp() throws Exception {
        this.playlistProperties = new PlaylistProperties();
        this.commandExecutor = new MPDCommandExecutor();
        this.commandExecutor.setMpd(getMpd());
        this.commandExecutor.usePassword(TestProperties.getInstance().getPassword());
        this.commandExecutor.authenticate();
    }

    @AfterEach
    public void tearDown() throws Exception {
        this.commandExecutor.close();
    }

    @Test
    public void sendCommands() throws Exception {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        commandExecutor.sendCommands(songs
                .stream()
                .map(song -> new MPDCommand(playlistProperties.getAdd(), song.getFile()))
                .collect(Collectors.toList()));
    }

}