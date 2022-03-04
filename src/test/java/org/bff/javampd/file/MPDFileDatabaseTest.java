package org.bff.javampd.file;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.bff.javampd.MPDException;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.DatabaseProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MPDFileDatabaseTest {

  @Mock private DatabaseProperties databaseProperties;
  @Mock private CommandExecutor commandExecutor;
  @InjectMocks private MPDFileDatabase fileDatabase;

  private DatabaseProperties realDatabaseProperties;

  @BeforeEach
  void setup() {
    realDatabaseProperties = new DatabaseProperties();
  }

  @Test
  void testListRootDirectory() {
    List<String> response = new ArrayList<>();
    response.add("directory: Q");
    response.add("Last-Modified: 2015-10-11T22:11:35Z");

    prepMockedCommand("", response);

    List<MPDFile> mpdFiles = new ArrayList<>(fileDatabase.listRootDirectory());
    assertEquals(1, mpdFiles.size());
    assertEquals("Q", mpdFiles.get(0).getPath());
    assertEquals(
        LocalDateTime.parse("2015-10-11T22:11:35Z", DateTimeFormatter.ISO_DATE_TIME),
        mpdFiles.get(0).getLastModified());
    assertTrue(mpdFiles.get(0).isDirectory());
  }

  @Test
  void testListDirectory() {
    String dir = "test";
    MPDFile file = MPDFile.builder(dir).build();
    file.setDirectory(true);
    fileDatabase.listDirectory(file);

    List<String> response = new ArrayList<>();
    response.add("directory: Q");
    response.add("Last-Modified: 2015-10-11T22:11:35Z");

    String listInfoCommand = realDatabaseProperties.getListInfo();
    when(databaseProperties.getListInfo()).thenReturn(listInfoCommand);
    when(commandExecutor.sendCommand(listInfoCommand, dir)).thenReturn(response);

    List<MPDFile> mpdFiles = new ArrayList<>(fileDatabase.listDirectory(file));
    assertEquals(1, mpdFiles.size());
    assertEquals("Q", mpdFiles.get(0).getPath());
    assertEquals(
        LocalDateTime.parse("2015-10-11T22:11:35Z", DateTimeFormatter.ISO_DATE_TIME),
        mpdFiles.get(0).getLastModified());
    assertTrue(mpdFiles.get(0).isDirectory());
  }

  @Test
  void testListDirectoryWithFiles() {
    String dir = "test";
    MPDFile file = MPDFile.builder(dir).build();
    file.setDirectory(true);
    fileDatabase.listDirectory(file);

    List<String> response = new ArrayList<>();
    response.add("file: Q");
    response.add("Last-Modified: 2015-10-11T22:11:35Z");

    prepMockedCommand(dir, response);

    List<MPDFile> mpdFiles = new ArrayList<>(fileDatabase.listDirectory(file));
    assertEquals(1, mpdFiles.size());
    assertEquals("Q", mpdFiles.get(0).getPath());
    assertEquals(
        LocalDateTime.parse("2015-10-11T22:11:35Z", DateTimeFormatter.ISO_DATE_TIME),
        mpdFiles.get(0).getLastModified());
    assertFalse(mpdFiles.get(0).isDirectory());
  }

  @Test
  void testListDirectoryWithMultipleFilesSize() {
    String dir = "test";
    MPDFile file = MPDFile.builder(dir).build();
    file.setDirectory(true);
    fileDatabase.listDirectory(file);

    List<String> response = createMultFileResponse();
    prepMockedCommand(dir, response);

    assertEquals(4, new ArrayList<>(fileDatabase.listDirectory(file)).size());
  }

  @Test
  void testListDirectoryWithMultipleFiles1() {
    String dir = "test";
    MPDFile file = MPDFile.builder(dir).build();
    file.setDirectory(true);
    fileDatabase.listDirectory(file);

    List<String> response = createMultFileResponse();
    prepMockedCommand(dir, response);

    List<MPDFile> mpdFiles = new ArrayList<>(fileDatabase.listDirectory(file));
    assertEquals("Q", mpdFiles.get(0).getPath());
    assertEquals(
        LocalDateTime.parse("2015-10-11T22:11:35Z", DateTimeFormatter.ISO_DATE_TIME),
        mpdFiles.get(0).getLastModified());
    assertTrue(mpdFiles.get(0).isDirectory());
  }

  @Test
  void testListDirectoryWithMultipleFiles2() {
    String dir = "test";
    MPDFile file = MPDFile.builder(dir).build();
    file.setDirectory(true);
    fileDatabase.listDirectory(file);

    List<String> response = createMultFileResponse();
    prepMockedCommand(dir, response);

    List<MPDFile> mpdFiles = new ArrayList<>(fileDatabase.listDirectory(file));
    assertEquals("R", mpdFiles.get(1).getPath());
    assertEquals(
        LocalDateTime.parse("2015-10-11T22:11:36Z", DateTimeFormatter.ISO_DATE_TIME),
        mpdFiles.get(1).getLastModified());
    assertFalse(mpdFiles.get(1).isDirectory());
  }

  @Test
  void testListDirectoryWithMultipleFiles3() {
    String dir = "test";
    MPDFile file = MPDFile.builder(dir).build();
    file.setDirectory(true);
    fileDatabase.listDirectory(file);

    List<String> response = createMultFileResponse();
    prepMockedCommand(dir, response);

    List<MPDFile> mpdFiles = new ArrayList<>(fileDatabase.listDirectory(file));
    assertEquals("S", mpdFiles.get(2).getPath());
    assertEquals(
        LocalDateTime.parse("2015-10-11T22:11:37Z", DateTimeFormatter.ISO_DATE_TIME),
        mpdFiles.get(2).getLastModified());
    assertFalse(mpdFiles.get(2).isDirectory());
  }

  @Test
  void testListDirectoryWithMultipleFiles4() {
    String dir = "test";
    MPDFile file = MPDFile.builder(dir).build();
    file.setDirectory(true);
    fileDatabase.listDirectory(file);

    List<String> response = createMultFileResponse();
    prepMockedCommand(dir, response);

    List<MPDFile> mpdFiles = new ArrayList<>(fileDatabase.listDirectory(file));

    assertEquals("T", mpdFiles.get(3).getPath());
    assertEquals(
        LocalDateTime.parse("2015-10-11T22:11:38Z", DateTimeFormatter.ISO_DATE_TIME),
        mpdFiles.get(3).getLastModified());
    assertTrue(mpdFiles.get(3).isDirectory());
  }

  @Test
  void testListDirectoryException() {
    MPDFile file = MPDFile.builder("").build();
    file.setDirectory(false);
    assertThrows(MPDException.class, () -> fileDatabase.listDirectory(file));
  }

  private List<String> createMultFileResponse() {
    List<String> response = new ArrayList<>();
    response.add("directory: Q");
    response.add("Last-Modified: 2015-10-11T22:11:35Z");
    response.add("file: R");
    response.add("Last-Modified: 2015-10-11T22:11:36Z");
    response.add("file: S");
    response.add("Last-Modified: 2015-10-11T22:11:37Z");
    response.add("directory: T");
    response.add("Last-Modified: 2015-10-11T22:11:38Z");

    return response;
  }

  private void prepMockedCommand(String file, List<String> response) {
    when(databaseProperties.getListInfo()).thenReturn(realDatabaseProperties.getListInfo());
    when(commandExecutor.sendCommand(realDatabaseProperties.getListInfo(), file))
        .thenReturn(response);
  }
}
