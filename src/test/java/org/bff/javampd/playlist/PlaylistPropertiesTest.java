package org.bff.javampd.playlist;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlaylistPropertiesTest {
    private PlaylistProperties playlistProperties;

    @Before
    public void setUp() throws Exception {
        playlistProperties = new PlaylistProperties();
    }

    @Test
    public void getAdd() throws Exception {
        assertEquals("add", playlistProperties.getAdd());
    }

    @Test
    public void getClear() throws Exception {
        assertEquals("clear", playlistProperties.getClear());
    }

    @Test
    public void getCurrentSong() throws Exception {
        assertEquals("currentsong", playlistProperties.getCurrentSong());
    }

    @Test
    public void getDelete() throws Exception {
        assertEquals("rm", playlistProperties.getDelete());
    }

    @Test
    public void getChanges() throws Exception {
        assertEquals("plchanges", playlistProperties.getChanges());
    }

    @Test
    public void getId() throws Exception {
        assertEquals("playlistid", playlistProperties.getId());
    }

    @Test
    public void getInfo() throws Exception {
        assertEquals("playlistinfo", playlistProperties.getInfo());
    }

    @Test
    public void getLoad() throws Exception {
        assertEquals("load", playlistProperties.getLoad());
    }

    @Test
    public void getMove() throws Exception {
        assertEquals("move", playlistProperties.getMove());
    }

    @Test
    public void getMoveId() throws Exception {
        assertEquals("moveid", playlistProperties.getMoveId());
    }

    @Test
    public void getRemove() throws Exception {
        assertEquals("delete", playlistProperties.getRemove());
    }

    @Test
    public void getRemoveId() throws Exception {
        assertEquals("deleteid", playlistProperties.getRemoveId());
    }

    @Test
    public void getSave() throws Exception {
        assertEquals("save", playlistProperties.getSave());
    }

    @Test
    public void getShuffle() throws Exception {
        assertEquals("shuffle", playlistProperties.getShuffle());
    }

    @Test
    public void getSwap() throws Exception {
        assertEquals("swap", playlistProperties.getSwap());
    }

    @Test
    public void getSwapId() throws Exception {
        assertEquals("swapid", playlistProperties.getSwapId());
    }

}