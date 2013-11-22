package org.bff.javampd.properties;

/**
 * @author bill
 * @since: 11/22/13 11:57 AM
 */
public class PlayerProperties extends MPDProperties {
    private enum Command {
        XFADE("MPD_PLAYER_CROSSFADE"),
        CURRSONG("MPD_PLAYER_CURRENTSONG"),
        NEXT("MPD_PLAYER_NEXT"),
        PAUSE("MPD_PLAYER_PAUSE"),
        PLAY("MPD_PLAYER_PLAY"),
        PLAYID("MPD_PLAYER_PLAY_ID"),
        PREV("MPD_PLAYER_PREV"),
        REPEAT("MPD_PLAYER_REPEAT"),
        RANDOM("MPD_PLAYER_RANDOM"),
        SEEK("MPD_PLAYER_SEEK"),
        SEEKID("MPD_PLAYER_SEEK_ID"),
        STOP("MPD_PLAYER_STOP"),
        SETVOL("MPD_PLAYER_SET_VOLUME");

        private final String key;

        Command(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public String getXFade() {
        return getPropertyString(Command.XFADE.getKey());
    }

    public String getCurrentSong() {
        return getPropertyString(Command.CURRSONG.getKey());
    }

    public String getNext() {
        return getPropertyString(Command.NEXT.getKey());
    }

    public String getPause() {
        return getPropertyString(Command.PAUSE.getKey());
    }

    public String getPlay() {
        return getPropertyString(Command.PLAY.getKey());
    }

    public String getPlayId() {
        return getPropertyString(Command.PLAYID.getKey());
    }

    public String getPrevious() {
        return getPropertyString(Command.PREV.getKey());
    }

    public String getRepeat() {
        return getPropertyString(Command.REPEAT.getKey());
    }

    public String getRandom() {
        return getPropertyString(Command.RANDOM.getKey());
    }

    public String getSeek() {
        return getPropertyString(Command.SEEK.getKey());
    }

    public String getSeekId() {
        return getPropertyString(Command.SEEKID.getKey());
    }

    public String getStop() {
        return getPropertyString(Command.STOP.getKey());
    }

    public String getSetVolume() {
        return getPropertyString(Command.SETVOL.getKey());
    }
}
