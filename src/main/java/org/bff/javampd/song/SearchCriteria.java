package org.bff.javampd.song;

public record SearchCriteria(SongSearcher.ScopeType searchType, String criteria) {}
