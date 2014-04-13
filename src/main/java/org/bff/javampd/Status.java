package org.bff.javampd;

/**
 * Enumeration of the available information from the MPD
 * server status.
 */
public enum Status {

    /**
     * The current volume (0-100)
     */
    VOLUME("volume:"),
    /**
     * is the song repeating (0 or 1)
     */
    REPEAT("repeat:"),
    /**
     * is the song playing in random order (0 or 1)
     */
    RANDOM("random:"),
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
    Status(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the <CODE>String</CODE> prefix of the response.
     *
     * @return the prefix of the response
     */
    public String getStatusPrefix() {
        return prefix;
    }

    /**
     * Returns the {@link org.bff.javampd.Status} the status line starts with
     *
     * @param statusLine the line to process
     * @return the {@link org.bff.javampd.Status} the lines starts with.  <code>null</code>
     * if there isn't a match
     */
    public static Status lookupStatus(String statusLine) {
        for (Status statusList : Status.values()) {
            if (statusLine.startsWith(statusList.getStatusPrefix())) {
                return statusList;
            }
        }
        return null;
    }
}
