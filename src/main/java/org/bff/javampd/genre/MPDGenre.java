package org.bff.javampd.genre;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * MPDGenre represents a genre
 *
 * @author Bill
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class MPDGenre {
  private final String name;
}
