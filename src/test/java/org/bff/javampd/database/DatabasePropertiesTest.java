package org.bff.javampd.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabasePropertiesTest {
  private DatabaseProperties databaseProperties;

  @BeforeEach
  public void before() {
    databaseProperties = new DatabaseProperties();
  }

  @Test
  public void getFind() {
    assertEquals("find", databaseProperties.getFind());
  }

  @Test
  public void getList() {
    assertEquals("list", databaseProperties.getList());
  }

  @Test
  public void getGroup() {
    assertEquals("group", databaseProperties.getGroup());
  }

  @Test
  public void getListInfo() {
    assertEquals("lsinfo", databaseProperties.getListInfo());
  }

  @Test
  public void getSearch() {
    assertEquals("search", databaseProperties.getSearch());
  }

  @Test
  public void getListSongs() {
    assertEquals("listplaylist", databaseProperties.getListSongs());
  }
}
