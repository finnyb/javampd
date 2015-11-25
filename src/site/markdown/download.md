## Maven
Get it from maven central

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
  <groupId>net.thejavashop</groupId>
  <artifactId>javampd</artifactId>
  <version>6.0.0-SNAPSHOT</version>
</dependency>
```
##Releases
releases can be found on [here](https://github.com/finnyb/javampd/releases)
