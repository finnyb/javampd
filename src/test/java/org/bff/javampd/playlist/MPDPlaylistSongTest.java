package org.bff.javampd.playlist;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class MPDPlaylistSongTest {
    @Test
    void equalsContract() {
        EqualsVerifier.simple().forClass(MPDSavedPlaylist.class).verify();
    }
}
