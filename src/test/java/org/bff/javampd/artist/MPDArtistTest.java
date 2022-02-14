package org.bff.javampd.artist;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class MPDArtistTest {
    @Test
    void equalsContract() {
        EqualsVerifier.simple().forClass(MPDArtist.class).verify();
    }

}
