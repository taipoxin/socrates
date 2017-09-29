package by.tiranid.utils;

import by.tiranid.sync.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;


@Slf4j
public class MainUtils {


    // based on userLogin prop
    public static void loadDefaultProperties() {
        if (FileUtils.defPath == null) {
            FileUtils.setDefPath();
            log.info("set default path");
        }
        if (MainClientProperties.propFilePath == null) {
            MainClientProperties.setPropFilePath();
            log.info("set full file path: " + MainClientProperties.propFilePath);
        }
        if (FileUtils.filePath == null) {
            FileUtils.setFilePath();
        }
        String filePath = FileUtils.defPath;

        log.info("loading properties from file...");
        Properties props = MainClientProperties.loadFromFile();
        if (props == null || props.getProperty("userLogin") == null) {
            props = new Properties();
            log.info("properties file is empty" + "\n creating new properties file");
            String userLogin = "tiranid";
            String userPass = "6559520";
            String userHash = Integer.toString((userLogin + userPass).hashCode());

            props.setProperty("filePath", filePath);
            props.setProperty("userHash", userHash);
            props.setProperty("userLogin", userLogin);
            MainClientProperties.updatePropFile(props);
            log.info("properties set by default for current user " + userLogin);
        } else {
            log.info("properties set successfully");
        }
    }
}
