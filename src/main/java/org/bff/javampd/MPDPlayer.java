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

import com.google.inject.Inject;
import org.bff.javampd.events.PlayerChangeEvent;
import org.bff.javampd.events.PlayerChangeListener;
import org.bff.javampd.events.VolumeChangeEvent;
import org.bff.javampd.events.VolumeChangeListener;
import org.bff.javampd.exception.MPDPlayerException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.MPDAudioInfo;
import org.bff.javampd.objects.MPDSong;
import org.bff.javampd.properties.PlayerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * MPDPlayer represents a player controller to a MPD server.  To obtain
 * an instance of the class you must use the <code>getMPDPlayer</code> method from
 * the {@link MPD} connection class.  This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill
 */
public class MPDPlayer implements Player {

    private int oldVolume;
    private List<PlayerChangeListener> listeners = new ArrayList<PlayerChangeListener>();
    private List<VolumeChangeListener> volListeners = new ArrayList<VolumeChangeListener>();

    private Status status = Status.STATUS_STOPPED;
    @Inject
    private ServerStatus serverStatus;
    @Inject
    private PlayerProperties playerProperties;
    @Inject
    private CommandExecutor commandExecutor;

    private final Logger logger = LoggerFactory.getLogger(MPDPlayer.class);

    @Override
    public MPDSong getCurrentSong() throws MPDPlayerException {
        try {
            List<MPDSong> songList =
                    MPDSongConverter.convertResponseToSong(commandExecutor.sendCommand(playerProperties.getCurrentSong()));

            if (songList.isEmpty()) {
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

    @Override
    public synchronized void addPlayerChangeListener(PlayerChangeListener pcl) {
        listeners.add(pcl);
    }

    @Override
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

    @Override
    public synchronized void addVolumeChangeListener(VolumeChangeListener vcl) {
        volListeners.add(vcl);
    }

    @Override
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

    @Override
    public void play() throws MPDPlayerException {
        playId(null);
    }

    @Override
    public void playId(MPDSong song) throws MPDPlayerException {
        try {
            if (song == null) {
                commandExecutor.sendCommand(playerProperties.getPlay());
            } else {
                commandExecutor.sendCommand(playerProperties.getPlayId(), song.getId());
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

    @Override
    public void seek(long secs) throws MPDPlayerException {
        seekId(null, secs);
    }

    @Override
    public void seekId(MPDSong song, long secs) throws MPDPlayerException {
        List<String> response = null;
        String params[] = new String[2];
        params[2] = Long.toString(secs);
        if (song == null) {
            if (getCurrentSong().getLength() > secs) {
                params[1] = Integer.toString(getCurrentSong().getId());
                try {
                    response = commandExecutor.sendCommand(playerProperties.getSeekId(), params);
                } catch (MPDResponseException re) {
                    throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
                }
            }
        } else {
            if (song.getLength() >= secs) {
                params[1] = Integer.toString(song.getId());
                try {
                    response = commandExecutor.sendCommand(playerProperties.getSeekId(), params);
                } catch (MPDResponseException re) {
                    throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
                }
            }
        }

        if (response != null) {
            firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_SEEKING);
        }
    }

    @Override
    public void stop() throws MPDPlayerException {
        try {
            commandExecutor.sendCommand(playerProperties.getStop());
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        status = Status.STATUS_STOPPED;
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_STOPPED);
    }

    @Override
    public void pause() throws MPDPlayerException {
        try {
            commandExecutor.sendCommand(playerProperties.getPause());
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        status = Status.STATUS_PAUSED;
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_PAUSED);

    }

    @Override
    public void playNext() throws MPDPlayerException {
        try {
            commandExecutor.sendCommand(playerProperties.getNext());
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_NEXT);
    }

    @Override
    public void playPrev() throws MPDPlayerException {
        try {
            commandExecutor.sendCommand(playerProperties.getPrevious());
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_PREVIOUS);

    }

    @Override
    public void mute() throws MPDPlayerException {
        oldVolume = getVolume();
        setVolume(0);
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_MUTED);
    }

    @Override
    public void unMute() throws MPDPlayerException {
        setVolume(oldVolume);
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_UNMUTED);
    }

    @Override
    public int getBitrate() throws MPDPlayerException {
        try {
            return serverStatus.getBitrate();
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    @Override
    public int getVolume() throws MPDPlayerException {
        try {
            return serverStatus.getVolume();
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    @Override
    public void setVolume(int volume) throws MPDPlayerException {
        if (volume < 0 || volume > 100) {
            throw new MPDPlayerException("Volume not in allowable range");
        }

        try {
            commandExecutor.sendCommand(playerProperties.getSetVolume(), volume);
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        fireVolumeChangeEvent(volume);
    }

    @Override
    public boolean isRepeat() throws MPDPlayerException {
        try {
            return serverStatus.isRepeat();
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    @Override
    public void setRepeat(boolean shouldRepeat) throws MPDPlayerException {
        String repeat;
        if (shouldRepeat) {
            repeat = "1";
        } else {
            repeat = "0";
        }
        try {
            commandExecutor.sendCommand(playerProperties.getRepeat(), repeat);
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    @Override
    public boolean isRandom() throws MPDPlayerException {
        try {
            return serverStatus.isRandom();
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    @Override
    public void setRandom(boolean shouldRandom) throws MPDPlayerException {
        String random;
        if (shouldRandom) {
            random = "1";
        } else {
            random = "0";
        }
        try {
            commandExecutor.sendCommand(playerProperties.getRandom(), random);
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    @Override
    public void randomizePlay() throws MPDPlayerException {
        setRandom(true);
    }

    @Override
    public void unRandomizePlay() throws MPDPlayerException {
        setRandom(false);
    }

    @Override
    public int getXFade() throws MPDPlayerException {
        try {
            return Integer.parseInt(serverStatus.getXFade());
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    @Override
    public void setXFade(int xFade) throws MPDPlayerException {
        try {
            commandExecutor.sendCommand(playerProperties.getXFade(), xFade);
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }
    }

    @Override
    public long getElapsedTime() throws MPDPlayerException {
        try {
            return serverStatus.getTime();
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

    }

    @Override
    public MPDAudioInfo getAudioDetails() throws MPDPlayerException {
        MPDAudioInfo info = null;
        try {
            String response = serverStatus.getAudio();
            if (response != null) {
                info = new MPDAudioInfo();
                String[] split = response.split(":");
                parseSampleRate(info, split[0]);
                parseBitRate(info, split[1]);
                parseChannels(info, split[2]);
            }
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        return info;
    }

    private void parseSampleRate(MPDAudioInfo info, String sampleRate) {
        try {
            info.setSampleRate(Integer.parseInt(sampleRate));
        } catch (NumberFormatException nfe) {
            logger.error("Could not format sample rate", nfe);
            info.setSampleRate(-1);
        }
    }

    @Override
    public Status getStatus() throws MPDPlayerException {
        String currentStatus = null;
        try {
            currentStatus = serverStatus.getState();
        } catch (MPDResponseException re) {
            throw new MPDPlayerException(re.getMessage(), re.getCommand(), re);
        }

        if (currentStatus.equalsIgnoreCase(Status.STATUS_PLAYING.getPrefix())) {
            return Status.STATUS_PLAYING;
        } else if (currentStatus.equalsIgnoreCase(Status.STATUS_PAUSED.getPrefix())) {
            return Status.STATUS_PAUSED;
        } else {
            return Status.STATUS_STOPPED;
        }
    }

    private void parseChannels(MPDAudioInfo info, String channels) {
        try {
            info.setChannels(Integer.parseInt(channels));
        } catch (NumberFormatException nfe) {
            logger.error("Could not format channels", nfe);
            info.setChannels(-1);
        }
    }

    private void parseBitRate(MPDAudioInfo info, String bitRate) {
        try {
            info.setBits(Integer.parseInt(bitRate));
        } catch (NumberFormatException nfe) {
            logger.error("Could not format bits", nfe);
            info.setBits(-1);
        }
    }
}
