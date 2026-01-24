package org.bff.javampd.playlist;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.DatabaseProperties;
import org.bff.javampd.database.MusicDatabase;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.server.MPD;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongConverter;
import org.bff.javampd.song.SongDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MPDPlaylistDatabase represents a playlist database to a {@link MPD}. To obtain an instance of the
 * class you must use the {@link MusicDatabase#getPlaylistDatabase()} method from the {@link MPD}
 * connection class.
 *
 * @author Bill
 */
public class MPDPlaylistDatabase implements PlaylistDatabase {

  public static final Pattern RADIO_STREAM_PAT = Pattern.compile("http.+");

  private final SongDatabase songDatabase;

  private final CommandExecutor commandExecutor;

  private final DatabaseProperties databaseProperties;

  private final TagLister tagLister;

  private final SongConverter songConverter;

  private static final Logger LOGGER = LoggerFactory.getLogger(MPDPlaylistDatabase.class);

  @Inject
  public MPDPlaylistDatabase(
      SongDatabase songDatabase,
      CommandExecutor commandExecutor,
      DatabaseProperties databaseProperties,
      TagLister tagLister,
      SongConverter songConverter) {
    this.songDatabase = songDatabase;
    this.commandExecutor = commandExecutor;
    this.databaseProperties = databaseProperties;
    this.tagLister = tagLister;
    this.songConverter = songConverter;
  }

  @Override
  public Collection<MPDSavedPlaylist> listSavedPlaylists() {
    List<MPDSavedPlaylist> playlists = new ArrayList<>();

    listPlaylists()
        .forEach(
            s -> {
              MPDSavedPlaylist playlist = MPDSavedPlaylist.builder(s).build();
              playlist.setSongs(listPlaylistSongs(s));
              playlists.add(playlist);
            });
    return playlists;
  }

  @Override
  public Collection<String> listPlaylists() {
    return tagLister.listInfo(TagLister.ListInfoType.PLAYLIST);
  }

  @Override
  public Collection<MPDSong> listPlaylistSongs(String playlistName) {
    return listPlaylistSongs(playlistName, 1, Long.MAX_VALUE);
  }

  @Override
  public Collection<MPDSong> listPlaylistSongs(String playlistName, long start, long count) {
    List<String> response =
        commandExecutor.sendCommand(databaseProperties.getListSongs(), playlistName);
    List<MPDSong> radioStreams = getWebRadioStreams(response);
    List<MPDSong> songList = fileNameToMPDSong(response, start, count - radioStreams.size());

    // Handle web radio streams
    songList.addAll(radioStreams);

    return songList;
  }

  @Override
  public Collection<String> listRawPlaylistSongs(String playlistName) {
    return commandExecutor.sendCommand(databaseProperties.getListSongs(), playlistName);
  }

  @Override
  public int countPlaylistSongs(String playlistName) {
    List<String> response =
        commandExecutor.sendCommand(databaseProperties.getListSongs(), playlistName);

    return response.size();
  }

  private List<MPDSong> fileNameToMPDSong(List<String> response, long start, long end) {
    return songConverter.getSongFileNameList(response).stream()
        .skip(start - 1)
        .limit(end)
        .map(
            song -> {
              Optional<MPDSong> mpdSong = Optional.empty();
              try {
                mpdSong =
                    Optional.of(new ArrayList<>(songDatabase.searchFileName(song)).getFirst());
              } catch (IndexOutOfBoundsException e) {
                LOGGER.error("Could not find file: {}", song);
              }
              return mpdSong;
            })
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  private List<MPDSong> getWebRadioStreams(List<String> response) {
    return songConverter.getSongFileNameList(response).stream()
        .filter(stream -> RADIO_STREAM_PAT.matcher(stream.toLowerCase()).matches())
        .<MPDSong>map(song -> MPDPlaylistSong.builder().file(song).title(song).build())
        .toList();
  }
}
