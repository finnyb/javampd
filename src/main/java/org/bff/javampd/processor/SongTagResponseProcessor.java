package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;

public interface SongTagResponseProcessor {
    /**
     * Returns the line prefix that delimits songs in the response list
     *
     * @return the prefix that breaks songs in the list
     */
    String getPrefix();

    /**
     * Process the response line and set the appropriate @{link MPDSong} property
     *
     * @param song the {@link MPDSong}
     * @param line the line to process
     */
    void processTag(MPDSong song, String line);
}
