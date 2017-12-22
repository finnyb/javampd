package org.bff.javampd;

import com.google.inject.AbstractModule;
import org.bff.javampd.monitor.*;

/**
 * Initializes the DI bindings
 *
 * @author bill
 */
public class MPDMonitorModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StandAloneMonitor.class).to(MPDStandAloneMonitor.class);
        bind(OutputMonitor.class).to(MPDOutputMonitor.class);
        bind(TrackMonitor.class).to(MPDTrackMonitor.class);
        bind(ConnectionMonitor.class).to(MPDConnectionMonitor.class);
        bind(VolumeMonitor.class).to(MPDVolumeMonitor.class);
        bind(PlayerMonitor.class).to(MPDPlayerMonitor.class);
        bind(BitrateMonitor.class).to(MPDBitrateMonitor.class);
        bind(PlaylistMonitor.class).to(MPDPlaylistMonitor.class);
        bind(ErrorMonitor.class).to(MPDErrorMonitor.class);
    }
}
