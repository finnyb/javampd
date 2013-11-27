package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;

import java.util.Collection;

/**
 * @author bill
 * @since: 11/24/13 10:12 AM
 */
public interface ServerStatus {
    /**
     * Returns the full status of the MPD server as a <CODE>Collection</CODE>
     * of Strings.  To query specific statuses use {@link #getStatus(org.bff.javampd.ServerStatus.StatusList status)}.
     *
     * @return the desired status information
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    Collection<String> getStatus() throws MPDConnectionException, MPDResponseException;

    int getPlaylistVersion() throws MPDResponseException, MPDConnectionException;

    String getState() throws MPDResponseException, MPDConnectionException;

    String getXFade() throws MPDResponseException, MPDConnectionException;

    String getAudio() throws MPDResponseException, MPDConnectionException;

    long getTime() throws MPDResponseException, MPDConnectionException;

    int getBitrate() throws MPDResponseException, MPDConnectionException;

    int getVolume() throws MPDResponseException, MPDConnectionException;

    boolean isRepeat() throws MPDResponseException, MPDConnectionException;

    boolean isRandom() throws MPDResponseException, MPDConnectionException;

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
