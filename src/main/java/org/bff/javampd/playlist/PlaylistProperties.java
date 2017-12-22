package org.bff.javampd.playlist;

import org.bff.javampd.server.MPDProperties;

/**
 * @author bill
 */
public class PlaylistProperties extends MPDProperties {

    private enum Command {
        ADD("playlist.add"),
        CLEAR("playlist.clear"),
        CURRSONG("playlist.currsong"),
        DELETE("playlist.delete"),
        CHANGES("playlist.changes"),
        ID("playlist.list.id"),
        INFO("playlist.list"),
        LOAD("playlist.load"),
        MOVE("playlist.move"),
        MOVEID("playlist.move.id"),
        REMOVE("playlist.remove"),
        REMOVEID("playlist.remove.id"),
        SAVE("playlist.save"),
        SHUFFLE("playlist.shuffle"),
        SWAP("playlist.swap"),
        SWAPID("playlist.swap.id");

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
