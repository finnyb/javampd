package org.bff.javampd.playlist;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MPDSavedPlaylistTest {
    @Test
    void equalsContract() {
        EqualsVerifier.simple().forClass(MPDSavedPlaylist.class).verify();
    }

}
