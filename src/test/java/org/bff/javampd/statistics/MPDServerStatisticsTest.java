package org.bff.javampd.statistics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.bff.javampd.command.MPDCommandExecutor;
import org.bff.javampd.server.ServerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MPDServerStatisticsTest {

  @Mock private MPDCommandExecutor commandExecutor;
  @Mock private StatsConverter statsConverter;
  @Captor private ArgumentCaptor<String> captor;

  private ServerStatistics serverStatistics;

  @BeforeEach
  void setUp() {
    serverStatistics =
        new MPDServerStatistics(new ServerProperties(), commandExecutor, statsConverter);
  }

  @Test
  void sendCommand() {
    when(commandExecutor.sendCommand((String) any())).thenReturn(List.of());

    serverStatistics.getStatistics();
    verify(commandExecutor).sendCommand(captor.capture());

    assertEquals("stats", captor.getValue());
  }
}
