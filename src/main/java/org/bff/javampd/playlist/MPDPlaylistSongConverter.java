package org.bff.javampd.playlist;

import lombok.extern.slf4j.Slf4j;
import org.bff.javampd.song.SongProcessor;

import java.util.*;

@Slf4j
public class MPDPlaylistSongConverter implements PlaylistSongConverter {

    private static final String DELIMITING_PREFIX = SongProcessor.getDelimitingPrefix();
    private static final String TERMINATION = SongProcessor.getTermination();

    @Override
    public List<MPDPlaylistSong> convertResponseToSongs(List<String> list) {
        var songList = new ArrayList<MPDPlaylistSong>();
        var iterator = list.iterator();

        String line = null;
        while (iterator.hasNext()) {
            if ((!isLineDelimiter(line))) {
                line = iterator.next();
            }

            if (line != null && isLineDelimiter(line)) {
                line = processSong(
                        line.substring(DELIMITING_PREFIX.length()).trim(),
                        iterator,
                        songList);
            }
        }

        return songList;
    }

    protected String processSong(String fileName,
                                 Iterator<String> iterator,
                                 List<MPDPlaylistSong> songList) {
        var props = new HashMap<String, String>();
        props.put(SongProcessor.FILE.name(), fileName);

        String line = null;
        if (iterator.hasNext()) {
            line = iterator.next();
        }

        while (!isStreamTerminated(line) && !isLineDelimiter(line)) {
            var processor = SongProcessor.lookup(line);
            if (processor != null) {
                var tag = processor.getProcessor().processTag(line);
                props.put(processor.name(), tag);
            } else {
                log.warn("Processor not found - {}", line);
            }

            if (iterator.hasNext()) {
                line = iterator.next();
            } else {
                break;
            }
        }
        songList.add(createPlaylistSong(props));

        return line;
    }

    private MPDPlaylistSong createPlaylistSong(Map<String, String> props) {
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

    protected boolean isLineDelimiter(String line) {
        return line != null && line.toLowerCase().startsWith(DELIMITING_PREFIX.toLowerCase());
    }

    private boolean isStreamTerminated(String line) {
        return line == null ||
                TERMINATION.equalsIgnoreCase(line) ||
                isLineDelimiter(line);
    }

    private int parseInt(String tag) {
        try {
            return Integer.parseInt(tag);
        } catch (NumberFormatException e) {
            log.warn(String.format("Unable to convert tag to Integer: %s", tag));
            return -1;
        }
    }
}
