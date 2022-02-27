package org.bff.javampd.playlist;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.bff.javampd.song.MPDSong;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class MPDPlaylistSong extends MPDSong {

    /**
     * Returns the position of the song in the playlist.
     */
    @Builder.Default
    private final int position = -1;

    /**
     * Returns the playlist song id for the song.
     */
    @Builder.Default
    private final int id = -1;
}
