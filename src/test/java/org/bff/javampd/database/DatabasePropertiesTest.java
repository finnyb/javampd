package org.bff.javampd.database;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatabasePropertiesTest {
    private DatabaseProperties databaseProperties;

    @Before
    public void before() {
        databaseProperties = new DatabaseProperties();
    }

    @Test
    public void getFind() throws Exception {
        assertEquals("find", databaseProperties.getFind());
    }

    @Test
    public void getList() throws Exception {
        assertEquals("list", databaseProperties.getList());
    }

    @Test
    public void getListInfo() throws Exception {
        assertEquals("lsinfo", databaseProperties.getListInfo());
    }

    @Test
    public void getSearch() throws Exception {
        assertEquals("search", databaseProperties.getSearch());
    }

    @Test
    public void getListSongs() throws Exception {
        assertEquals("listplaylist", databaseProperties.getListSongs());
    }

}