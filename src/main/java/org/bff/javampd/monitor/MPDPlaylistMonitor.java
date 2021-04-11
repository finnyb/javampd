package org.bff.javampd.monitor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bff.javampd.playlist.PlaylistBasicChangeEvent;
import org.bff.javampd.playlist.PlaylistBasicChangeListener;
import org.bff.javampd.server.Status;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class MPDPlaylistMonitor implements PlaylistMonitor {
    private final List<PlaylistBasicChangeListener> playlistListeners;

    private int newPlaylistVersion;
    private int oldPlaylistVersion;
    private int newPlaylistLength;
    private int oldPlaylistLength;
    private int oldSong;
    private int newSong;
    private int oldSongId;
    private int newSongId;

    private final PlayerMonitor playerMonitor;

    @Inject
    MPDPlaylistMonitor(PlayerMonitor playerMonitor) {
        this.playerMonitor = playerMonitor;
        this.playlistListeners = new ArrayList<>();
    }

    @Override
    public synchronized void addPlaylistChangeListener(PlaylistBasicChangeListener pcl) {
        playlistListeners.add(pcl);
    }

    @Override
    public synchronized void removePlaylistChangeListener(PlaylistBasicChangeListener pcl) {
        playlistListeners.remove(pcl);
    }

    @Override
    public int getSongId() {
        return this.newSongId;
    }

    @Override
    public void playerStopped() {
        if (getSongId() == -1) {
            firePlaylistChangeEvent(PlaylistBasicChangeEvent.Event.PLAYLIST_ENDED);
        }
    }

    /**
     * Sends the appropriate {@link org.bff.javampd.playlist.PlaylistChangeEvent} to all registered
     * {@link org.bff.javampd.playlist.PlaylistChangeListener}.
     *
     * @param event the {@link org.bff.javampd.playlist.PlaylistBasicChangeEvent.Event}
     */
    public synchronized void firePlaylistChangeEvent(PlaylistBasicChangeEvent.Event event) {
        PlaylistBasicChangeEvent pce = new PlaylistBasicChangeEvent(this, event);

        for (PlaylistBasicChangeListener pcl : playlistListeners) {
            pcl.playlistBasicChange(pce);
        }
    }

    @Override
    public void processResponseStatus(String line) {
        Status status = Status.lookupStatus(line);

        switch (status) {
            case PLAYLIST:
                newPlaylistVersion = Integer.parseInt(line.substring(Status.PLAYLIST.getStatusPrefix().length()).trim());
                break;
            case PLAYLISTLENGTH:
                newPlaylistLength = Integer.parseInt(line.substring(Status.PLAYLISTLENGTH.getStatusPrefix().length()).trim());
                break;
            case CURRENTSONG:
                newSong = Integer.parseInt(line.substring(Status.CURRENTSONG.getStatusPrefix().length()).trim());
                break;
            case CURRENTSONGID:
                newSongId = Integer.parseInt(line.substring(Status.CURRENTSONGID.getStatusPrefix().length()).trim());
                break;
            default:
                //Do nothing
                break;
        }
    }

    @Override
    public void reset() {
        newPlaylistVersion = 0;
        oldPlaylistVersion = 0;
        newPlaylistLength = 0;
        oldPlaylistLength = 0;
        oldSong = 0;
        newSong = 0;
        oldSongId = 0;
        newSongId = 0;
    }

    @Override
    public void checkStatus() {
        if (oldPlaylistVersion != newPlaylistVersion) {
            firePlaylistChangeEvent(PlaylistBasicChangeEvent.Event.PLAYLIST_CHANGED);
            oldPlaylistVersion = newPlaylistVersion;
        }

        if (oldPlaylistLength != newPlaylistLength) {
            if (oldPlaylistLength < newPlaylistLength) {
                firePlaylistChangeEvent(PlaylistBasicChangeEvent.Event.SONG_ADDED);
            } else {
                firePlaylistChangeEvent(PlaylistBasicChangeEvent.Event.SONG_DELETED);
            }

            oldPlaylistLength = newPlaylistLength;
        }

        if (playerMonitor.getStatus() == PlayerStatus.STATUS_PLAYING) {
            if (oldSong != newSong) {
                firePlaylistChangeEvent(PlaylistBasicChangeEvent.Event.SONG_CHANGED);
                oldSong = newSong;
            } else if (oldSongId != newSongId) {
                firePlaylistChangeEvent(PlaylistBasicChangeEvent.Event.SONG_CHANGED);
                oldSongId = newSongId;
            }
        }
    }
}
