/*
 * MPDPlayer.java
 *
 * Created on September 29, 2005, 3:42 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.javampd;

import org.bff.javampd.events.PlayerChangeEvent;
import org.bff.javampd.events.PlayerChangeListener;
import org.bff.javampd.events.VolumeChangeEvent;
import org.bff.javampd.events.VolumeChangeListener;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDPlayerException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.MPDAudioInfo;
import org.bff.javampd.objects.MPDSong;
import org.bff.javampd.properties.PlayerProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * MPDPlayer represents a player controller to a MPD server.  To obtain
 * an instance of the class you must use the <code>getMPDPlayer</code> method from
 * the {@link MPD} connection class.  This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class MPDPlayer extends CommandExecutor {

    private int oldVolume;
    private List<PlayerChangeListener> listeners = new ArrayList<PlayerChangeListener>();
    private List<VolumeChangeListener> volListeners = new ArrayList<VolumeChangeListener>();
    private PlayerProperties playerProperties;

    /**
     * The status of the player.
     */
    public enum Status {

        STATUS_STOPPED("stop"),
        STATUS_PLAYING("play"),
        STATUS_PAUSED("pause");

        private String prefix;

        Status(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return this.prefix;
        }
    }

    private Status status = Status.STATUS_STOPPED;
    private MPDServerStatus serverStatus;

    /**
     * Creates a new instance of MPDPlayer
     *
     * @param mpd the MPD connection
     */
    MPDPlayer(MPD mpd) {
        super(mpd);
        this.serverStatus = mpd.getMPDServerStatus();
        this.playerProperties = new PlayerProperties();
    }

    /**
     * Returns the current song either playing or queued for playing.
     *
     * @return the current song
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public MPDSong getCurrentSong() throws MPDConnectionException, MPDPlayerException {
        try {
            List<MPDSong> songList =
                    MPDSongConverter.convertResponseToSong(sendMPDCommand(playerProperties.getCurrentSong()));

            if (songList.size() == 0) {
                return null;
            } else {
                return songList.get(0);
            }
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlayerException(e);
        }
    }

    /**
     * Adds a {@link PlayerChangeListener} to this object to receive
     * {@link PlayerChangeEvent}s.
     *
     * @param pcl the PlayerChangeListener to add
     */
    public synchronized void addPlayerChangeListener(PlayerChangeListener pcl) {
        listeners.add(pcl);
    }

    /**
     * Removes a {@link PlayerChangeListener} from this object.
     *
     * @param pcl the PlayerChangeListener to remove
     */
    public synchronized void removePlayerChangedListener(PlayerChangeListener pcl) {
        listeners.remove(pcl);
    }

    /**
     * Sends the appropriate {@link PlayerChangeEvent} to all registered
     * {@link PlayerChangeListener}s.
     *
     * @param event the {@link PlayerChangeEvent.Event} to send
     */
    protected synchronized void firePlayerChangeEvent(PlayerChangeEvent.Event event) {
        PlayerChangeEvent pce = new PlayerChangeEvent(this, event);

        for (PlayerChangeListener pcl : listeners) {
            pcl.playerChanged(pce);
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
     * Starts the player.
     *
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void play() throws MPDConnectionException, MPDPlayerException {
        playId(null);
    }

    /**
     * Starts the player with the specified song.
     *
     * @param song the song to start the player with
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void playId(MPDSong song) throws MPDConnectionException, MPDPlayerException {
        try {
            if (song == null) {
                sendMPDCommand(playerProperties.getPlay());
            } else {
                sendMPDCommand(playerProperties.getPlayId(), song.getId());
            }
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlayerException(e);
        }

        if (status == Status.STATUS_STOPPED || status == Status.STATUS_PAUSED) {
            status = Status.STATUS_PLAYING;
            firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_STARTED);
        } else {
            firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_SONG_SET);
        }
    }

    /**
     * Seeks to the desired location in the current song.  If the location is larger
     * than the length of the song or is less than 0 then the parameter is ignored.
     *
     * @param secs the location to seek to
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void seek(long secs) throws MPDConnectionException, MPDPlayerException {
        seekId(null, secs);
    }

    /**
     * Seeks to the desired location in the specified song.  If the location is larger
     * than the length of the song or is less than 0 then the parameter is ignored.
     *
     * @param song the song to seek in
     * @param secs the location to seek to
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void seekId(MPDSong song, long secs) throws MPDConnectionException, MPDPlayerException {
        List<String> response = null;
        String params[] = new String[2];
        params[2] = Long.toString(secs);
        if (song == null) {
            if (getCurrentSong().getLength() > secs) {
                params[1] = Integer.toString(getCurrentSong().getId());
                try {
                    response = sendMPDCommand(playerProperties.getSeekId(), params);
                } catch (MPDResponseException re) {
                    throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
                }
            }
        } else {
            if (song.getLength() >= secs) {
                params[1] = Integer.toString(song.getId());
                try {
                    response = sendMPDCommand(playerProperties.getSeekId(), params);
                } catch (MPDResponseException re) {
                    throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
                }
            }
        }

        if (response != null) {
            firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_SEEKING);
        }
    }

    /**
     * Stops the player.
     *
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void stop() throws MPDConnectionException, MPDPlayerException {
        try {
            sendMPDCommand(playerProperties.getStop());
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        status = Status.STATUS_STOPPED;
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_STOPPED);
    }

    /**
     * Pauses the player.
     *
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void pause() throws MPDConnectionException, MPDPlayerException {
        try {
            sendMPDCommand(playerProperties.getPause());
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        status = Status.STATUS_PAUSED;
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_PAUSED);

    }

    /**
     * Plays the next song in the playlist.
     *
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void playNext() throws MPDConnectionException, MPDPlayerException {
        try {
            sendMPDCommand(playerProperties.getNext());
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_NEXT);
    }

    /**
     * Plays the previous song in the playlist.
     *
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void playPrev() throws MPDConnectionException, MPDPlayerException {
        try {
            sendMPDCommand(playerProperties.getPrevious());
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_PREVIOUS);

    }

    /**
     * Mutes the volume of the player.
     *
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void mute() throws MPDConnectionException, MPDPlayerException {
        oldVolume = getVolume();
        setVolume(0);
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_MUTED);
    }

    /**
     * Unmutes the volume of the player.
     *
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void unMute() throws MPDConnectionException, MPDPlayerException {
        setVolume(oldVolume);
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_UNMUTED);
    }

    /**
     * Returns the instantaneous bitrate of the currently playing song.
     *
     * @return the instantaneous bitrate in kbps
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public int getBitrate() throws MPDConnectionException, MPDPlayerException {
        try {
            return serverStatus.getBitrate();
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    /**
     * Returns the current volume of the player.
     *
     * @return the volume of the player (0-100)
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public int getVolume() throws MPDConnectionException, MPDPlayerException {
        try {
            return serverStatus.getVolume();
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    /**
     * Sets the volume of the player.  The volume is between 0 and 100, any volume less
     * that 0 results in a volume of 0 while any volume greater than 100 results in a
     * volume of 100.
     *
     * @param volume the volume level (0-100)
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void setVolume(int volume) throws MPDConnectionException, MPDPlayerException {
        if (volume < 0 || volume > 100) {
            throw new MPDPlayerException("Volume not in allowable range");
        }

        try {
            sendMPDCommand(playerProperties.getSetVolume(), volume);
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        fireVolumeChangeEvent(volume);
    }

    /**
     * Returns if the player is repeating.
     *
     * @return is the player repeating
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public boolean isRepeat() throws MPDConnectionException, MPDPlayerException {
        try {
            return serverStatus.isRepeat();
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    /**
     * Sets the repeating status of the player.
     *
     * @param shouldRepeat should the player repeat the current song
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void setRepeat(boolean shouldRepeat) throws MPDConnectionException, MPDPlayerException {
        String repeat;
        if (shouldRepeat) {
            repeat = "1";
        } else {
            repeat = "0";
        }
        try {
            sendMPDCommand(playerProperties.getRepeat(), repeat);
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    /**
     * Returns if the player is in random play mode.
     *
     * @return true if the player is in random mode false otherwise
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public boolean isRandom() throws MPDConnectionException, MPDPlayerException {
        try {
            return serverStatus.isRandom();
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    /**
     * Sets the random status of the player. So the songs will be played in random order
     *
     * @param shouldRandom should the player play in random mode
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void setRandom(boolean shouldRandom) throws MPDConnectionException, MPDPlayerException {
        String random;
        if (shouldRandom) {
            random = "1";
        } else {
            random = "0";
        }
        try {
            sendMPDCommand(playerProperties.getRandom(), random);
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    /**
     * Plays the playlist in a random order.
     *
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void randomizePlay() throws MPDConnectionException, MPDPlayerException {
        setRandom(true);
    }

    /**
     * Plays the playlist in order.
     *
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void unRandomizePlay() throws MPDConnectionException, MPDPlayerException {
        setRandom(false);
    }

    /**
     * Returns the cross fade of the player in seconds.
     *
     * @return the cross fade of the player in seconds
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public int getXFade() throws MPDConnectionException, MPDPlayerException {
        try {
            return Integer.parseInt(serverStatus.getXFade());
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    /**
     * Sets the cross fade of the player in seconds.
     *
     * @param xFade the amount of cross fade to set in seconds
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void setXFade(int xFade) throws MPDConnectionException, MPDPlayerException {
        try {
            sendMPDCommand(playerProperties.getXFade(), xFade);
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    /**
     * Returns the elapsed time of the current song in seconds.
     *
     * @return the elapsed time of the song in seconds
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public long getElapsedTime() throws MPDConnectionException, MPDPlayerException {
        try {
            return serverStatus.getTime();
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

    }

    /**
     * Returns the {@link MPDAudioInfo} about the current status of the player.  If the status is unknown
     * {@code null} will be returned.  Any individual parameter that is not known will be a -1
     *
     * @return the sample rate
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public MPDAudioInfo getAudioDetails() throws MPDConnectionException, MPDPlayerException {
        MPDAudioInfo info = null;
        try {
            String response = serverStatus.getAudio();
            if (response != null) {
                info = new MPDAudioInfo();
                String[] split = response.split(":");
                try {
                    info.setSampleRate(Integer.parseInt(split[0]));
                } catch (NumberFormatException nfe) {
                    info.setSampleRate(-1);
                }
                try {
                    info.setBits(Integer.parseInt(split[1]));
                } catch (NumberFormatException nfe) {
                    info.setBits(-1);
                }
                try {
                    info.setChannels(Integer.parseInt(split[2]));
                } catch (NumberFormatException nfe) {
                    info.setChannels(-1);
                }
            }
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        return info;
    }

    /**
     * Returns the current status of the player.
     *
     * @return the status of the player
     * @throws MPDPlayerException     if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Status getStatus() throws MPDResponseException, MPDConnectionException {
        String currentStatus = serverStatus.getState();
        if (currentStatus.equalsIgnoreCase(Status.STATUS_PLAYING.getPrefix())) {
            return Status.STATUS_PLAYING;
        } else if (currentStatus.equalsIgnoreCase(Status.STATUS_PAUSED.getPrefix())) {
            return Status.STATUS_PAUSED;
        } else {
            return Status.STATUS_STOPPED;
        }
    }
}
