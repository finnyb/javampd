package org.bff.javampd;

import org.bff.javampd.exception.MPDResponseException;

/**
 * @author bill
 * @since: 11/24/13 10:13 AM
 */
public interface ServerStatistics {
    /**
     * Returns the time length of the music played since the server was started.
     *
     * @return the time length of the music played
     * @throws MPDResponseException   if the MPD response generates an error
     */
    long getPlaytime() throws MPDResponseException;

    /**
     * Returns the MPD server daemon uptime in seconds.
     *
     * @return the server uptime in seconds
     * @throws MPDResponseException   if the MPD response generates an error
     */
    long getUptime() throws MPDResponseException;

    int getAlbums() throws MPDResponseException;

    int getArtists() throws MPDResponseException;

    int getSongs() throws MPDResponseException;

    long getDatabasePlaytime() throws MPDResponseException;

    long getDatabaseUpdateTime() throws MPDResponseException;

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
