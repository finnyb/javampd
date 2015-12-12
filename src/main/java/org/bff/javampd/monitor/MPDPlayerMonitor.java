package org.bff.javampd.monitor;

import com.google.inject.Singleton;
import org.bff.javampd.player.PlayerBasicChangeEvent;
import org.bff.javampd.player.PlayerBasicChangeListener;
import org.bff.javampd.server.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class MPDPlayerMonitor extends MPDBitrateMonitor implements PlayerMonitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDPlayerMonitor.class);

    private PlayerStatus status = PlayerStatus.STATUS_STOPPED;
    private List<PlayerBasicChangeListener> playerListeners;
    private String state;

    MPDPlayerMonitor() {
        this.playerListeners = new ArrayList<>();
        state = "";
    }

    @Override
    public void processResponseStatus(String line) {
        super.processResponseStatus(line);
        if (Status.lookupStatus(line) == Status.STATE) {
            state = line.substring(Status.STATE.getStatusPrefix().length()).trim();
        }
    }

    @Override
    public void checkStatus() {
        super.checkStatus();
        PlayerStatus newStatus = PlayerStatus.STATUS_STOPPED;
        if (state.startsWith(StandAloneMonitor.PlayerResponse.PLAY.getPrefix())) {
            newStatus = PlayerStatus.STATUS_PLAYING;
        } else if (state.startsWith(StandAloneMonitor.PlayerResponse.PAUSE.getPrefix())) {
            newStatus = PlayerStatus.STATUS_PAUSED;
        } else if (state.startsWith(StandAloneMonitor.PlayerResponse.STOP.getPrefix())) {
            newStatus = PlayerStatus.STATUS_STOPPED;
        }

        if (!status.equals(newStatus)) {
            LOGGER.debug("status change from {} to {}", status, newStatus);
            switch (newStatus) {
                case STATUS_PLAYING:
                    processPlayingStatus(status);
                    break;
                case STATUS_STOPPED:
                    firePlayerChangeEvent(PlayerBasicChangeEvent.Status.PLAYER_STOPPED);
                    break;
                case STATUS_PAUSED:
                    processPausedStatus(status);
                    break;
                default:
                    LOGGER.warn("Invalid player status --> {}", status);
                    break;
            }
            status = newStatus;
        }
    }

    @Override
    public synchronized void addPlayerChangeListener(PlayerBasicChangeListener pcl) {
        playerListeners.add(pcl);
    }

    @Override
    public synchronized void removePlayerChangeListener(PlayerBasicChangeListener pcl) {
        playerListeners.remove(pcl);
    }

    @Override
    public PlayerStatus getStatus() {
        return this.status;
    }

    /**
     * Sends the appropriate {@link org.bff.javampd.player.PlayerBasicChangeEvent.Status} to all registered
     * {@link PlayerBasicChangeListener}s.
     *
     * @param status the {@link org.bff.javampd.player.PlayerBasicChangeEvent.Status}
     */
    protected synchronized void firePlayerChangeEvent(PlayerBasicChangeEvent.Status status) {
        PlayerBasicChangeEvent pce = new PlayerBasicChangeEvent(this, status);

        for (PlayerBasicChangeListener pcl : playerListeners) {
            pcl.playerBasicChange(pce);
        }
    }

    private void processPlayingStatus(PlayerStatus status) {
        switch (status) {
            case STATUS_PAUSED:
                firePlayerChangeEvent(PlayerBasicChangeEvent.Status.PLAYER_UNPAUSED);
                break;
            case STATUS_STOPPED:
                firePlayerChangeEvent(PlayerBasicChangeEvent.Status.PLAYER_STARTED);
                break;
            default:
                LOGGER.warn("Invalid player status --> {}", status);
                break;
        }
    }

    private void processPausedStatus(PlayerStatus status) {
        switch (status) {
            case STATUS_PLAYING:
                firePlayerChangeEvent(PlayerBasicChangeEvent.Status.PLAYER_PAUSED);
                break;
            case STATUS_STOPPED:
                firePlayerChangeEvent(PlayerBasicChangeEvent.Status.PLAYER_STOPPED);
                break;
            default:
                LOGGER.warn("Invalid player status --> {}", status);
                break;
        }
    }
}
