package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.properties.ServerProperties;

import java.util.Collection;
import java.util.List;

/**
 * @author bill
 * @since: 11/22/13 7:59 AM
 */
public class MPDServerStatus extends CommandExecutor {
    private final ServerProperties commandProperties;

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

    public MPDServerStatus(MPD mpd) {
        super(mpd);
        this.commandProperties = new ServerProperties();
    }

    /**
     * Returns the current status of the requested status element.
     * See <code>StatusList</code> for a list of possible items returned
     * by getStatus.
     *
     * @param status the status desired
     * @return the desired status information
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    protected String getStatus(StatusList status) throws MPDConnectionException, MPDResponseException {
        List<String> respList = sendMPDCommand(commandProperties.getStatus());

        for (String line : respList) {
            if (line.startsWith(status.getStatusPrefix())) {
                return line.substring(status.getStatusPrefix().length()).trim();
            }
        }
        return null;
    }

    /**
     * Returns the full status of the MPD server as a <CODE>Collection</CODE>
     * of Strings.  To query specific statuses use {@link #getStatus(StatusList status)}.
     *
     * @return the desired status information
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    public Collection<String> getStatus() throws MPDConnectionException, MPDResponseException {
        List<String> respList = sendMPDCommand(commandProperties.getStatus());
        return respList;
    }

    public int getPlaylistVersion() throws MPDResponseException, MPDConnectionException {
        return Integer.parseInt(getStatus(StatusList.PLAYLIST));
    }

    public String getState() throws MPDResponseException, MPDConnectionException {
        return getStatus(StatusList.STATE);
    }

    public String getXFade() throws MPDResponseException, MPDConnectionException {
        return getStatus(StatusList.XFADE);
    }

    public String getAudio() throws MPDResponseException, MPDConnectionException {
        return getStatus(StatusList.AUDIO);
    }

    public long getTime() throws MPDResponseException, MPDConnectionException {
        String time = getStatus(StatusList.TIME);

        if (time == null || !time.contains(":")) {
            return 0;
        } else {
            return Integer.parseInt(time.trim().split(":")[0]);
        }
    }

    public int getBitrate() throws MPDResponseException, MPDConnectionException {
        return Integer.parseInt(getStatus(StatusList.BITRATE));
    }

    public int getVolume() throws MPDResponseException, MPDConnectionException {
        return Integer.parseInt(getStatus(StatusList.VOLUME));
    }

    public boolean isRepeat() throws MPDResponseException, MPDConnectionException {
        return "1".equals(getStatus(StatusList.REPEAT));
    }

    public boolean isRandom() throws MPDResponseException, MPDConnectionException {
        return "1".equals(getStatus(StatusList.RANDOM));
    }
}
