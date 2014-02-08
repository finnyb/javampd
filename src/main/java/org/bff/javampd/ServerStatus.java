package org.bff.javampd;

import org.bff.javampd.exception.MPDResponseException;

import java.util.Collection;

/**
 * @author bill
 * @since: 11/24/13 10:12 AM
 */
public interface ServerStatus {
    /**
     * Returns the full status of the MPD server as a <CODE>Collection</CODE>
     * of Strings.
     *
     * @return the desired status information
     * @throws MPDResponseException   if the MPD response generates an error
     */
    Collection<String> getStatus() throws MPDResponseException;

    int getPlaylistVersion() throws MPDResponseException;

    String getState() throws MPDResponseException;

    String getXFade() throws MPDResponseException;

    String getAudio() throws MPDResponseException;

    long getTime() throws MPDResponseException;

    int getBitrate() throws MPDResponseException;

    int getVolume() throws MPDResponseException;

    boolean isRepeat() throws MPDResponseException;

    boolean isRandom() throws MPDResponseException;

    /**
     * Enumeration of the available information from the MPD
     * server status.
     */
    public enum StatusList {

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
         * the current sample rate, bits, and channels
         */
        AUDIO("audio:"),
        /**
         * job id
         */
        UPDATINGSDB("updatings_db:"),
        /**
         * if there is an error, returns message here
         */
        ERROR("error:");
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
            return prefix;
        }
    }
}
