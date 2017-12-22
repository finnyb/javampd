package org.bff.javampd.player;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerPropertiesTest {
    private PlayerProperties playerProperties;

    @Before
    public void setUp() throws Exception {
        playerProperties = new PlayerProperties();
    }

    @Test
    public void getXFade() throws Exception {
        assertEquals("crossfade", playerProperties.getXFade());
    }

    @Test
    public void getCurrentSong() throws Exception {
        assertEquals("currentsong", playerProperties.getCurrentSong());
    }

    @Test
    public void getNext() throws Exception {
        assertEquals("next", playerProperties.getNext());
    }

    @Test
    public void getPause() throws Exception {
        assertEquals("pause", playerProperties.getPause());
    }

    @Test
    public void getPlay() throws Exception {
        assertEquals("play", playerProperties.getPlay());
    }

    @Test
    public void getPlayId() throws Exception {
        assertEquals("playid", playerProperties.getPlayId());
    }

    @Test
    public void getPrevious() throws Exception {
        assertEquals("previous", playerProperties.getPrevious());
    }

    @Test
    public void getRepeat() throws Exception {
        assertEquals("repeat", playerProperties.getRepeat());
    }

    @Test
    public void getRandom() throws Exception {
        assertEquals("random", playerProperties.getRandom());
    }

    @Test
    public void getSeek() throws Exception {
        assertEquals("seek", playerProperties.getSeek());
    }

    @Test
    public void getSeekId() throws Exception {
        assertEquals("seekid", playerProperties.getSeekId());
    }

    @Test
    public void getStop() throws Exception {
        assertEquals("stop", playerProperties.getStop());
    }

    @Test
    public void getSetVolume() throws Exception {
        assertEquals("setvol", playerProperties.getSetVolume());
    }

}