package org.example.observer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsoleLogger {
    private static ConsoleLogger instance;
    private List<String> logs = new ArrayList<>();
    private ConsoleLogger() {
    }
    public static ConsoleLogger getInstance() {
        if (instance == null) {
            instance = new ConsoleLogger();
        }
        return instance;
    }
    public void log(String message) {
        String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        String logMessage = "[" + timestamp + "] " + message;
        logs.add(logMessage);
        System.out.println(logMessage);
    }
    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }
    public void clearLogs() {
        logs.clear();
    }
}
