package org.bff.javampd.monitor;

import org.bff.javampd.StandAloneMonitor;
import org.bff.javampd.Status;
import org.bff.javampd.events.PlayerBasicChangeEvent;
import org.bff.javampd.events.PlayerBasicChangeListener;
import org.bff.javampd.exception.MPDException;

import java.util.ArrayList;
import java.util.List;

public class MPDPlayerMonitor implements PlayerMonitor {
    private PlayerStatus status = PlayerStatus.STATUS_STOPPED;
    private List<PlayerBasicChangeListener> playerListeners;
    private String state;

    public MPDPlayerMonitor() {
        this.playerListeners = new ArrayList<>();
    }

    @Override
    public void processResponseStatus(String line) {
        if (Status.lookupStatus(line) == Status.STATE) {
            state = line.substring(Status.STATE.getStatusPrefix().length()).trim();
        }
    }

    @Override
    public int getDelay() {
        return 0;
    }

    @Override
    public void checkStatus() throws MPDException {
        PlayerStatus newStatus = PlayerStatus.STATUS_STOPPED;
        if (state.startsWith(StandAloneMonitor.PlayerResponse.PLAY.getPrefix())) {
            newStatus = PlayerStatus.STATUS_PLAYING;
        } else if (state.startsWith(StandAloneMonitor.PlayerResponse.PAUSE.getPrefix())) {
            newStatus = PlayerStatus.STATUS_PAUSED;
        } else if (state.startsWith(StandAloneMonitor.PlayerResponse.STOP.getPrefix())) {
            newStatus = PlayerStatus.STATUS_STOPPED;
        }

        if (!status.equals(newStatus)) {
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
                    assert false : "Invalid player status --> " + status;
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
     * Sends the appropriate {@link org.bff.javampd.events.PlayerBasicChangeEvent.Status} to all registered
     * {@link PlayerBasicChangeListener}s.
     *
     * @param status the {@link org.bff.javampd.events.PlayerBasicChangeEvent.Status}
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
                assert false : "Invalid player status --> " + status;
                break;
        }
    }

    private void processPausedStatus(PlayerStatus status) {
        switch (status) {
            case STATUS_PAUSED:
                firePlayerChangeEvent(PlayerBasicChangeEvent.Status.PLAYER_UNPAUSED);
                break;
            case STATUS_PLAYING:
                firePlayerChangeEvent(PlayerBasicChangeEvent.Status.PLAYER_PAUSED);
                break;
            case STATUS_STOPPED:
                firePlayerChangeEvent(PlayerBasicChangeEvent.Status.PLAYER_STOPPED);
                break;
            default:
                assert false : "Invalid player status --> " + status;
                break;
        }
    }
}
