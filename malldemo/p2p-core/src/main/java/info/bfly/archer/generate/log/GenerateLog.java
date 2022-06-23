package info.bfly.archer.generate.log;

import java.util.ArrayList;
import java.util.List;

public class GenerateLog {
    private List<String> errorLogs;

    public GenerateLog() {
        errorLogs = new ArrayList<String>();
    }

    public void addErrorLog(String error) {
        errorLogs.add(error);
    }

    public List<String> getErrorLogs() {
        return errorLogs;
    }

    public boolean isEmpty() {
        return errorLogs.size() == 0;
    }
}
