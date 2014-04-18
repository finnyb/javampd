package org.bff.javampd.monitor;

import org.bff.javampd.events.PlaylistBasicChangeListener;

public interface PlaylistMonitor extends StatusMonitor {
    void addPlaylistChangeListener(PlaylistBasicChangeListener pcl);

    void removePlaylistStatusChangedListener(PlaylistBasicChangeListener pcl);

    int getSongId();

    void playerStopped();
}
