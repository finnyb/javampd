package org.bff.javampd.player;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerPropertiesTest {
  private PlayerProperties playerProperties;

  @BeforeEach
  void setUp() {
    playerProperties = new PlayerProperties();
  }

  @Test
  void getXFade() {
    assertEquals("crossfade", playerProperties.getXFade());
  }

  @Test
  void getCurrentSong() {
    assertEquals("currentsong", playerProperties.getCurrentSong());
  }

  @Test
  void getNext() {
    assertEquals("next", playerProperties.getNext());
  }

  @Test
  void getPause() {
    assertEquals("pause", playerProperties.getPause());
  }

  @Test
  void getPlay() {
    assertEquals("play", playerProperties.getPlay());
  }

  @Test
  void getPlayId() {
    assertEquals("playid", playerProperties.getPlayId());
  }

  @Test
  void getPrevious() {
    assertEquals("previous", playerProperties.getPrevious());
  }

  @Test
  void getRepeat() {
    assertEquals("repeat", playerProperties.getRepeat());
  }

  @Test
  void getRandom() {
    assertEquals("random", playerProperties.getRandom());
  }

  @Test
  void getSeek() {
    assertEquals("seek", playerProperties.getSeek());
  }

  @Test
  void getSeekId() {
    assertEquals("seekid", playerProperties.getSeekId());
  }

  @Test
  void getStop() {
    assertEquals("stop", playerProperties.getStop());
  }

  @Test
  void getMixRampDb() {
    assertThat(playerProperties.getMixRampDb(), is(equalTo("mixrampdb")));
  }

  @Test
  void getMixRampDelay() {
    assertThat(playerProperties.getMixRampDelay(), is(equalTo("mixrampdelay")));
  }

  @Test
  void getSetVolume() {
    assertEquals("setvol", playerProperties.getSetVolume());
  }
}
