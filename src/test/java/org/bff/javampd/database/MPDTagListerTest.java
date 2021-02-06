package org.bff.javampd.database;

import org.bff.javampd.command.MPDCommandExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MPDTagListerTest {

    @Mock
    private MPDCommandExecutor commandExecutor;
    @Mock
    private DatabaseProperties databaseProperties;

    @InjectMocks
    private MPDTagLister tagLister;

    @Test
    void testListInfoSingle() {
        List<String> retList = new ArrayList<>();
        retList.add("playlist: 5");
        when(commandExecutor.sendCommand("lsinfo")).thenReturn(retList);
        when(databaseProperties.getListInfo()).thenReturn("lsinfo");

        List<String> infoList = new ArrayList<>(tagLister.listInfo(TagLister.ListInfoType.PLAYLIST));

        assertEquals(1, infoList.size());
        assertEquals("5", infoList.get(0));
    }

    @Test
    void testListInfoDouble() {
        List<String> retList = new ArrayList<>();
        retList.add("playlist: 5");
        retList.add("directory: 6");
        when(databaseProperties.getListInfo()).thenReturn("lsinfo");
        when(commandExecutor.sendCommand("lsinfo")).thenReturn(retList);
        List<String> infoList = new ArrayList<>(
                tagLister.listInfo(TagLister.ListInfoType.PLAYLIST,
                        TagLister.ListInfoType.DIRECTORY));

        assertEquals(2, infoList.size());
        assertEquals("5", infoList.get(0));
        assertEquals("6", infoList.get(1));
    }

    @Test
    void testListInfoNone() {
        List<String> retList = new ArrayList<>();
        retList.add("bogus: 5");
        when(databaseProperties.getListInfo()).thenReturn("lsinfo");
        when(commandExecutor.sendCommand("lsinfo")).thenReturn(retList);
        List<String> infoList = new ArrayList<>(tagLister.listInfo(TagLister.ListInfoType.PLAYLIST));

        assertEquals(0, infoList.size());
    }

    @Test
    void testList() {
        String testAlbumResponse = "album: 5";
        List<String> retList = new ArrayList<>();
        retList.add(testAlbumResponse);
        when(commandExecutor.sendCommand("list", "album")).thenReturn(retList);
        when(databaseProperties.getList()).thenReturn("list");
        List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM));

        assertEquals(1, infoList.size());
        assertEquals(testAlbumResponse, infoList.get(0));
    }

    @Test
    void testListGroupArtist() {
        List<String> retList = new ArrayList<>();
        retList.add("album: testAlbum");
        retList.add("artist: testArtist");
        when(commandExecutor.sendCommand("list", "album", "group", "artist")).thenReturn(retList);
        when(databaseProperties.getList()).thenReturn("list");
        when(databaseProperties.getGroup()).thenReturn("group");
        List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM, TagLister.GroupType.ARTIST));

        assertEquals(2, infoList.size());
        assertEquals("album: testAlbum", infoList.get(0));
        assertEquals("artist: testArtist", infoList.get(1));
    }

    @Test
    void testListGroupDate() {
        List<String> retList = new ArrayList<>();
        retList.add("album: testAlbum");
        retList.add("date: testDate");
        when(commandExecutor.sendCommand("list", "album", "group", "date")).thenReturn(retList);
        when(databaseProperties.getList()).thenReturn("list");
        when(databaseProperties.getGroup()).thenReturn("group");

        List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM, TagLister.GroupType.DATE));

        assertEquals(2, infoList.size());
        assertEquals("album: testAlbum", infoList.get(0));
        assertEquals("date: testDate", infoList.get(1));
    }

    @Test
    void testListGroupGenre() {
        List<String> retList = new ArrayList<>();
        retList.add("album: testAlbum");
        retList.add("genre: testGenre");
        when(commandExecutor.sendCommand("list", "album", "group", "genre")).thenReturn(retList);
        when(databaseProperties.getList()).thenReturn("list");
        when(databaseProperties.getGroup()).thenReturn("group");

        List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM, TagLister.GroupType.GENRE));

        assertEquals(2, infoList.size());
        assertEquals("album: testAlbum", infoList.get(0));
        assertEquals("genre: testGenre", infoList.get(1));
    }

    @Test
    void testListError() {
        List<String> retList = new ArrayList<>();
        retList.add("");
        when(commandExecutor.sendCommand("list", "artist")).thenReturn(retList);
        when(databaseProperties.getList()).thenReturn("list");

        List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ARTIST));

        assertEquals(1, infoList.size());
        assertEquals("", infoList.get(0));
    }

    @Test
    void testListWithParam() {
        String testResponse = "album: artist";
        List<String> retList = new ArrayList<>();
        retList.add(testResponse);

        List<String> params = new ArrayList<>();
        params.add("artist");

        when(commandExecutor.sendCommand("list", "album", "artist")).thenReturn(retList);
        when(databaseProperties.getList()).thenReturn("list");

        List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM, params));

        assertEquals(1, infoList.size());
        assertEquals(testResponse, infoList.get(0));
    }

    @Test
    void testListWithParamAndGroups() {
        String testResponse = "album: artist";
        List<String> retList = new ArrayList<>();
        retList.add(testResponse);

        List<String> params = new ArrayList<>();
        params.add("artist");

        when(commandExecutor.sendCommand("list",
                "album",
                "artist",
                "group",
                TagLister.GroupType.ARTIST.getType()))
                .thenReturn(retList);
        when(databaseProperties.getList()).thenReturn("list");
        when(databaseProperties.getGroup()).thenReturn("group");

        List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM, params, TagLister.GroupType.ARTIST));

        assertEquals(1, infoList.size());
        assertEquals(testResponse, infoList.get(0));
    }
}
