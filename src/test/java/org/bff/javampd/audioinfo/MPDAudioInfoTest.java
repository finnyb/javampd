package org.bff.javampd.audioinfo;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class MPDAudioInfoTest {
    @Test
    void equalsContract() {
        EqualsVerifier.simple().forClass(MPDAudioInfo.class).verify();
    }
}
