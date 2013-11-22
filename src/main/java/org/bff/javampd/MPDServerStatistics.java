package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.properties.ServerProperties;

import java.util.List;

/**
 * @author bill
 * @since: 11/22/13 7:59 AM
 */
public class MPDServerStatistics extends CommandExecutor {
    private final ServerProperties commandProperties;

    /**
     * Enumeration of the available information from MPD server
     * statistics.
     */
    protected enum StatList {
        ALBUMS("albums:"),
        ARTISTS("artists:"),
        SONGS("songs:"),
        DBPLAYTIME("db_playtime:"),
        DBUPDATE("db_update:"),
        PLAYTIME("playtime:"),
        UPTIME("uptime:");

        private String prefix;

        /**
         * Default constructor for Statistics
         *
         * @param prefix the prefix of the line in the response
         */
        StatList(String prefix) {
            this.prefix = prefix;
        }

        /**
         * Returns the <CODE>String</CODE> prefix of the response.
         *
         * @return the prefix of the response
         */
        public String getStatPrefix() {
            return prefix;
        }
    }

    public MPDServerStatistics(MPD mpd) {
        super(mpd);
        this.commandProperties = new ServerProperties();
    }

    /**
     * Returns the statistics of the requested statistics element.
     * See <code>StatList</code> for a list of possible items returned
     * by getServerStat.
     *
     * @param stat the statistic desired
     * @return the requested statistic
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    protected String getStat(StatList stat) throws MPDConnectionException, MPDResponseException {
        List<String> respList = sendMPDCommand(commandProperties.getStats());

        for (String line : respList) {
            if (line.startsWith(stat.getStatPrefix())) {
                return line.substring(stat.getStatPrefix().length()).trim();
            }
        }
        return null;
    }


    /**
     * Returns the time length of the music played since the server was started.
     *
     * @return the time length of the music played
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    public long getPlaytime() throws MPDConnectionException, MPDResponseException {
        return Long.parseLong(getStat(StatList.PLAYTIME));
    }

    /**
     * Returns the MPD server daemon uptime in seconds.
     *
     * @return the server uptime in seconds
     * @throws org.bff.javampd.exception.MPDResponseException
     *          if the MPD response generates an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command to the server
     */
    public long getUptime() throws MPDConnectionException, MPDResponseException {
        return Long.parseLong(getStat(StatList.UPTIME));
    }

    public int getAlbums() throws MPDConnectionException, MPDResponseException {
        return Integer.parseInt(getStat(StatList.ALBUMS));
    }

    public int getArtists() throws MPDConnectionException, MPDResponseException {
        return Integer.parseInt(getStat(StatList.ARTISTS));
    }

    public int getSongs() throws MPDConnectionException, MPDResponseException {
        return Integer.parseInt(getStat(StatList.SONGS));
    }

    public long getDatabasePlaytime() throws MPDConnectionException, MPDResponseException {
        return Integer.parseInt(getStat(StatList.DBPLAYTIME));
    }

    public long getDatabaseUpdateTime() throws MPDConnectionException, MPDResponseException {
        return Long.parseLong(getStat(StatList.DBUPDATE));
    }
}
