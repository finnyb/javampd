package org.bff.javampd.database;

import org.bff.javampd.command.MPDCommandExecutor;
import org.bff.javampd.properties.DatabaseProperties;
import org.bff.javampd.server.MPDResponseException;
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
    private MPDCommandExecutor mpdCommandExecutor;

    @Mock
    private DatabaseProperties databaseProperties;

    @InjectMocks
    private MPDTagLister tagLister;

    @Test
    public void testListInfoPlaylist() throws Exception {
        String playlist = "playlist";

        List<String> infoList = new ArrayList<>();
        infoList.add(TagLister.ListInfoType.PLAYLIST.getPrefix() + playlist);

        when(databaseProperties.getListInfo()).thenReturn(new DatabaseProperties().getListInfo());
        when(mpdCommandExecutor.sendCommand(databaseProperties.getListInfo()))
                .thenReturn(infoList);

        List<String> tags = new ArrayList<>(tagLister.listInfo(TagLister.ListInfoType.PLAYLIST));
        assertEquals(playlist, tags.get(0));
    }

    @Test
    public void testListInfoDirectory() throws Exception {
        String directory = "directory";

        List<String> infoList = new ArrayList<>();
        infoList.add(TagLister.ListInfoType.DIRECTORY.getPrefix() + directory);

        when(databaseProperties.getListInfo()).thenReturn(new DatabaseProperties().getListInfo());
        when(mpdCommandExecutor.sendCommand(databaseProperties.getListInfo()))
                .thenReturn(infoList);

        List<String> tags = new ArrayList<>(tagLister.listInfo(TagLister.ListInfoType.DIRECTORY));
        assertEquals(directory, tags.get(0));
    }

    @Test
    public void testListInfoFile() throws Exception {
        String file = "file";

        List<String> infoList = new ArrayList<>();
        infoList.add(TagLister.ListInfoType.FILE.getPrefix() + file);

        when(databaseProperties.getListInfo()).thenReturn(new DatabaseProperties().getListInfo());
        when(mpdCommandExecutor.sendCommand(databaseProperties.getListInfo()))
                .thenReturn(infoList);

        List<String> tags = new ArrayList<>(tagLister.listInfo(TagLister.ListInfoType.FILE));
        assertEquals(file, tags.get(0));
    }

    @Test
    public void testListInfoAll() throws Exception {
        String playlist = "playlist";
        String directory = "directory";
        String file = "file";

        List<String> infoList = new ArrayList<>();
        infoList.add(TagLister.ListInfoType.PLAYLIST.getPrefix() + playlist);
        infoList.add(TagLister.ListInfoType.DIRECTORY.getPrefix() + directory);
        infoList.add(TagLister.ListInfoType.FILE.getPrefix() + file);

        when(databaseProperties.getListInfo()).thenReturn(new DatabaseProperties().getListInfo());
        when(mpdCommandExecutor.sendCommand(databaseProperties.getListInfo()))
                .thenReturn(infoList);

        List<String> tags = new ArrayList<>(tagLister
                .listInfo(TagLister.ListInfoType.PLAYLIST,
                        TagLister.ListInfoType.DIRECTORY,
                        TagLister.ListInfoType.FILE));
        assertEquals(playlist, tags.get(0));
        assertEquals(directory, tags.get(1));
        assertEquals(file, tags.get(2));
    }

    @Test
    public void testListAlbum() throws Exception {
        String album = "album";

        List<String> infoList = new ArrayList<>();
        infoList.add("Album: " + album);

        when(databaseProperties.getList()).thenReturn(new DatabaseProperties().getList());

        when(mpdCommandExecutor.sendCommand(databaseProperties.getList(),
                new String[]{album}))
                .thenReturn(infoList);

        List<String> tags = new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM));
        assertEquals(album, tags.get(0));
    }


    @Test
    public void testListArtist() throws Exception {
        String artist = "artist";

        List<String> infoList = new ArrayList<>();
        infoList.add("Artist: " + artist);

        when(databaseProperties.getList()).thenReturn(new DatabaseProperties().getList());

        when(mpdCommandExecutor.sendCommand(databaseProperties.getList(),
                new String[]{artist}))
                .thenReturn(infoList);

        List<String> tags = new ArrayList<>(tagLister.list(TagLister.ListType.ARTIST));
        assertEquals(artist, tags.get(0));
    }


    @Test
    public void testListDate() throws Exception {
        String date = "date";

        List<String> infoList = new ArrayList<>();
        infoList.add("DATE: " + date);

        when(databaseProperties.getList()).thenReturn(new DatabaseProperties().getList());

        when(mpdCommandExecutor.sendCommand(databaseProperties.getList(),
                new String[]{date}))
                .thenReturn(infoList);

        List<String> tags = new ArrayList<>(tagLister.list(TagLister.ListType.DATE));
        assertEquals(date, tags.get(0));
    }


    @Test
    public void testListGenre() throws Exception {
        String genre = "genre";

        List<String> infoList = new ArrayList<>();
        infoList.add("Album: " + genre);

        when(databaseProperties.getList()).thenReturn(new DatabaseProperties().getList());

        when(mpdCommandExecutor.sendCommand(databaseProperties.getList(),
                new String[]{genre}))
                .thenReturn(infoList);

        List<String> tags = new ArrayList<>(tagLister.list(TagLister.ListType.GENRE));
        assertEquals(genre, tags.get(0));
    }

    @Test(expected = MPDDatabaseException.class)
    public void testListWithResponseException() throws Exception {
        String genre = "genre";

        List<String> infoList = new ArrayList<>();
        infoList.add("Album: " + genre);

        when(databaseProperties.getList()).thenReturn(new DatabaseProperties().getList());

        when(mpdCommandExecutor.sendCommand(databaseProperties.getList(),
                new String[]{genre}))
                .thenThrow(new MPDResponseException("oops"));

        tagLister.list(TagLister.ListType.GENRE);
    }

    @Test(expected = MPDDatabaseException.class)
    public void testListInfoWithResponseException() throws Exception {
        String file = "file";

        List<String> infoList = new ArrayList<>();
        infoList.add(TagLister.ListInfoType.FILE.getPrefix() + file);

        when(databaseProperties.getListInfo()).thenReturn(new DatabaseProperties().getListInfo());
        when(mpdCommandExecutor.sendCommand(databaseProperties.getListInfo()))
                .thenThrow(new MPDResponseException("oops"));

        tagLister.listInfo(TagLister.ListInfoType.FILE);
    }

    @Test(expected = MPDDatabaseException.class)
    public void testListWithRuntimeException() throws Exception {
        String genre = "genre";

        List<String> infoList = new ArrayList<>();
        infoList.add("Album: " + genre);

        when(databaseProperties.getList()).thenReturn(new DatabaseProperties().getList());

        when(mpdCommandExecutor.sendCommand(databaseProperties.getList(),
                new String[]{genre}))
                .thenThrow(new RuntimeException("oops"));

        tagLister.list(TagLister.ListType.GENRE);
    }

    @Test(expected = MPDDatabaseException.class)
    public void testListInfoWithRuntimeException() throws Exception {
        String file = "file";

        List<String> infoList = new ArrayList<>();
        infoList.add(TagLister.ListInfoType.FILE.getPrefix() + file);

        when(databaseProperties.getListInfo()).thenReturn(new DatabaseProperties().getListInfo());
        when(mpdCommandExecutor.sendCommand(databaseProperties.getListInfo()))
                .thenThrow(new RuntimeException("oops"));

        tagLister.listInfo(TagLister.ListInfoType.FILE);
    }
}