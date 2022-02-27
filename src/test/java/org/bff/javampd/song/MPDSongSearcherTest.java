package org.bff.javampd.song;

import org.bff.javampd.command.CommandExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MPDSongSearcherTest {

    private SongSearcher songSearcher;
    private CommandExecutor mockedCommandExecuter;
    private SongConverter mockedSongConverter;
    private SearchProperties searchProperties;

    @Captor
    private ArgumentCaptor<String> commandArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> paramArgumentCaptor;

    @BeforeEach
    void setup() {
        searchProperties = new SearchProperties();
        mockedSongConverter = mock(SongConverter.class);
        mockedCommandExecuter = mock(CommandExecutor.class);
        songSearcher = new MPDSongSearcher(searchProperties,
                mockedCommandExecuter,
                mockedSongConverter);
    }

    @Test
    void searchAny() {
        var search = "test";
        when(mockedCommandExecuter.sendCommand(any(), anyString())).thenReturn(new ArrayList<>());

        this.songSearcher.searchAny(search);
        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(),
                paramArgumentCaptor.capture());

        assertAll(
                () -> assertEquals("search", commandArgumentCaptor.getValue()),
                () -> assertEquals(String.format("(any contains '%s')", search), paramArgumentCaptor.getValue())
        );
    }

    @Test
    void searchByScopeAndString() {
        var search = "test";
        when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

        this.songSearcher.search(SongSearcher.ScopeType.ALBUM, search);
        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(),
                paramArgumentCaptor.capture());

        assertAll(
                () -> assertEquals("search", commandArgumentCaptor.getValue()),
                () -> assertEquals(String.format("(album contains '%s')", search), paramArgumentCaptor.getValue())
        );
    }

    @Test
    void searchBySingleCriteria() {
        var search = "test";
        when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

        this.songSearcher.search(new SearchCriteria(SongSearcher.ScopeType.TITLE, search));
        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(),
                paramArgumentCaptor.capture());

        assertAll(
                () -> assertEquals("search", commandArgumentCaptor.getValue()),
                () -> assertEquals(String.format("(title contains '%s')", search), paramArgumentCaptor.getValue())
        );
    }

    @Test
    void searchByMultipleCriteria() {
        var searchArtist = "Tool";
        var searchTitle = "Vic";

        when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

        this.songSearcher.search(
                new SearchCriteria(SongSearcher.ScopeType.TITLE, searchTitle),
                new SearchCriteria(SongSearcher.ScopeType.ARTIST, searchArtist)
        );

        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(),
                paramArgumentCaptor.capture());

        assertAll(
                () -> assertEquals("search", commandArgumentCaptor.getValue()),
                () -> assertEquals(String.format("((title contains '%s') AND (artist contains '%s'))", searchTitle, searchArtist),
                        paramArgumentCaptor.getValue())
        );
    }

    @Test
    void findAny() {
        var find = "test";
        when(mockedCommandExecuter.sendCommand(searchProperties.getFind(),
                generateParams(SongSearcher.ScopeType.ANY, find))).thenReturn(new ArrayList<>());

        this.songSearcher.findAny(find);
        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(),
                paramArgumentCaptor.capture());

        assertAll(
                () -> assertEquals("find", commandArgumentCaptor.getValue()),
                () -> assertEquals(String.format("(any == '%s')", find), paramArgumentCaptor.getValue())
        );
    }

    @Test
    void findByScopeAndString() {
        var find = "test";
        when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

        this.songSearcher.find(SongSearcher.ScopeType.ALBUM, find);
        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(),
                paramArgumentCaptor.capture());

        assertAll(
                () -> assertEquals("find", commandArgumentCaptor.getValue()),
                () -> assertEquals(String.format("(album == '%s')", find), paramArgumentCaptor.getValue())
        );
    }

    @Test
    void findBySingleCriteria() {
        var find = "test";
        when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

        this.songSearcher.find(new SearchCriteria(SongSearcher.ScopeType.TITLE, find));
        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(),
                paramArgumentCaptor.capture());

        assertAll(
                () -> assertEquals("find", commandArgumentCaptor.getValue()),
                () -> assertEquals(String.format("(title == '%s')", find), paramArgumentCaptor.getValue())
        );
    }

    @Test
    void findByMultipleCriteria() {
        var findArtist = "Tool";
        var findTitle = "Vicarious";

        when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

        this.songSearcher.find(
                new SearchCriteria(SongSearcher.ScopeType.TITLE, findTitle),
                new SearchCriteria(SongSearcher.ScopeType.ARTIST, findArtist)
        );

        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(),
                paramArgumentCaptor.capture());

        assertAll(
                () -> assertEquals("find", commandArgumentCaptor.getValue()),
                () -> assertEquals(String.format("((title == '%s') AND (artist == '%s'))", findTitle, findArtist),
                        paramArgumentCaptor.getValue())
        );
    }


    private String generateParams(SongSearcher.ScopeType scopeType,
                                  String criteria) {
        return String.format("(%s == '%s')", scopeType.getType(), criteria);
    }
}
