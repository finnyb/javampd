# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build and run all tests
mvn clean verify

# Run tests only
mvn test

# Run a single test class
mvn test -Dtest=ClassName

# Run a single test method
mvn test -Dtest=ClassName#methodName

# Format code (required before committing)
mvn spotless:apply

# Check formatting without applying
mvn spotless:check

# Build without tests
mvn clean install -DskipTests
```

Code formatting is enforced via Spotless (Google Java Format). `mvn spotless:check` runs automatically during `verify` and will fail the build if code is not formatted. Always run `mvn spotless:apply` before committing.

## Architecture

JavaMPD is a Java client library for the [MPD (Music Player Daemon)](https://www.musicpd.org/) protocol. It communicates with a running MPD server over TCP.

### Entry Point

`MPD` (`server/MPD.java`) is the main class users instantiate via builder:

```java
MPD mpd = MPD.builder().server("localhost").port(6600).build();
```

On construction, `MPD` initializes three Guice modules that wire together all components.

### Dependency Injection

Three Guice modules handle all bindings:

- **`MPDModule`** — core components: Player, Playlist, Admin, CommandExecutor, ServerStatus, converters
- **`MPDDatabaseModule`** — database/query layer: ArtistDatabase, AlbumDatabase, SongDatabase, GenreDatabase, SongSearcher, etc.
- **`MPDMonitorModule`** — event monitoring: TrackMonitor, VolumeMonitor, PlaylistMonitor, ConnectionMonitor, etc.

### Communication Layer

All MPD protocol commands are defined in `src/main/resources/mpd.properties`. `MPDCommandExecutor` sends commands via `MPDSocket` (TCP). The `CommandExecutor` interface is a Guice singleton injected wherever commands need to be sent.

### Domain Packages

Each domain area follows a consistent pattern: an interface, an `MPDX` implementation, a domain model, and often a converter and/or database class:

| Package | Responsibility |
|---------|----------------|
| `command` | Command objects and execution over the TCP socket |
| `server` | Connection, authentication, server status/properties |
| `player` | Playback control (play, pause, seek, volume, crossfade) |
| `playlist` | Playlist CRUD (add, remove, save, load) |
| `song` | Song model, search (`SearchCriteria`), database queries |
| `album` | Album model, album art, database queries |
| `artist` | Artist model, artist queries |
| `genre` | Genre model, genre queries |
| `database` | `MusicDatabase` facade + `MPDTagLister` for raw tag queries |
| `processor` | `TagResponseProcessor` implementations that parse MPD protocol tag responses |
| `monitor` | Event listener system for state changes (track, volume, connection, errors) |
| `art` | Artwork finding from the filesystem |
| `admin` | Server administration (update, rescan, kill, outputs) |
| `statistics` | Server statistics (uptime, counts) |

### Converters and Processors

MPD returns raw text responses. `TagResponseProcessor` subclasses parse individual tag fields; converter classes (e.g., `SongConverter`, `AlbumConverter`) assemble complete domain objects from those parsed tags.

### Testing

- **JUnit 5** with **Mockito** (`@ExtendWith(MockitoExtension.class)`)
- **Hamcrest** for assertions (`assertThat`)
- **EqualsVerifier** for equals/hashCode contracts
- **Awaitility** for async monitor tests
- Integration tests are excluded from the standard test run (files matching `*IT*.java`)
- Tests mirror the main source structure under `src/test/java/org/bff/javampd/`

### CI/CD

GitHub Actions (`.github/workflows/build.yml`) runs on push/PR to `develop`. The build command is `mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar` with SonarQube analysis against project `finnyb_javampd`.
