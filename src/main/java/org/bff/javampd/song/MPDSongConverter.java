package org.bff.javampd.song;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MPDSongConverter extends MPDTagConverter<MPDSong> implements SongConverter {

    @Override
    public List<MPDSong> convertResponseToSongs(List<String> list) {
        return super.convertResponse(list);
    }

    @Override
    protected MPDSong createSong(Map<String, String> props) {
        return MPDSong.builder()
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
                .build();
    }

    @Override
    public List<String> getSongFileNameList(List<String> fileList) {
        var delimiter = SongProcessor.FILE.getProcessor().getPrefix().toLowerCase();
        return fileList.stream()
                .filter(s -> s.toLowerCase().startsWith(delimiter))
                .map(s -> (s.substring(delimiter.length())).trim())
                .collect(Collectors.toList());
    }
}
