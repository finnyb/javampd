package org.bff.javampd.statistics;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MPDStatsConverterTest {
  private StatsConverter statsConverter;

  @BeforeEach
  void setup() {
    this.statsConverter = new MPDStatsConverter();
  }

  @Test
  void convertEmptyResponse() {
    MPDStatistics stats = this.statsConverter.convertResponseToStats(List.of());
    assertAll(
        () -> assertEquals(0, stats.getAlbums()),
        () -> assertEquals(0, stats.getUptime()),
        () -> assertEquals(0, stats.getArtists()),
        () -> assertEquals(0, stats.getTracks()),
        () -> assertEquals(0, stats.getLastUpdateTime()),
        () -> assertEquals(0, stats.getDatabasePlaytime()),
        () -> assertEquals(0, stats.getPlaytime()));
  }

  @Test
  void convertResponse() {
    MPDStatistics stats =
        this.statsConverter.convertResponseToStats(
            List.of(
                "uptime: 11262",
                "playtime: 19823",
                "artists: 2961",
                "albums: 5671",
                "songs: 84109",
                "db_playtime: 20572745",
                "db_update: 1646880222",
                "OK"));

    assertAll(
        () -> assertEquals(5671, stats.getAlbums()),
        () -> assertEquals(11262, stats.getUptime()),
        () -> assertEquals(2961, stats.getArtists()),
        () -> assertEquals(84109, stats.getTracks()),
        () -> assertEquals(1646880222, stats.getLastUpdateTime()),
        () -> assertEquals(20572745, stats.getDatabasePlaytime()),
        () -> assertEquals(19823, stats.getPlaytime()));
  }
}
