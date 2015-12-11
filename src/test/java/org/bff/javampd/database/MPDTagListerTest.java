package org.bff.javampd.database;

import org.bff.javampd.command.MPDCommandExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDTagListerTest {

    @Mock
    private MPDCommandExecutor commandExecutor;
    @Mock
    private DatabaseProperties databaseProperties;

    @InjectMocks
    private MPDTagLister tagLister;

    @Before
    public void setUp() throws Exception {
        when(databaseProperties.getListInfo()).thenReturn("lsinfo");
        when(databaseProperties.getList()).thenReturn("list");
    }

    @Test
    public void testListInfoSingle() throws Exception {
        List<String> retList = new ArrayList<>();
        retList.add("playlist: 5");
        when(commandExecutor.sendCommand("lsinfo")).thenReturn(retList);
        List<String> infoList = new ArrayList<>(tagLister.listInfo(TagLister.ListInfoType.PLAYLIST));

        assertEquals(1, infoList.size());
        assertEquals("5", infoList.get(0));
    }

    @Test
    public void testListInfoDouble() throws Exception {
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
    public void testListInfoNone() throws Exception {
        List<String> retList = new ArrayList<>();
        retList.add("bogus: 5");
        when(databaseProperties.getListInfo()).thenReturn("lsinfo");
        when(commandExecutor.sendCommand("lsinfo")).thenReturn(retList);
        List<String> infoList = new ArrayList<>(tagLister.listInfo(TagLister.ListInfoType.PLAYLIST));

        assertEquals(0, infoList.size());
    }

    @Test
    public void testList() throws Exception {
        List<String> retList = new ArrayList<>();
        retList.add("album: 5");
        when(commandExecutor.sendCommand("list", "album")).thenReturn(retList);
        List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM));

        assertEquals(1, infoList.size());
        assertEquals("5", infoList.get(0));
    }

    @Test
    public void testListError() throws Exception {
        List<String> retList = new ArrayList<>();
        retList.add("");
        when(commandExecutor.sendCommand("list", "artist")).thenReturn(retList);

        List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ARTIST));

        assertEquals(1, infoList.size());
        assertEquals("", infoList.get(0));
    }

    @Test
    public void testListWithParam() throws Exception {
        List<String> retList = new ArrayList<>();
        retList.add("album: artist");

        List<String> params = new ArrayList<>();
        params.add("artist");

        when(commandExecutor.sendCommand("list", "album", "artist")).thenReturn(retList);
        List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM, params));

        assertEquals(1, infoList.size());
        assertEquals("artist", infoList.get(0));
    }
}