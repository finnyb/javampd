package org.bff.javampd.song;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import org.bff.javampd.command.CommandExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MPDSongSearcherTest {

  private SongSearcher songSearcher;
  private CommandExecutor mockedCommandExecuter;
  private SearchProperties searchProperties;

  @Captor private ArgumentCaptor<String> commandArgumentCaptor;
  @Captor private ArgumentCaptor<String> paramArgumentCaptor;

  @BeforeEach
  void setup() {
    searchProperties = new SearchProperties();
    SongConverter mockedSongConverter = mock(SongConverter.class);
    mockedCommandExecuter = mock(CommandExecutor.class);
    songSearcher =
        new MPDSongSearcher(searchProperties, mockedCommandExecuter, mockedSongConverter);
  }

  @Test
  void searchAny() {
    var search = "test";
    when(mockedCommandExecuter.sendCommand(any(), anyString())).thenReturn(new ArrayList<>());

    this.songSearcher.searchAny(search);
    verify(mockedCommandExecuter)
        .sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

    assertAll(
        () -> assertEquals("search", commandArgumentCaptor.getValue()),
        () ->
            assertEquals("(any contains '%s')".formatted(search), paramArgumentCaptor.getValue()));
  }

  @Test
  void searchByScopeAndString() {
    var search = "test";
    when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

    this.songSearcher.search(SongSearcher.ScopeType.ALBUM, search);
    verify(mockedCommandExecuter)
        .sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

    assertAll(
        () -> assertEquals("search", commandArgumentCaptor.getValue()),
        () ->
            assertEquals(
                "(album contains '%s')".formatted(search), paramArgumentCaptor.getValue()));
  }

  @Test
  void searchBySingleCriteria() {
    var search = "test";
    when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

    this.songSearcher.search(new SearchCriteria(SongSearcher.ScopeType.TITLE, search));
    verify(mockedCommandExecuter)
        .sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

    assertAll(
        () -> assertEquals("search", commandArgumentCaptor.getValue()),
        () ->
            assertEquals(
                "(title contains '%s')".formatted(search), paramArgumentCaptor.getValue()));
  }

  @Test
  void searchByMultipleCriteria() {
    var searchArtist = "Tool";
    var searchTitle = "Vic";

    when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

    this.songSearcher.search(
        new SearchCriteria(SongSearcher.ScopeType.TITLE, searchTitle),
        new SearchCriteria(SongSearcher.ScopeType.ARTIST, searchArtist));

    verify(mockedCommandExecuter)
        .sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

    assertAll(
        () -> assertEquals("search", commandArgumentCaptor.getValue()),
        () ->
            assertEquals(
                "((title contains '%s') AND (artist contains '%s'))"
                    .formatted(searchTitle, searchArtist),
                paramArgumentCaptor.getValue()));
  }

  @Test
  void findAny() {
    var find = "test";
    when(mockedCommandExecuter.sendCommand(
            searchProperties.getFind(), generateParams(SongSearcher.ScopeType.ANY, find)))
        .thenReturn(new ArrayList<>());

    this.songSearcher.findAny(find);
    verify(mockedCommandExecuter)
        .sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

    assertAll(
        () -> assertEquals("find", commandArgumentCaptor.getValue()),
        () -> assertEquals("(any == '%s')".formatted(find), paramArgumentCaptor.getValue()));
  }

  @Test
  void findByScopeAndString() {
    var find = "test";
    when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

    this.songSearcher.find(SongSearcher.ScopeType.ALBUM, find);
    verify(mockedCommandExecuter)
        .sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

    assertAll(
        () -> assertEquals("find", commandArgumentCaptor.getValue()),
        () -> assertEquals("(album == '%s')".formatted(find), paramArgumentCaptor.getValue()));
  }

  @Test
  void findBySingleCriteria() {
    var find = "test";
    when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

    this.songSearcher.find(new SearchCriteria(SongSearcher.ScopeType.TITLE, find));
    verify(mockedCommandExecuter)
        .sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

    assertAll(
        () -> assertEquals("find", commandArgumentCaptor.getValue()),
        () -> assertEquals("(title == '%s')".formatted(find), paramArgumentCaptor.getValue()));
  }

  @Test
  void findByMultipleCriteria() {
    var findArtist = "Tool";
    var findTitle = "Vicarious";

    when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

    this.songSearcher.find(
        new SearchCriteria(SongSearcher.ScopeType.TITLE, findTitle),
        new SearchCriteria(SongSearcher.ScopeType.ARTIST, findArtist));

    verify(mockedCommandExecuter)
        .sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

    assertAll(
        () -> assertEquals("find", commandArgumentCaptor.getValue()),
        () ->
            assertEquals(
                "((title == '%s') AND (artist == '%s'))".formatted(findTitle, findArtist),
                paramArgumentCaptor.getValue()));
  }

  @Test
  void testFindEscapeSingleQuote() {
    var find = "I Ain't Mad at Cha";
    when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

    this.songSearcher.find(new SearchCriteria(SongSearcher.ScopeType.TITLE, find));
    verify(mockedCommandExecuter)
        .sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());
    assertAll(
        () -> assertEquals("find", commandArgumentCaptor.getValue()),
        () ->
            assertThat(
                paramArgumentCaptor.getValue(),
                is(equalTo("(title == 'I Ain\\\\'t Mad at Cha')"))));
  }

  @Test
  void testSearchEscapeSingleQuote() {
    var search = "Mama's Just a Little Girl";
    when(mockedCommandExecuter.sendCommand(anyString(), anyString())).thenReturn(new ArrayList<>());

    this.songSearcher.search(new SearchCriteria(SongSearcher.ScopeType.TITLE, search));
    verify(mockedCommandExecuter)
        .sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());
    assertAll(
        () -> assertThat(commandArgumentCaptor.getValue(), is(equalTo("search"))),
        () ->
            assertThat(
                paramArgumentCaptor.getValue(),
                is(equalTo("(title contains 'Mama\\\\'s Just a Little Girl')"))));
  }

  private String generateParams(SongSearcher.ScopeType scopeType, String criteria) {
    return "(%s == '%s')".formatted(scopeType.getType(), criteria);
  }
}
