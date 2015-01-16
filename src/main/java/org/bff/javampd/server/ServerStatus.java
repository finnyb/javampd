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
    Collection<String> getStatus();

    int getPlaylistVersion();

    /**
     * Returns the current player state
     *
     * @return the current state
     * @throws MPDResponseException
     */
    String getState();

    int getXFade();

    String getAudio();

    /**
     * Returns the error description.  If there is no error null is returned.  You
     * might want to first check the error status with {@link #isError}
     *
     * @return the error string, null if none
     * @throws MPDResponseException
     */
    String getError();

    /**
     * Returns the elapsed time of the currently playing song
     *
     * @return elapsed time of the song
     * @throws MPDResponseException
     */
    long getElapsedTime();

    /**
     * Returns the total time of the currently playing song
     *
     * @return total time of the song
     * @throws MPDResponseException
     */
    long getTotalTime();

    int getBitrate();

    int getVolume();

    /**
     * Returns true if the MPD status contains an error. {@link #getError} to get the
     * error description.
     *
     * @return true if there is an error
     * @throws MPDResponseException
     */
    boolean isError();

    boolean isRepeat();

    boolean isRandom();
}
