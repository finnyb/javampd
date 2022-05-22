package org.bff.javampd.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabasePropertiesTest {
  private DatabaseProperties databaseProperties;

  @BeforeEach
  void before() {
    databaseProperties = new DatabaseProperties();
  }

  @Test
  void getFind() {
    assertEquals("find", databaseProperties.getFind());
  }

  @Test
  void getList() {
    assertEquals("list", databaseProperties.getList());
  }

  @Test
  void getGroup() {
    assertEquals("group", databaseProperties.getGroup());
  }

  @Test
  void getListInfo() {
    assertEquals("lsinfo", databaseProperties.getListInfo());
  }

  @Test
  void getSearch() {
    assertEquals("search", databaseProperties.getSearch());
  }

  @Test
  void getListSongs() {
    assertEquals("listplaylist", databaseProperties.getListSongs());
  }
}
