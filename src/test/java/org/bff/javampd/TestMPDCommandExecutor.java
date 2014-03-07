package org.bff.javampd;

import com.google.inject.Singleton;
import mockeddata.MockedData;
import org.bff.javampd.exception.MPDResponseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;

/**
 * @author bill
 * @since: 11/23/13 6:22 PM
 */
@Singleton
public class TestMPDCommandExecutor extends MPDCommandExecutor {
    private MockedData mockedData;
    private File dataFile;

    private Collection<String> lookupCommand(String command) {
        if (mockedData == null) {
            throw new IllegalArgumentException("Mocked data not loaded, have you set the data file?");
        }
        return mockedData.lookupCommand(command.trim());
    }

    @Override
    public synchronized Collection<String> sendCommand(MPDCommand command) throws MPDResponseException {
        return lookupCommand(convertCommand(command.getCommand(), command.getParams()));
    }

    private String convertCommand(String command, List<String> params) {
        StringBuilder sb = new StringBuilder(command);
        if (params != null) {
            for (String param : params) {
                if (param != null) {
                    param = param.replaceAll("\"", "\\\\\"");
                    sb.append(param.contains(" ") ? " \"" : " ");
                    sb.append(param);
                    sb.append(param.contains(" ") ? "\"" : "");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public void setMpd(MPD mpd) {
        //do nothing
    }

    public File getDataFile() {
        return dataFile;
    }

    public void setDataFile(File dataFile) throws FileNotFoundException {
        this.dataFile = dataFile;
        mockedData = new MockedData(dataFile);
    }
}
