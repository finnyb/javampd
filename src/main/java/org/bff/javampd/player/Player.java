package org.bff.javampd.player;

import org.bff.javampd.audioinfo.MPDAudioInfo;
import org.bff.javampd.song.MPDSong;

/**
 * @author bill
 */
public interface Player {

    /**
     * The status of the player.
     */
    public enum Status {

        STATUS_STOPPED("stop"),
        STATUS_PLAYING("play"),
        STATUS_PAUSED("pause");

        private String prefix;

        Status(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return this.prefix;
        }
    }

    /**
     * Returns the current song either playing or queued for playing.
     *
     * @return the current song
     * @throws MPDPlayerException if the MPD responded with an error
     */
    MPDSong getCurrentSong() throws MPDPlayerException;

    /**
     * Adds a {@link PlayerChangeListener} to this object to receive
     * {@link PlayerChangeEvent}s.
     *
     * @param pcl the PlayerChangeListener to add
     */
    void addPlayerChangeListener(PlayerChangeListener pcl);

    /**
     * Removes a {@link PlayerChangeListener} from this object.
     *
     * @param pcl the PlayerChangeListener to remove
     */
    void removePlayerChangedListener(PlayerChangeListener pcl);

    /**
     * Adds a {@link VolumeChangeListener} to this object to receive
     * {@link VolumeChangeEvent}s.
     *
     * @param vcl the VolumeChangeListener to add
     */
    void addVolumeChangeListener(VolumeChangeListener vcl);

    /**
     * Removes a {@link VolumeChangeListener} from this object.
     *
     * @param vcl the VolumeChangeListener to remove
     */
    void removeVolumeChangedListener(VolumeChangeListener vcl);

    /**
     * Starts the player.
     *
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void play() throws MPDPlayerException;

    /**
     * Starts the player with the specified song.
     *
     * @param song the song to start the player with
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void playId(MPDSong song) throws MPDPlayerException;

    /**
     * Seeks to the desired location in the current song.  If the location is larger
     * than the length of the song or is less than 0 then the parameter is ignored.
     *
     * @param secs the location to seek to
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void seek(long secs) throws MPDPlayerException;

    /**
     * Seeks to the desired location in the specified song.  If the location is larger
     * than the length of the song or is less than 0 then the parameter is ignored.
     *
     * @param song the song to seek in
     * @param secs the location to seek to
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void seekId(MPDSong song, long secs) throws MPDPlayerException;

    /**
     * Stops the player.
     *
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void stop() throws MPDPlayerException;

    /**
     * Pauses the player.
     *
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void pause() throws MPDPlayerException;

    /**
     * Plays the next song in the playlist.
     *
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void playNext() throws MPDPlayerException;

    /**
     * Plays the previous song in the playlist.
     *
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void playPrev() throws MPDPlayerException;

    /**
     * Mutes the volume of the player.
     *
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void mute() throws MPDPlayerException;

    /**
     * Unmutes the volume of the player.
     *
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void unMute() throws MPDPlayerException;

    /**
     * Returns the instantaneous bitrate of the currently playing song.
     *
     * @return the instantaneous bitrate in kbps
     * @throws MPDPlayerException if the MPD responded with an error
     */
    int getBitrate() throws MPDPlayerException;

    /**
     * Returns the current volume of the player.
     *
     * @return the volume of the player (0-100)
     * @throws MPDPlayerException if the MPD responded with an error
     */
    int getVolume() throws MPDPlayerException;

    /**
     * Sets the volume of the player.  The volume is between 0 and 100, any volume less
     * that 0 results in a volume of 0 while any volume greater than 100 results in a
     * volume of 100.
     *
     * @param volume the volume level (0-100)
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void setVolume(int volume) throws MPDPlayerException;

    /**
     * Returns if the player is repeating.
     *
     * @return is the player repeating
     * @throws MPDPlayerException if the MPD responded with an error
     */
    boolean isRepeat() throws MPDPlayerException;

    /**
     * Sets the repeating status of the player.
     *
     * @param shouldRepeat should the player repeat the current song
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void setRepeat(boolean shouldRepeat) throws MPDPlayerException;

    /**
     * Returns if the player is in random play mode.
     *
     * @return true if the player is in random mode false otherwise
     * @throws MPDPlayerException if the MPD responded with an error
     */
    boolean isRandom() throws MPDPlayerException;

    /**
     * Sets the random status of the player. So the songs will be played in random order
     *
     * @param shouldRandom should the player play in random mode
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void setRandom(boolean shouldRandom) throws MPDPlayerException;

    /**
     * Plays the playlist in a random order.
     *
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void randomizePlay() throws MPDPlayerException;

    /**
     * Plays the playlist in order.
     *
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void unRandomizePlay() throws MPDPlayerException;

    /**
     * Returns the cross fade of the player in seconds.
     *
     * @return the cross fade of the player in seconds
     * @throws MPDPlayerException if the MPD responded with an error
     */
    int getXFade() throws MPDPlayerException;

    /**
     * Sets the cross fade of the player in seconds.
     *
     * @param xFade the amount of cross fade to set in seconds
     * @throws MPDPlayerException if the MPD responded with an error
     */
    void setXFade(int xFade) throws MPDPlayerException;

    /**
     * Returns the elapsed time of the current song in seconds.
     *
     * @return the elapsed time of the song in seconds
     * @throws MPDPlayerException if the MPD responded with an error
     */
    long getElapsedTime() throws MPDPlayerException;

    /**
     * Returns the total time of the current song in seconds.
     *
     * @return the elapsed time of the song in seconds
     * @throws MPDPlayerException if the MPD responded with an error
     */
    long getTotalTime() throws MPDPlayerException;

    /**
     * Returns the {@link org.bff.javampd.audioinfo.MPDAudioInfo} about the current status of the player.  If the status is unknown
     * {@code null} will be returned.  Any individual parameter that is not known will be a -1
     *
     * @return the sample rate
     * @throws MPDPlayerException if the MPD responded with an error
     */
    MPDAudioInfo getAudioDetails() throws MPDPlayerException;

    /**
     * Returns the current status of the player.
     *
     * @return the status of the player
     * @throws MPDPlayerException if the MPD responded with an error
     */
    Status getStatus() throws MPDPlayerException;
}
