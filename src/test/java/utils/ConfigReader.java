package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/test/resources/config/appium.properties");
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading appium.properties file: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}