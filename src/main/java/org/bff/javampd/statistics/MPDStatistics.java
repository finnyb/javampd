package org.bff.javampd.statistics;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MPDStatistics {
  private long playtime;
  private long uptime;
  private int artists;
  private int albums;
  private int tracks;
  private long lastUpdateTime;
  private long databasePlaytime;
}
