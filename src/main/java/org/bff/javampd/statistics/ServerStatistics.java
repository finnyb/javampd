package org.bff.javampd.statistics;

import org.bff.javampd.server.MPDResponseException;

/**
 * @author bill
 */
public interface ServerStatistics {
    /**
     * Returns the time length of the music played since the server was started.
     *
     * @return the time length of the music played
     * @throws MPDResponseException if the MPD response generates an error
     */
    long getPlaytime() throws MPDResponseException;

    /**
     * Returns the MPD server daemon uptime in seconds.
     *
     * @return the server uptime in seconds
     * @throws MPDResponseException if the MPD response generates an error
     */
    long getUptime() throws MPDResponseException;


    /**
     * Returns the total number of artists in the database.
     *
     * @return the total number of artists
     * @throws MPDResponseException if the MPD responded with an error
     */
    int getArtistCount() throws MPDResponseException;

    /**
     * Returns the total number of albums in the database.
     *
     * @return the total number of albums
     * @throws MPDResponseException if the MPD responded with an error
     */
    int getAlbumCount() throws MPDResponseException;

    /**
     * Returns the total number of songs in the database.
     *
     * @return the total number of songs
     * @throws MPDResponseException if the MPD responded with an error
     */
    int getSongCount() throws MPDResponseException;

    /**
     * Returns the last database update in UNIX time.
     *
     * @return the last database update in UNIX time
     * @throws MPDResponseException if the MPD responded with an error
     */
    long getLastUpdateTime() throws MPDResponseException;

    /**
     * Returns the sum of all song times in database.
     *
     * @return the sum of all song times
     * @throws MPDResponseException if the MPD responded with an error
     */
    long getDatabasePlaytime() throws MPDResponseException;

    /**
     * Enumeration of the available information from MPD server
     * statistics.
     */
    public enum StatList {
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
}
