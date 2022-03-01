package org.bff.javampd.song;

import lombok.Data;

@Data
public class SearchCriteria {
    private final SongSearcher.ScopeType searchType;
    private final String criteria;
}
