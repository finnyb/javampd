package org.bff.javampd.server;

import java.util.Collection;

/**
 * @author bill
 */
public interface ServerStatus {
    /**
     * Returns the full status of the MPD server as a <CODE>Collection</CODE>
     * of Strings.
     *
     * @return the desired status information
     * @throws MPDResponseException if the MPD response generates an error
     */
    Collection<String> getStatus() throws MPDResponseException;

    int getPlaylistVersion() throws MPDResponseException;

    /**
     * Returns the current player state
     *
     * @return the current state
     * @throws MPDResponseException
     */
    String getState() throws MPDResponseException;

    int getXFade() throws MPDResponseException;

    String getAudio() throws MPDResponseException;

    /**
     * Returns the error description.  If there is no error null is returned.  You
     * might want to first check the error status with {@link #isError}
     *
     * @return the error string, null if none
     * @throws MPDResponseException
     */
    String getError() throws MPDResponseException;

    long getTime() throws MPDResponseException;

    int getBitrate() throws MPDResponseException;

    int getVolume() throws MPDResponseException;

    /**
     * Returns true if the MPD status contains an error. {@link #getError} to get the
     * error description.
     *
     * @return true if there is an error
     * @throws MPDResponseException
     */
    boolean isError() throws MPDResponseException;

    boolean isRepeat() throws MPDResponseException;

    boolean isRandom() throws MPDResponseException;
}
