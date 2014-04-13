package org.bff.javampd.monitor;

import com.google.inject.Inject;
import org.bff.javampd.Status;
import org.bff.javampd.events.PlaylistBasicChangeEvent;
import org.bff.javampd.events.PlaylistBasicChangeListener;
import org.bff.javampd.exception.MPDException;

import java.util.ArrayList;
import java.util.List;


public class MPDPlaylistMonitor implements PlaylistMonitor {
    private List<PlaylistBasicChangeListener> playlistListeners;

    private int newPlaylistVersion;
    private int oldPlaylistVersion;
    private int newPlaylistLength;
    private int oldPlaylistLength;
    private int oldSong;
    private int newSong;
    private int oldSongId;
    private int newSongId;

    @Inject
    private PlayerMonitor playerMonitor;

    public MPDPlaylistMonitor() {
        this.playlistListeners = new ArrayList<>();
    }

    @Override
    public synchronized void addPlaylistChangeListener(PlaylistBasicChangeListener pcl) {
        playlistListeners.add(pcl);
    }

    @Override
    public synchronized void removePlaylistStatusChangedListener(PlaylistBasicChangeListener pcl) {
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
     * Sends the appropriate {@link org.bff.javampd.events.PlaylistChangeEvent} to all registered
     * {@link org.bff.javampd.events.PlaylistChangeListener}.
     *
     * @param event the {@link org.bff.javampd.events.PlaylistBasicChangeEvent.Event}
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
        }
    }

    @Override
    public int getDelay() {
        return 2;
    }

    @Override
    public void checkStatus() throws MPDException {
        if (oldPlaylistVersion != newPlaylistVersion) {
            firePlaylistChangeEvent(PlaylistBasicChangeEvent.Event.PLAYLIST_CHANGED);
            oldPlaylistVersion = newPlaylistVersion;
        }

        if (oldPlaylistLength != newPlaylistLength) {
            if (oldPlaylistLength < newPlaylistLength) {
                firePlaylistChangeEvent(PlaylistBasicChangeEvent.Event.SONG_ADDED);
            } else if (oldPlaylistLength > newPlaylistLength) {
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
