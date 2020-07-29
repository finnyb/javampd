package org.bff.javampd.playlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaylistPropertiesTest {
    private PlaylistProperties playlistProperties;

    @BeforeEach
    public void setUp() {
        playlistProperties = new PlaylistProperties();
    }

    @Test
    public void getAdd() {
        assertEquals("add", playlistProperties.getAdd());
    }

    @Test
    public void getClear() {
        assertEquals("clear", playlistProperties.getClear());
    }

    @Test
    public void getCurrentSong() {
        assertEquals("currentsong", playlistProperties.getCurrentSong());
    }

    @Test
    public void getDelete() {
        assertEquals("rm", playlistProperties.getDelete());
    }

    @Test
    public void getChanges() {
        assertEquals("plchanges", playlistProperties.getChanges());
    }

    @Test
    public void getId() {
        assertEquals("playlistid", playlistProperties.getId());
    }

    @Test
    public void getInfo() {
        assertEquals("playlistinfo", playlistProperties.getInfo());
    }

    @Test
    public void getLoad() {
        assertEquals("load", playlistProperties.getLoad());
    }

    @Test
    public void getMove() {
        assertEquals("move", playlistProperties.getMove());
    }

    @Test
    public void getMoveId() {
        assertEquals("moveid", playlistProperties.getMoveId());
    }

    @Test
    public void getRemove() {
        assertEquals("delete", playlistProperties.getRemove());
    }

    @Test
    public void getRemoveId() {
        assertEquals("deleteid", playlistProperties.getRemoveId());
    }

    @Test
    public void getSave() {
        assertEquals("save", playlistProperties.getSave());
    }

    @Test
    public void getShuffle() {
        assertEquals("shuffle", playlistProperties.getShuffle());
    }

    @Test
    public void getSwap() {
        assertEquals("swap", playlistProperties.getSwap());
    }

    @Test
    public void getSwapId() {
        assertEquals("swapid", playlistProperties.getSwapId());
    }

}