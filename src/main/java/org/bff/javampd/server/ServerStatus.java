package org.bff.javampd.server;

import java.util.Collection;
import java.util.Optional;

/**
 * ServerStatus represents the server's status at the time it is called.  The status has a default expiry interval
 * of 5 seconds.  This can be overridden by using {@link #setExpiryInterval(long)}.  You can also force an update
 * from the server by calling {@link #forceUpdate()}
 *
 * @author bill
 */
public interface ServerStatus {
    /**
     * Returns the full status of the MPD server as a <CODE>Collection</CODE>
     * of Strings.
     *
     * @return the desired status information
     */
    Collection<String> getStatus();

    /**
     * Returns the current playlist version
     *
     * @return the playlist version
     */
    int getPlaylistVersion();

    /**
     * Returns the current player state
     *
     * @return the current state
     */
    String getState();

    /**
     * Returns the cross fade value
     *
     * @return the crossfade value
     */
    int getXFade();

    /**
     * Returns the audio format  (sampleRate:bits:channels)
     *
     * @return the audio format
     */
    String getAudio();

    /**
     * Returns the error description.  If there is no error null is returned.  You
     * might want to first check the error status with {@link #isError}
     *
     * @return the error string, null if none
     */
    String getError();

    /**
     * Returns the elapsed time of the currently playing song
     *
     * @return elapsed time of the song
     */
    long getElapsedTime();

    /**
     * Returns the total time of the currently playing song
     *
     * @return total time of the song
     */
    long getTotalTime();

    /**
     * Returns the current bitrate of the {@link org.bff.javampd.song.MPDSong} playing
     *
     * @return the current bitrate
     */
    int getBitrate();

    /**
     * Returns the current volume
     *
     * @return the current volume
     */
    int getVolume();

    /**
     * Returns true if the MPD status contains an error. {@link #getError} to get the
     * error description.
     *
     * @return true if there is an error
     */
    boolean isError();

    /**
     * Returns if the playlist is repeating
     *
     * @return playlist repeating
     */
    boolean isRepeat();

    /**
     * Returns if the playlist is randomized
     *
     * @return true if playlist is random
     */
    boolean isRandom();

    /**
     * Returns if the database is currently updating
     *
     * @return true if the db is updating
     */
    boolean isDatabaseUpdating();

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

    /**
     * Returns if the player is in consuming mode. If true, each song played is removed from
     * playlist.
     *
     * @return true if the player is in consuming mode.
     */
    boolean isConsume();

    /**
     * Returns if the player plays a single track. If true, activated, playback is stopped after
     * current song, or song is repeated if the 'repeat' mode is enabled.
     *
     * @return true if the player plays a single track;
     */
    boolean isSingle();

    /**
     * Returns the playlist song number of the current song stopped on or playing
     *
     * @return the playlist song number of the current song stopped on or playing
     */
    Optional<Integer> playlistSongNumber();

    /**
     * Returns the playlist song id of the current song stopped on or playing, null if not known
     *
     * @return the playlist song id of the current song stopped on or playing, null if not known
     */
    Optional<String> playlistSongId();

    /**
     * The playlist song number of the next song to be played
     *
     * @return the playlist song number of the next song to be played
     */
    Optional<Integer> playlistNextSongNumber();

    /**
     * The playlist song id of the next song to be played
     *
     * @return the playlist song id of the next song to be played
     */
    Optional<String> playlistNextSongId();

    /**
     * Duration of the current song in seconds
     *
     * @return duration of the current song in seconds
     */
    Optional<Integer> durationCurrentSong();

    /**
     * Total time elapsed within the current song in seconds, but with higher resolution
     *
     * @return total time elapsed within the current song in seconds, but with higher resolution
     */
    Optional<Integer> elapsedCurrentSong();

    /**
     * The threshold at which songs will be overlapped. Like crossfading but doesn’t fade the track volume,
     * just overlaps.
     *
     * @return the threshold at which songs will be overlapped. Like crossfading but doesn’t fade the track volume,
     * just overlaps.
     */
    Optional<Integer> getMixRampDb();

    /**
     * Additional time subtracted from the overlap calculated by mixrampdb.
     *
     * @return additional time subtracted from the overlap calculated by mixrampdb
     */
    Optional<Integer> getMixRampDelay();
}
