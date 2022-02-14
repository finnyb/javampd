package org.bff.javampd.art;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.bff.javampd.artist.MPDArtist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MPDArtworkTest {

    @Test
    void testGetBytes() {
        byte[] bytes = {
                0
        };

        MPDArtwork artwork = MPDArtwork.builder().name("name").path("path").build();
        artwork.setBytes(bytes);

        assertEquals(bytes, artwork.getBytes());
    }

    @Test
    void equalsContract() {
        EqualsVerifier.simple().forClass(MPDArtwork.class).verify();
    }
}
