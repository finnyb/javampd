package org.bff.javampd.artist;

import org.bff.javampd.database.TagLister;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MPDArtistDatabaseTest {
    @Mock
    private TagLister tagLister;

    @InjectMocks
    private MPDArtistDatabase artistDatabase;

    @Test
    public void testListAllArtists() throws Exception {

    }

    @Test
    public void testListArtistsByGenre() throws Exception {

    }
}