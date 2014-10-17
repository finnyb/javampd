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
     * @throws MPDResponseException if the MPD response generates an error
     */
    Collection<String> getStatus() throws MPDResponseException;

    int getPlaylistVersion() throws MPDResponseException;

    String getState() throws MPDResponseException;

    String getXFade() throws MPDResponseException;

    String getAudio() throws MPDResponseException;

    long getTime() throws MPDResponseException;

    long getTotalTime() throws MPDResponseException;

    int getBitrate() throws MPDResponseException;

    int getVolume() throws MPDResponseException;

    boolean isRepeat() throws MPDResponseException;

    boolean isRandom() throws MPDResponseException;
}
