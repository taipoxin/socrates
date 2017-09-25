package by.tiranid.sync;

import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static String filePath;
    public static String defPath;
    public static String ppackage;
    public static boolean needSync = true;

    public static String setDefPath() {
        String relativePath = "src";
        Path path = Paths.get(relativePath);
        defPath = path.getName(0).toUri().getPath();
        defPath = defPath.substring(1, defPath.length()-4);
        return defPath;
    }

    public static String setFilePath(String ppackage) {
        String relativePath = ppackage;
        Path path = Paths.get(relativePath);
        String p = path.getName(0).toUri().getPath().substring(1);
        return p;
    }

    public static String setFilePath() {
        String relativePath = "chache";
        Path path = Paths.get(relativePath);
        filePath = path.getName(0).toUri().getPath().substring(1);
        return filePath;
    }


    public static void cleanAfterSync(String login) {
        try (PrintWriter writer = new PrintWriter(filePath + "/" + login + ".dxl")) {
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        needSync = false;
    }

    public static void saveDataToFile(String data) {

        String relativePath = "chache";
        String fileName = "login.dxl";

        File file = new File(filePath);
        if (!file.exists()) {
            log.info("creating new folder for file {}", fileName);
            file.mkdir();
        }


        try (FileWriter writer = new FileWriter(relativePath + "/" + fileName, true)) {
            log.info("writing time = {} in file {}", data, fileName);
            writer.write(data + "\n");
            writer.flush();
        } catch (IOException e) {
            log.error("failed writing", e);
        }
    }


    public static void saveRecordToFile(List<NameValuePair> record) {
        saveDataToFile(record.get(1).getValue());
    }


    public static List<String> readFromDxlToList(String path) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }


    public static void updateFile(List<String> lines, int count, String path) {
        try (PrintWriter writer = new PrintWriter(path)) {
            for (int i = count; i < lines.size(); i++) {
                String time = lines.get(i);
                writer.println(time);
            }
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }





}
