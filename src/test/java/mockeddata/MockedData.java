package mockeddata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MockedData {
    private Map<String, Collection<String>> mockedDataMap;

    private File mockedDataFile;

    public MockedData(File file) throws FileNotFoundException {
        this.mockedDataFile = file;
        this.mockedDataMap = new HashMap<>();
        loadMockedData();
    }

    public Collection<String> lookupCommand(String command) {
        return mockedDataMap.get(command);
    }

    private void loadMockedData() throws FileNotFoundException {
        Scanner scanner = new Scanner(this.mockedDataFile);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.startsWith("start command:")) {
                String command = s.replaceFirst("start command:", "").trim();
                mockedDataMap.put(command, processResponse(scanner));
            }
        }
    }

    private Collection<String> processResponse(Scanner scanner) {
        List<String> commandResponse = new ArrayList<>();
        String response = scanner.nextLine();
        while (!response.startsWith("end command:")) {
            commandResponse.add(response);
            response = scanner.nextLine();
        }
        return commandResponse;
    }
}
