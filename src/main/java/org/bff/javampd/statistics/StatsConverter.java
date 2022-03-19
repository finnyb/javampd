package org.bff.javampd.statistics;

import java.util.List;

@FunctionalInterface
public interface StatsConverter {
  MPDStatistics convertResponseToStats(List<String> list);
}
