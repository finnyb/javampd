package org.bff.javampd.playlist;

import com.google.inject.Singleton;
import org.bff.javampd.server.MPDProperties;

/**
 * @author bill
 */
@Singleton
public class PlaylistProperties extends MPDProperties {

    private enum Command {
        ADD("MPD_PLAYLIST_ADD"),
        CLEAR("MPD_PLAYLIST_CLEAR"),
        CURRSONG("MPD_PLAYLIST_CURRSONG"),
        DELETE("MPD_PLAYLIST_DELETE"),
        CHANGES("MPD_PLAYLIST_CHANGES"),
        ID("MPD_PLAYLIST_LIST_ID"),
        INFO("MPD_PLAYLIST_LIST"),
        LOAD("MPD_PLAYLIST_LOAD"),
        MOVE("MPD_PLAYLIST_MOVE"),
        MOVEID("MPD_PLAYLIST_MOVE_ID"),
        REMOVE("MPD_PLAYLIST_REMOVE"),
        REMOVEID("MPD_PLAYLIST_REMOVE_ID"),
        SAVE("MPD_PLAYLIST_SAVE"),
        SHUFFLE("MPD_PLAYLIST_SHUFFLE"),
        SWAP("MPD_PLAYLIST_SWAP"),
        SWAPID("MPD_PLAYLIST_SWAP_ID");

        private final String key;

        Command(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public String getAdd() {
        return getPropertyString(Command.ADD.getKey());
    }

    public String getClear() {
        return getPropertyString(Command.CLEAR.getKey());
    }

    public String getCurrentSong() {
        return getPropertyString(Command.CURRSONG.getKey());
    }

    public String getDelete() {
        return getPropertyString(Command.DELETE.getKey());
    }

    public String getChanges() {
        return getPropertyString(Command.CHANGES.getKey());
    }

    public String getId() {
        return getPropertyString(Command.ID.getKey());
    }

    public String getInfo() {
        return getPropertyString(Command.INFO.getKey());
    }

    public String getLoad() {
        return getPropertyString(Command.LOAD.getKey());
    }

    public String getMove() {
        return getPropertyString(Command.MOVE.getKey());
    }

    public String getMoveId() {
        return getPropertyString(Command.MOVEID.getKey());
    }

    public String getRemove() {
        return getPropertyString(Command.REMOVE.getKey());
    }

    public String getRemoveId() {
        return getPropertyString(Command.REMOVEID.getKey());
    }

    public String getSave() {
        return getPropertyString(Command.SAVE.getKey());
    }

    public String getShuffle() {
        return getPropertyString(Command.SHUFFLE.getKey());
    }

    public String getSwap() {
        return getPropertyString(Command.SWAP.getKey());
    }

    public String getSwapId() {
        return getPropertyString(Command.SWAPID.getKey());
    }
}
