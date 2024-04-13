## Maven

Get it from maven
central [here](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.inthebacklog%22%20AND%20a%3A%22javampd%22)

To use the latest snapshot add this repo

```
<repository>
    <id>sonatype-nexus-snapshots</id>
    <name>Sonatype Nexus Snapshots</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```

then add the dependency

```
<dependency>
  <groupId>com.inthebacklog</groupId>
  <artifactId>javampd</artifactId>
  <version>7.3.0</version>
</dependency>
```

## Releases

releases can be found on [here](https://github.com/finnyb/javampd/releases)
