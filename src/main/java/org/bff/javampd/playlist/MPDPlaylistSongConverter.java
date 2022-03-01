package org.bff.javampd.playlist;

import lombok.extern.slf4j.Slf4j;
import org.bff.javampd.song.MPDTagConverter;
import org.bff.javampd.song.SongProcessor;

import java.util.*;

@Slf4j
public class MPDPlaylistSongConverter extends MPDTagConverter<MPDPlaylistSong> implements PlaylistSongConverter {

    @Override
    public List<MPDPlaylistSong> convertResponseToSongs(List<String> list) {
        return super.convertResponse(list);
    }

    @Override
    public MPDPlaylistSong createSong(Map<String, String> props) {
        return MPDPlaylistSong.builder()
                .file(props.get(SongProcessor.FILE.name()))
                .name(props.get(SongProcessor.NAME.name()) == null ?
                        props.get(SongProcessor.TITLE.name()) :
                        props.get(SongProcessor.NAME.name()))
                .title(props.get(SongProcessor.TITLE.name()))
                .albumArtist(props.get(SongProcessor.ALBUM_ARTIST.name()))
                .artistName(props.get(SongProcessor.ARTIST.name()))
                .genre(props.get(SongProcessor.GENRE.name()))
                .date(props.get(SongProcessor.DATE.name()))
                .comment(props.get(SongProcessor.COMMENT.name()))
                .discNumber(props.get(SongProcessor.DISC.name()))
                .albumName(props.get(SongProcessor.ALBUM.name()))
                .track(props.get(SongProcessor.TRACK.name()))
                .length(parseInt(props.get(SongProcessor.TIME.name())))
                .id(parseInt(props.get(SongProcessor.ID.name())))
                .position(parseInt(props.get(SongProcessor.POS.name())))
                .build();
    }
}
