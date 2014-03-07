package org.bff.javampd;

import com.google.inject.AbstractModule;
import org.bff.javampd.properties.DatabaseProperties;

public class MPDTestDatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Database.class).to(MPDDatabase.class);
        bind(ServerStatistics.class).to(MPDServerStatistics.class);
        bind(CommandExecutor.class).to(TestMPDCommandExecutor.class);
        bind(DatabaseProperties.class);
    }

}
