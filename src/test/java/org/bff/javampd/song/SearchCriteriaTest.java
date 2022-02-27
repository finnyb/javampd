package org.bff.javampd.song;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class SearchCriteriaTest {
    @Test
    void testEqualsAndHash() {
        EqualsVerifier.simple().forClass(SearchCriteria.class).verify();
    }
}
