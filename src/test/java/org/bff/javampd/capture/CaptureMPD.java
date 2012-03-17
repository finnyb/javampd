package org.bff.javampd.capture;

import org.bff.javampd.MPD;
import org.bff.javampd.MPDCommand;
import org.bff.javampd.MockUtils;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.mock.MockMPD;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class CaptureMPD extends MPD {
    private static final String TEST_FILE = MockMPD.TEST_FILE;

    public CaptureMPD(String server, int port, String password) throws UnknownHostException, MPDConnectionException {
        super(server, port, password);
        createTestFile();
    }

    private void createTestFile() {
        File file = new File(TEST_FILE);
        if (!file.exists()) {
            try {
                file.delete();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected synchronized String connect(int timeout) throws IOException, MPDConnectionException {
        String version = super.connect(timeout);
        writeToFile("connect: " + version);
        return version;
    }

    public List<String> sendMPDCommand(MPDCommand command) throws MPDResponseException, MPDConnectionException {
        writeToFile("Class: " + MockUtils.getClassName());
        writeToFile("\tCommand: " + command.getCommand());
        for (String s : command.getParams()) {
            writeToFile("\t\tParam:" + s);
        }

        List<String> response = null;
        try {
            response = new ArrayList<String>(super.sendMPDCommand(command));
        } catch (MPDResponseException re) {
            writeToFile("ResponseException: " + re.getMessage());
            throw new MPDResponseException(re.getMessage(), re.getCommand());
        }

        if (response == null) {
            writeToFile("\t\t\tResponse:null");
        } else {
            for (String r : response) {
                writeToFile("\t\t\tResponse:" + r);
            }
        }
        return response;
    }

    private void writeToFile(String str) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(TEST_FILE, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
