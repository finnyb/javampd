/*
 * MPDStandAloneMonitor.java
 *
 * Created on October 18, 2005, 10:17 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.javampd.monitor;

import org.bff.javampd.MPD;
import org.bff.javampd.MPDOutput;
import org.bff.javampd.events.*;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDResponseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MPDStandAloneMonitor monitors a MPD connection by querying the status and
 * statistics of the MPD server at given delay intervals.  As statistics change
 * appropriate events are fired indicating these changes.  If more detailed
 * events are desired attach listeners to the different controllers of a
 * connection or use the {@link MPDEventRelayer} class.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class MPDStandAloneMonitor
        extends MPDEventMonitor
        implements Runnable {

    private final MPD mpd;
    private final int delay;
    private int newVolume;
    private int oldVolume;
    private int newPlaylistVersion;
    private int oldPlaylistVersion;
    private int newPlaylistLength;
    private int oldPlaylistLength;
    private int oldSong;
    private int newSong;
    private int oldSongId;
    private int newSongId;
    private int oldBitrate;
    private int newBitrate;
    private long elapsedTime;
    private String state;
    private String error;
    private boolean stopped;
    private HashMap<Integer, MPDOutput> outputMap;

    /**
     * The status of the player.
     */
    public enum PlayerStatus {

        /**
         * player stopped status
         */
        STATUS_STOPPED,
        /**
         * player playing status
         */
        STATUS_PLAYING,
        /**
         * player paused status
         */
        STATUS_PAUSED,
    }

    private PlayerStatus status = PlayerStatus.STATUS_STOPPED;
    private static final int DEFAULT_DELAY = 1000;
    private static final String RESPONSE_PLAY = "play";
    private static final String RESPONSE_STOP = "stop";
    private static final String RESPONSE_PAUSE = "pause";
    private List<PlayerBasicChangeListener> playerListeners;
    private List<PlaylistBasicChangeListener> playlistListeners;
    private List<VolumeChangeListener> volListeners;
    private List<MPDErrorListener> errorListeners;
    private List<OutputChangeListener> outputListeners;

    /**
     * Enumeration of the available information from the MPD
     * server status.
     */
    private enum StatusList {

        /**
         * The current volume (0-100)
         */
        VOLUME("volume:"),
        /**
         * is the song repeating (0 or 1)
         */
        REPEAT("repeat:"),
        /**
         * the playlist version number (31-bit unsigned integer)
         */
        PLAYLIST("playlist:"),
        /**
         * the length of the playlist
         */
        PLAYLISTLENGTH("playlistlength:"),
        /**
         * the current state (play, stop, or pause)
         */
        STATE("state:"),
        /**
         * playlist song number of the current song stopped on or playing
         */
        CURRENTSONG("song:"),
        /**
         * playlist song id of the current song stopped on or playing
         */
        CURRENTSONGID("songid:"),
        /**
         * the time of the current playing/paused song
         */
        TIME("time:"),
        /**
         * instantaneous bitrate in kbps
         */
        BITRATE("bitrate:"),
        /**
         * crossfade in seconds
         */
        XFADE("xfade:"),
        /**
         * the cuurent samplerate, bits, and channels
         */
        AUDIO("audio:"),
        /**
         * job id
         */
        UPDATINGSDB("updatings_db:"), //<int job id>
        /**
         * if there is an error, returns message here
         */
        ERROR("error:");
        /**
         * the prefix associated with the status
         */
        private String prefix;

        /**
         * Enum constructor
         *
         * @param prefix the prefix of the line in the response
         */
        StatusList(String prefix) {
            this.prefix = prefix;
        }

        /**
         * Returns the <CODE>String</CODE> prefix of the response.
         *
         * @return the prefix of the response
         */
        public String getStatusPrefix() {
            return (prefix);
        }
    }

    /**
     * Creates a new instance of MPDStandAloneMonitor using the default delay
     * of 1 second.
     *
     * @param mpd a connection to a MPD server
     */
    public MPDStandAloneMonitor(MPD mpd) {
        this(mpd, DEFAULT_DELAY);
    }

    /**
     * Creates a new instance of MPDStandAloneMonitor using the given delay interval
     * for queries.
     *
     * @param mpd   a connection to a MPD server
     * @param delay the delay interval
     */
    public MPDStandAloneMonitor(MPD mpd, int delay) {
        super(mpd);
        this.mpd = mpd;
        this.delay = delay;
        this.playerListeners = new ArrayList<PlayerBasicChangeListener>();
        this.playlistListeners = new ArrayList<PlaylistBasicChangeListener>();
        this.volListeners = new ArrayList<VolumeChangeListener>();
        this.errorListeners = new ArrayList<MPDErrorListener>();
        this.outputListeners = new ArrayList<OutputChangeListener>();
        this.outputMap = new HashMap<Integer, MPDOutput>();
        try {
            //initial load so no events fired
            List<String> response = new ArrayList<String>(mpd.getStatus());
            processResponse(response);
            loadOutputs(mpd.getMPDAdmin().getOutputs());
        } catch (MPDException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Adds a {@link PlayerBasicChangeListener} to this object to receive
     * {@link PlayerChangeEvent}s.
     *
     * @param pcl the PlayerBasicChangeListener to add
     */
    public synchronized void addPlayerChangeListener(PlayerBasicChangeListener pcl) {
        playerListeners.add(pcl);
    }

    /**
     * Removes a {@link PlayerBasicChangeListener} from this object.
     *
     * @param pcl the PlayerBasicChangeListener to remove
     */
    public synchronized void removePlayerChangeListener(PlayerBasicChangeListener pcl) {
        playerListeners.remove(pcl);
    }

    /**
     * Sends the appropriate {@link PlayerBasicChangeEvent} to all registered
     * {@link PlayerBasicChangeListener}s.
     *
     * @param id the event id to send
     */
    protected synchronized void firePlayerChangeEvent(int id) {
        PlayerBasicChangeEvent pce = new PlayerBasicChangeEvent(this, id);

        for (PlayerBasicChangeListener pcl : playerListeners) {
            pcl.playerBasicChange(pce);
        }
    }

    /**
     * Adds a {@link VolumeChangeListener} to this object to receive
     * {@link VolumeChangeEvent}s.
     *
     * @param vcl the VolumeChangeListener to add
     */
    public synchronized void addVolumeChangeListener(VolumeChangeListener vcl) {
        volListeners.add(vcl);
    }

    /**
     * Removes a {@link VolumeChangeListener} from this object.
     *
     * @param vcl the VolumeChangeListener to remove
     */
    public synchronized void removeVolumeChangedListener(VolumeChangeListener vcl) {
        volListeners.remove(vcl);
    }

    /**
     * Sends the appropriate {@link VolumeChangeEvent} to all registered
     * {@link VolumeChangeListener}.
     *
     * @param volume the new volume
     */
    protected synchronized void fireVolumeChangeEvent(int volume) {
        VolumeChangeEvent vce = new VolumeChangeEvent(this, volume);

        for (VolumeChangeListener vcl : volListeners) {
            vcl.volumeChanged(vce);
        }
    }

    /**
     * Adds a {@link OutputChangeListener} to this object to receive
     * {@link OutputChangeEvent}s.
     *
     * @param vcl the OutputChangeListener to add
     */
    public synchronized void addOutputChangeListener(OutputChangeListener vcl) {
        outputListeners.add(vcl);
    }

    /**
     * Removes a {@link OutputChangeListener} from this object.
     *
     * @param vcl the OutputChangeListener to remove
     */
    public synchronized void removeOutputChangedListener(OutputChangeListener vcl) {
        outputListeners.remove(vcl);
    }

    /**
     * Sends the appropriate {@link OutputChangeEvent} to all registered
     * {@link OutputChangeListener}s.
     *
     * @param event the event id to send
     */
    protected synchronized void fireOutputChangeEvent(OutputChangeEvent event) {
        for (OutputChangeListener ocl : outputListeners) {
            ocl.outputChanged(event);
        }
    }

    /**
     * Adds a {@link PlaylistBasicChangeListener} to this object to receive
     * {@link PlaylistChangeEvent}s.
     *
     * @param pcl the PlaylistChangeListener to add
     */
    public synchronized void addPlaylistChangeListener(PlaylistBasicChangeListener pcl) {
        playlistListeners.add(pcl);
    }

    /**
     * Removes a {@link PlaylistBasicChangeListener} from this object.
     *
     * @param pcl the PlaylistBasicChangeListener to remove
     */
    public synchronized void removePlaylistStatusChangedListener(PlaylistBasicChangeListener pcl) {
        playlistListeners.remove(pcl);
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     *
     * @param id the event id to send
     */
    protected synchronized void firePlaylistChangeEvent(int id) {
        PlaylistBasicChangeEvent pce = new PlaylistBasicChangeEvent(this, id);

        for (PlaylistBasicChangeListener pcl : playlistListeners) {
            pcl.playlistBasicChange(pce);
        }
    }

    /**
     * Adds a {@link MPDErrorListener} to this object to receive
     * {@link MPDErrorEvent}s.
     *
     * @param el the MPDErrorListener to add
     */
    public synchronized void addMPDErrorListener(MPDErrorListener el) {
        errorListeners.add(el);
    }

    /**
     * Removes a {@link MPDErrorListener} from this object.
     *
     * @param el the MPDErrorListener to remove
     */
    public synchronized void removeMPDErrorListener(MPDErrorListener el) {
        errorListeners.remove(el);
    }

    /**
     * Sends the appropriate {@link MPDErrorListener} to all registered
     * {@link MPDErrorListener}s.
     *
     * @param msg the event message
     */
    protected void fireMPDErrorEvent(String msg) {
        MPDErrorEvent ee = new MPDErrorEvent(this, msg);

        for (MPDErrorListener el : errorListeners) {
            el.errorEventReceived(ee);
        }
    }

    /**
     * Implements the Runnable run method to monitor the MPD connection.
     */
    @Override
    public void run() {
        List<String> response;
        while (!isStopped()) {

            try {
                try {
                    synchronized (this) {
                        response = new ArrayList<String>(mpd.getStatus());
                        processResponse(response);

                        checkError();
                        checkPlayer();
                        checkPlaylist();
                        checkTrackPosition(elapsedTime);
                        checkVolume();
                        checkBitrate();
                        checkConnection();
                        checkOutputs();
                        this.wait(delay);
                    }
                } catch (InterruptedException ie) {
                    setStopped(true);
                }
            } catch (MPDException mce) {
                if (mce instanceof MPDConnectionException) {
                    fireConnectionChangeEvent(false, mce.getMessage());
                    boolean retry = true;

                    while (retry) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MPDStandAloneMonitor.class.getName()).log(Level.SEVERE, null, ex);
                        }


                        checkConnection();
                        if (isConnectedState()) {
                            retry = false;
                        }
                    }
                }
            }
        }
    }

    /**
     * Starts the monitor by creating and starting a thread using this instance
     * as the Runnable interface.
     */
    public void start() {
        Executors.newSingleThreadExecutor().execute(this);
    }

    /**
     * Stops the thread.
     */
    public void stop() {
        setStopped(true);
    }

    /**
     * Returns true if the monitor is stopped, false if the monitor is still running.
     *
     * @return true if monitor is running, false otherwise false
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Returns the current status of the player.
     *
     * @return the status of the player
     */
    public PlayerStatus getStatus() {
        return status;
    }

    private void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    private void checkError() {
        if (error != null) {
            fireMPDErrorEvent(error);
        }
    }

    private void checkPlayer() {
        PlayerStatus newStatus = PlayerStatus.STATUS_STOPPED;
        if (state.startsWith(RESPONSE_PLAY)) {
            newStatus = PlayerStatus.STATUS_PLAYING;
        } else if (state.startsWith(RESPONSE_PAUSE)) {
            newStatus = PlayerStatus.STATUS_PAUSED;
        } else if (state.startsWith(RESPONSE_STOP)) {
            newStatus = PlayerStatus.STATUS_STOPPED;
        }

        if (status != newStatus) {
            switch (newStatus) {
                case STATUS_PLAYING:
                    switch (status) {
                        case STATUS_PAUSED:
                            firePlayerChangeEvent(PlayerBasicChangeEvent.PLAYER_UNPAUSED);
                            break;
                        case STATUS_STOPPED:
                            firePlayerChangeEvent(PlayerBasicChangeEvent.PLAYER_STARTED);
                            break;
                    }
                    break;
                case STATUS_STOPPED:
                    elapsedTime = 0; //when stopped no time in response reading 0
                    firePlayerChangeEvent(PlayerBasicChangeEvent.PLAYER_STOPPED);
                    if (newSongId == -1) {
                        firePlaylistChangeEvent(PlaylistBasicChangeEvent.PLAYLIST_ENDED);
                    }

                    break;
                case STATUS_PAUSED:
                    switch (status) {
                        case STATUS_PAUSED:
                            firePlayerChangeEvent(PlayerBasicChangeEvent.PLAYER_UNPAUSED);
                            break;
                        case STATUS_PLAYING:
                            firePlayerChangeEvent(PlayerBasicChangeEvent.PLAYER_PAUSED);
                            break;
                    }
            }
            status = newStatus;
        }
    }

    private int checkBitrateCount;

    private void checkBitrate() {
        if (checkBitrateCount == 7) {
            checkBitrateCount = 0;
            if (oldBitrate != newBitrate) {
                firePlayerChangeEvent(PlayerBasicChangeEvent.PLAYER_BITRATE_CHANGE);
                oldBitrate = newBitrate;
            }
        } else {
            ++checkBitrateCount;
        }
    }

    private int checkOutputCount;

    /**
     * Checks the connection status of the MPD.  Fires a {@link ConnectionChangeEvent}
     * if the connection status changes.
     *
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem with the connection
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if response is an error
     */
    protected final void checkOutputs() throws MPDConnectionException, MPDResponseException {
        if (checkOutputCount == 3) {
            checkOutputCount = 0;

            List<MPDOutput> outputs = new ArrayList<MPDOutput>(mpd.getMPDAdmin().getOutputs());
            if (outputs.size() > outputMap.size()) {
                fireOutputChangeEvent(new OutputChangeEvent(this, OutputChangeEvent.OUTPUT_EVENT.OUTPUT_ADDED));
                loadOutputs(outputs);
            } else if (outputs.size() < outputMap.size()) {
                fireOutputChangeEvent(new OutputChangeEvent(this, OutputChangeEvent.OUTPUT_EVENT.OUTPUT_DELETED));
                loadOutputs(outputs);
            } else {
                for (MPDOutput out : outputs) {
                    MPDOutput output = outputMap.get(out.getId());
                    if (output == null) {
                        fireOutputChangeEvent(new OutputChangeEvent(out, OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED));
                        loadOutputs(outputs);
                        return;
                    } else {
                        if (output.isEnabled() != out.isEnabled()) {
                            fireOutputChangeEvent(new OutputChangeEvent(out, OutputChangeEvent.OUTPUT_EVENT.OUTPUT_CHANGED));
                            loadOutputs(outputs);
                            return;
                        }
                    }

                }
            }
        } else {
            ++checkOutputCount;
        }
    }

    private void loadOutputs(Collection<MPDOutput> outputs) {
        outputMap.clear();
        for (MPDOutput output : outputs) {
            outputMap.put(output.getId(), output);
        }
    }

    private int checkPlaylistCount;

    private void checkPlaylist() {
        if (checkPlaylistCount == 2) {

            checkPlaylistCount = 0;
            if (oldPlaylistVersion != newPlaylistVersion) {
                firePlaylistChangeEvent(PlaylistBasicChangeEvent.PLAYLIST_CHANGED);
                oldPlaylistVersion = newPlaylistVersion;
            }

            if (oldPlaylistLength != newPlaylistLength) {
                if (oldPlaylistLength < newPlaylistLength) {
                    firePlaylistChangeEvent(PlaylistBasicChangeEvent.SONG_ADDED);
                } else if (oldPlaylistLength > newPlaylistLength) {
                    firePlaylistChangeEvent(PlaylistBasicChangeEvent.SONG_DELETED);
                }

                oldPlaylistLength = newPlaylistLength;
            }

            if (status == PlayerStatus.STATUS_PLAYING) {
                if (oldSong != newSong) {
                    firePlaylistChangeEvent(PlaylistBasicChangeEvent.SONG_CHANGED);
                    oldSong = newSong;
                } else if (oldSongId != newSongId) {
                    firePlaylistChangeEvent(PlaylistBasicChangeEvent.SONG_CHANGED);
                    oldSongId = newSongId;
                }
            }
        } else {
            ++checkPlaylistCount;
        }
    }

    private int checkVolumeCount;

    private void checkVolume() {
        if (checkVolumeCount == 5) {
            checkVolumeCount = 0;
            if (oldVolume != newVolume) {
                fireVolumeChangeEvent(newVolume);
                oldVolume = newVolume;
            }
        } else {
            ++checkVolumeCount;
        }
    }

    private void processResponse(List<String> response) {
        newSongId = -1;
        newSong = -1;
        error = null;

        for (String line : response) {
            if (line.startsWith(StatusList.VOLUME.getStatusPrefix())) {
                newVolume = Integer.parseInt(line.substring(StatusList.VOLUME.getStatusPrefix().length()).trim());
            }
            if (line.startsWith(StatusList.PLAYLIST.getStatusPrefix())) {
                newPlaylistVersion = Integer.parseInt(line.substring(StatusList.PLAYLIST.getStatusPrefix().length()).trim());
            }
            if (line.startsWith(StatusList.PLAYLISTLENGTH.getStatusPrefix())) {
                newPlaylistLength = Integer.parseInt(line.substring(StatusList.PLAYLISTLENGTH.getStatusPrefix().length()).trim());
            }
            if (line.startsWith(StatusList.STATE.getStatusPrefix())) {
                state = line.substring(StatusList.STATE.getStatusPrefix().length()).trim();
            }
            if (line.startsWith(StatusList.CURRENTSONG.getStatusPrefix())) {
                newSong = Integer.parseInt(line.substring(StatusList.CURRENTSONG.getStatusPrefix().length()).trim());
            }
            if (line.startsWith(StatusList.CURRENTSONGID.getStatusPrefix())) {
                newSongId = Integer.parseInt(line.substring(StatusList.CURRENTSONGID.getStatusPrefix().length()).trim());
            }
            if (line.startsWith(StatusList.TIME.getStatusPrefix())) {
                elapsedTime = Long.parseLong(line.substring(StatusList.TIME.getStatusPrefix().length()).trim().split(":")[0]);
            }
            if (line.startsWith(StatusList.BITRATE.getStatusPrefix())) {
                newBitrate = Integer.parseInt(line.substring(StatusList.BITRATE.getStatusPrefix().length()).trim());
            }
            if (line.startsWith(StatusList.ERROR.getStatusPrefix())) {
                error = line.substring(StatusList.ERROR.getStatusPrefix().length()).trim();
            }
        }
    }
}
