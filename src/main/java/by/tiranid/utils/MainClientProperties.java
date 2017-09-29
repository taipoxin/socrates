package by.tiranid.utils;

import by.tiranid.sync.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

@Slf4j
public class MainClientProperties {

    // default = config.properties
    public static String propRelativePath = "src/main/resources/";
    public static String propFile = "config.properties";
    public static String propFilePath;
    public static Properties properties;



    public static void setPropFilePath() {
        MainClientProperties.propFilePath = FileUtils.defPath + propRelativePath + propFile;
    }

    public static void setPropFilePath(String path) {
        MainClientProperties.propFilePath = path;
    }


    public static void setPropFile(String propFile) {
        MainClientProperties.propFile = propFile;
    }

    public static void setProperties(Properties properties) {
        MainClientProperties.properties = properties;
    }

    // default
    public static Properties loadFromFile() {
        return loadFromFile(propFilePath);
    }

    public static Properties loadFromFile(String file) {
        // update properties
        properties = new Properties();
        try (InputStream input = new FileInputStream(file)) {
            // load a properties file
            properties.load(input);
            return properties;
        } catch (IOException ex) {
            log.info("config file is not exists");
        }
        return null;
    }




    public static void updatePropFile(Properties props) {
        updatePropFile(props, propFilePath);
    }

    public static void updatePropFile(Properties props, String filePath) {
        try (OutputStream output = new FileOutputStream(filePath)) {
            // save properties to project root folder
            props.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }


}
