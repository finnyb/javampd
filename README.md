JavaMPD
=======

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.inthebacklog/javampd/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.inthebacklog/javampd)
[![Coverage Status](https://coveralls.io/repos/github/finnyb/javampd/badge.svg?branch=develop)](https://coveralls.io/github/finnyb/javampd?branch=develop)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=finnyb_javampd&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=finnyb_javampd)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=finnyb_javampd)

Java API for controlling the Music Player Daemon (MPD)

Maven Dependency:

```xml
<dependency>
  <groupId>com.inthebacklog</groupId>
  <artifactId>javampd</artifactId>
  <version>7.2.0</version>
</dependency>
```

Snapshot repo:
```xml
<repositories>
    <repository>
        <id>oss.sonatype.org-snapshot</id>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

To connect to mpd using the defaults of localhost and port 6600 
```java
MPD mpd = MPD.builder().build();
```

or build to your environment

```java
MPD mpd = MPD.builder()
        .server("yourserver")
        .port(yourport)
        .password(yourpassword)
        .build();
```

Full documentation can be found [here](http://finnyb.github.io/javampd/7.1.0)
