package org.bff.javampd.statistics;

/**
 * represents the server's statistics at the time it is called.  The status has a default expiry interval
 * of 60 seconds.  This can be overridden by using {@link #setExpiryInterval(long)}.  You can also force an update
 * from the server by calling {@link #forceUpdate()}
 *
 * @author bill
 */
public interface ServerStatistics {
    /**
     * Returns the time length of the music played since the server was started.
     *
     * @return the time length of the music played
     */
    long getPlaytime();

    /**
     * Returns the MPD server daemon uptime in seconds.
     *
     * @return the server uptime in seconds
     */
    long getUptime();


    /**
     * Returns the total number of artists in the database.
     *
     * @return the total number of artists
     */
    int getArtistCount();

    /**
     * Returns the total number of albums in the database.
     *
     * @return the total number of albums
     */
    int getAlbumCount();

    /**
     * Returns the total number of songs in the database.
     *
     * @return the total number of songs
     */
    int getSongCount();

    /**
     * Returns the last database update in UNIX time.
     *
     * @return the last database update in UNIX time
     */
    long getLastUpdateTime();

    /**
     * Returns the sum of all song times in database.
     *
     * @return the sum of all song times
     */
    long getDatabasePlaytime();

    /**
     * Enumeration of the available information from MPD server
     * statistics.
     */
    enum StatList {
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

    /**
     * Sets the length of time that the status is considered expired.  Set to 0 to always call the server
     *
     * @param seconds the number of seconds before the loaded status is considered expired
     */
    void setExpiryInterval(long seconds);

    /**
     * Forces a server update
     */
    void forceUpdate();
}
