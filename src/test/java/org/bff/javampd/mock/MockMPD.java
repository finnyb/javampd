package org.bff.javampd.mock;

import org.bff.javampd.MPD;
import org.bff.javampd.MPDCommand;
import org.bff.javampd.exception.MPDConnectionException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockMPD extends MPD {
    public static final String TEST_FILE = "src/test/resources/testdata.txt";
    private List<TestData> testDataList;
    private String version;


    public MockMPD(String server, int port, String password) {
        loadTestFile();
    }

    protected synchronized String connect(int timeout) throws IOException, MPDConnectionException {
        return version;
    }

    @Override
    public Collection<String> sendMPDCommand(MPDCommand command) {
        return lookupResponse(command);
    }

    public boolean sendMPDCommands(List<MPDCommand> commandList) {
        return true;
    }

    private void loadTestFile() {
        testDataList = new ArrayList<TestData>();
        List<String> list = new ArrayList<String>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(TEST_FILE));
            String str;
            while ((str = in.readLine()) != null) {
                list.add(str);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] array = new String[list.size()];
        int count = 0;
        for (String s : list) {
            array[count++] = s;
        }

        int i = 0;
        while (i < array.length) {
            if(array[i].startsWith("connect:")) {
                version = array[i].replaceAll("connect:","").trim();
            }

            if (array[i].startsWith("Command:")) {
                TestData testData = new TestData();
                testData.setCommand(array[i].replaceAll("Command:", "").trim());
                ++i;
                while (array[i].trim().startsWith("Param:")) {
                    String param = array[i++].replaceAll("Param:", "").trim();
                    testData.getParams().add(param.equals("null") ? null : param);
                }
                while (array[i].trim().startsWith("Response:")) {
                    String response = array[i++].replaceAll("Response:", "").trim();
                    testData.getResult().add(response.equals("null") ? null : response);
                    if (i > array.length - 1) {
                        break;
                    }
                }
                testDataList.add(testData);
            } else {
                ++i;
            }
        }
    }

    /**
     * This may look inefficient but it's not as the first in the list should always
     * get hit :)
     *
     * @param command
     * @return
     */
    private String[] lookupResponse(String command) {
        for (TestData data : testDataList) {
            if (data.command.equals(command)) {
                if (data.getParams().size() == 0) {
                    testDataList.remove(data);
                    return (String[]) data.getResult().toArray(new String[0]);
                }
            }
        }
        return null;
    }

    /**
     * This may look inefficient but it's not as the first in the list should always
     * get hit :)
     *
     * @param command
     * @return
     */
    private Collection<String> lookupResponse(MPDCommand command) {
        for (TestData data : testDataList) {
            if (data.getCommand().equals(command.getCommand().trim())) {
                if (data.getParams().containsAll(command.getParams())) {
                    testDataList.remove(data);
                    return data.getResult();
                }
            }
        }
        return null;
    }

    public String getVersion() {
        return version;
    }
    private class TestData {
        private String command;
        private List<String> params;
        private List<String> result;

        public TestData() {
            params = new ArrayList<String>();
            result = new ArrayList<String>();
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }

        public List<String> getResult() {
            return result;
        }

        public void setResult(List<String> result) {
            this.result = result;
        }
    }
}
