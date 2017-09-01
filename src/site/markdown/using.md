## Connecting
Create a connection to MPD using the builder.

Using default values of localhost port 6600

```
MPD mpd = new MPD.Builder().build();
```

or build to your environment

```
MPD mpd = new MPD.Builder()
        .server("yourserver")
        .port(yourport)
        .password(yourpassword)
        .build();
```

Almost everything you'll need will be gotten from this object.

The connection remains open for the life of the MPD object so take 
care not to create new MPD objects each time you want to do something.
Call close on the object when you are done.

##Art
When running on the same server as MPD a list of artwork can be obtained for an artist or album.  If not running on the
same server but hosting the images locally a path prefix can be passed to locate the images. 

A list of all images in the directory can be returned if those files are readable by whatever user is running JavaMPD.  

Ideally you should have some directory structure like
/Artist/Album/song.flac
/Artist/Album/cover.jpg

When searching for artist images assumptions are made that the artist lives in her own directory.  Images in the artist
directory are returned first then images for each album are returned.

supported image types:
    _jpg_ and _png_
    
```
ArtworkFinder artworkFinder = mpd.getArtworkFinder();
List<MPDArtwork> artworkList = artworkFinder.find(album);
```

##Searching
Searching for songs is done via the SongSearcher class.

```
mpd.getSongSearcher();
```

##Querying
MPD database access is accomplished using the appropriate access object from the MPDMusicDatabase.

Get the database from MPD.

```
mpd.getMusicDatabase();
```

There is a database for the following:

- Songs
- Albums
- Artists
- Genres
- Years
- Playlists
- Files


##Logging
[slf4j](http://www.slf4j.org/) is used for logging allowing you to use any compatible logging framework.
The following examples use [logback](http://logback.qos.ch/).

Lots of debugging information can be obtained from debug level logging.

```
...
<root level="debug">
    <appender-ref ref="STDOUT"/>
</root>
...
```

##Monitoring
MPDStandAloneMonitor to monitor events
####Refresh Intervals
The monitor.delay property is the length of time (in seconds) that the monitor sleeps between rounds.
Each monitor has a multiplier that can be overridden depending on the desired check frequency.  Setting the
multiplier value to 0 means checking every time the monitor runs (0 based).

The following properties can be overridden:

```
monitor.delay

monitor.output.multiplier
monitor.connection.multiplier
monitor.playlist.multiplier
monitor.error.multiplier
monitor.player.multiplier
monitor.track.multiplier
monitor.exception.multiplier
```

To override place a javampd.properties file on the classpath with your desired overrides:

For example:

javampd.properties

```
monitor.playlist.multiplier=4
```

would check for playlist changes every ~5 seconds

or maybe something list this:

javampd.properties

```
monitor.delay=10
monitor.playlist.multiplier=0
monitor.track.multiplier=2
```

would run the monitor every ~10 seconds checking the playlist every ~10 seconds and the track every ~30

##Server status
load the server status by getting the status from MPD.  

```
ServerStatus status = mpd.getServerStatus();
int volume = status.getVolume();
```

There is a default expiry interval of 5 seconds. This can be overridden by setting the interval using setExpiryInterval, use 0 to always call the server for each method.  

```
ServerStatus status = mpd.getServerStatus();
//removes 5 second cache
status.setExpiryInterval(0);    
```

Status updates can be force using forceUpdate

```
ServerStatus status = mpd.getServerStatus();
//reset the update clock so updates are pulled
status.forceUpdate();
status.setExpiryInterval(0); 
```

##Server statistics
load the server statistics by getting the status from MPD.  

```
ServerStatistics stats = mpd.getServerStatistics();
long uptime = status.getUptime();
```

It has an default expiry interval of 60 seconds.  Caching behavior is exactly like the status.
