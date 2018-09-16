package org.bff.javampd.player;

import com.google.inject.Inject;
import org.bff.javampd.audioinfo.MPDAudioInfo;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.server.ServerStatus;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * MPDPlayer represents a player controller to a MPD server.  To obtain
 * an instance of the class you must use the <code>getMPDPlayer</code> method from
 * the {@link org.bff.javampd.server.MPD} connection class.  This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill
 */
public class MPDPlayer implements Player {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDPlayer.class);

    private int oldVolume;
    private List<PlayerChangeListener> listeners = new ArrayList<>();
    private VolumeChangeDelegate volumeChangeDelegate;

    private Status status = Status.STATUS_STOPPED;
    private ServerStatus serverStatus;
    private PlayerProperties playerProperties;
    private CommandExecutor commandExecutor;
    private SongConverter songConverter;

    @Inject
    public MPDPlayer(ServerStatus serverStatus,
                     PlayerProperties playerProperties,
                     CommandExecutor commandExecutor,
                     SongConverter songConverter) {
        this.serverStatus = serverStatus;
        this.playerProperties = playerProperties;
        this.commandExecutor = commandExecutor;
        this.songConverter = songConverter;
        this.volumeChangeDelegate = new VolumeChangeDelegate();
    }

    @Override
    public MPDSong getCurrentSong() {
        List<MPDSong> songList =
                songConverter.convertResponseToSong(commandExecutor.sendCommand(playerProperties.getCurrentSong()));

        if (songList.isEmpty()) {
            return null;
        } else {
            return songList.get(0);
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
        volumeChangeDelegate.addVolumeChangeListener(vcl);
    }

    @Override
    public synchronized void removeVolumeChangedListener(VolumeChangeListener vcl) {
        volumeChangeDelegate.removeVolumeChangedListener(vcl);
    }

    /**
     * Sends the appropriate {@link VolumeChangeEvent} to all registered
     * {@link VolumeChangeListener}.
     *
     * @param volume the new volume
     */
    protected synchronized void fireVolumeChangeEvent(int volume) {
        volumeChangeDelegate.fireVolumeChangeEvent(this, volume);
    }

    @Override
    public void play() {
        playSong(null);
    }

    @Override
    public void playSong(MPDSong song) {
        if (song == null) {
            commandExecutor.sendCommand(playerProperties.getPlay());
        } else {
            commandExecutor.sendCommand(playerProperties.getPlayId(), song.getId());
        }

        if (status == Status.STATUS_STOPPED || status == Status.STATUS_PAUSED) {
            status = Status.STATUS_PLAYING;
            firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_STARTED);
        } else {
            firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_SONG_SET);
        }
    }

    @Override
    public void seek(long secs) {
        seekSong(null, secs);
    }

    @Override
    public void seekSong(MPDSong song, long secs) {
        List<String> response = null;
        String[] params = new String[2];
        params[1] = Long.toString(secs);
        if (song == null) {
            if (getCurrentSong().getLength() > secs) {
                params[0] = Integer.toString(getCurrentSong().getId());
                response = commandExecutor.sendCommand(playerProperties.getSeekId(), params);
            }
        } else {
            if (song.getLength() >= secs) {
                params[0] = Integer.toString(song.getId());
                response = commandExecutor.sendCommand(playerProperties.getSeekId(), params);
            }
        }

        if (response != null) {
            firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_SEEKING);
        }
    }

    @Override
    public void stop() {
        commandExecutor.sendCommand(playerProperties.getStop());
        status = Status.STATUS_STOPPED;
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_STOPPED);
    }

    @Override
    public void pause() {
        commandExecutor.sendCommand(playerProperties.getPause());
        status = Status.STATUS_PAUSED;
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_PAUSED);

    }

    @Override
    public void playNext() {
        commandExecutor.sendCommand(playerProperties.getNext());
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_NEXT);
    }

    @Override
    public void playPrevious() {
        commandExecutor.sendCommand(playerProperties.getPrevious());
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_PREVIOUS);

    }

    @Override
    public void mute() {
        oldVolume = getVolume();
        setVolume(0);
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_MUTED);
    }

    @Override
    public void unMute() {
        setVolume(oldVolume);
        firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_UNMUTED);
    }

    @Override
    public int getBitrate() {
        return serverStatus.getBitrate();
    }

    @Override
    public int getVolume() {
        return serverStatus.getVolume();
    }

    @Override
    public void setVolume(int volume) {
        if (volume < 0 || volume > 100) {
            LOGGER.warn("Not changing volume to {}", volume);
        } else {
            commandExecutor.sendCommand(playerProperties.getSetVolume(), volume);
            fireVolumeChangeEvent(volume);
        }
    }

    @Override
    public boolean isRepeat() {
        return serverStatus.isRepeat();
    }

    @Override
    public void setRepeat(boolean shouldRepeat) {
        String repeat;
        if (shouldRepeat) {
            repeat = "1";
        } else {
            repeat = "0";
        }
        commandExecutor.sendCommand(playerProperties.getRepeat(), repeat);
    }

    @Override
    public boolean isRandom() {
        return serverStatus.isRandom();
    }

    @Override
    public void setRandom(boolean shouldRandom) {
        String random;
        if (shouldRandom) {
            random = "1";
        } else {
            random = "0";
        }
        commandExecutor.sendCommand(playerProperties.getRandom(), random);
    }

    @Override
    public void randomizePlay() {
        setRandom(true);
    }

    @Override
    public void unRandomizePlay() {
        setRandom(false);
    }

    @Override
    public int getXFade() {
        return serverStatus.getXFade();
    }

    @Override
    public void setXFade(int xFade) {
        commandExecutor.sendCommand(playerProperties.getXFade(), xFade);
    }

    @Override
    public long getElapsedTime() {
        return serverStatus.getElapsedTime();

    }

    @Override
    public long getTotalTime() {
        return serverStatus.getTotalTime();
    }

    @Override
    public void setConsume(boolean pConsume) {
        String consume;
        if (pConsume) {
            consume = "1";
        } else {
            consume = "0";
        }
        commandExecutor.sendCommand(playerProperties.getConsume(), consume);
    }

    @Override
    public void setSingle(boolean pSingle) {
        String single;
        if (pSingle) {
            single = "1";
        } else {
            single = "0";
        }
        commandExecutor.sendCommand(playerProperties.getSingle(), single);
    }

    @Override
    public MPDAudioInfo getAudioDetails() {
        MPDAudioInfo info = null;

        String response = serverStatus.getAudio();
        if (!"".equals(response)) {
            info = new MPDAudioInfo();
            String[] split = response.split(":");
            parseSampleRate(info, split[0]);
            parseBitRate(info, split[1]);
            parseChannels(info, split[2]);
        }

        return info;
    }

    private static void parseSampleRate(MPDAudioInfo info, String sampleRate) {
        try {
            info.setSampleRate(Integer.parseInt(sampleRate));
        } catch (NumberFormatException nfe) {
            LOGGER.error("Could not format sample rate", nfe);
            info.setSampleRate(-1);
        }
    }

    @Override
    public Status getStatus() {
        String currentStatus = serverStatus.getState();

        if (currentStatus.equalsIgnoreCase(Status.STATUS_PLAYING.getPrefix())) {
            return Status.STATUS_PLAYING;
        } else if (currentStatus.equalsIgnoreCase(Status.STATUS_PAUSED.getPrefix())) {
            return Status.STATUS_PAUSED;
        } else {
            return Status.STATUS_STOPPED;
        }
    }

    private static void parseChannels(MPDAudioInfo info, String channels) {
        try {
            info.setChannels(Integer.parseInt(channels));
        } catch (NumberFormatException nfe) {
            LOGGER.error("Could not format channels", nfe);
            info.setChannels(-1);
        }
    }

    private static void parseBitRate(MPDAudioInfo info, String bitRate) {
        try {
            info.setBits(Integer.parseInt(bitRate));
        } catch (NumberFormatException nfe) {
            LOGGER.error("Could not format bits", nfe);
            info.setBits(-1);
        }
    }
}
