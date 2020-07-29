package org.bff.javampd.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerPropertiesTest {
    private PlayerProperties playerProperties;

    @BeforeEach
    public void setUp() {
        playerProperties = new PlayerProperties();
    }

    @Test
    public void getXFade() {
        assertEquals("crossfade", playerProperties.getXFade());
    }

    @Test
    public void getCurrentSong() {
        assertEquals("currentsong", playerProperties.getCurrentSong());
    }

    @Test
    public void getNext() {
        assertEquals("next", playerProperties.getNext());
    }

    @Test
    public void getPause() {
        assertEquals("pause", playerProperties.getPause());
    }

    @Test
    public void getPlay() {
        assertEquals("play", playerProperties.getPlay());
    }

    @Test
    public void getPlayId() {
        assertEquals("playid", playerProperties.getPlayId());
    }

    @Test
    public void getPrevious() {
        assertEquals("previous", playerProperties.getPrevious());
    }

    @Test
    public void getRepeat() {
        assertEquals("repeat", playerProperties.getRepeat());
    }

    @Test
    public void getRandom() {
        assertEquals("random", playerProperties.getRandom());
    }

    @Test
    public void getSeek() {
        assertEquals("seek", playerProperties.getSeek());
    }

    @Test
    public void getSeekId() {
        assertEquals("seekid", playerProperties.getSeekId());
    }

    @Test
    public void getStop() {
        assertEquals("stop", playerProperties.getStop());
    }

    @Test
    public void getSetVolume() {
        assertEquals("setvol", playerProperties.getSetVolume());
    }

}