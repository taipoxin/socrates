package by.tiranid.web;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.ConnectException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestSender {

    private static String login = "tiranid";
    private static String password = "12345";

    public static boolean needSync = true;
    public static String filePath;


    public static List<NameValuePair> createTimeRecord(long iterStartTime) {
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("hash", Integer.toString((login+password).hashCode())));
        params.add(new BasicNameValuePair("time", Long.toString(iterStartTime)));
        return params;
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

    public static String setFilePath() {
        String relativePath = "chache";
        Path path = Paths.get(relativePath);
        filePath = path.getName(0).toUri().getPath().substring(1);
        return filePath;
    }

    public static void saveRecordToFile(List<NameValuePair> record) {
        String relativePath = "chache";
        String fileName = "login.dxl";
        File file = null;
        file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }

        String data = record.get(1).getValue();

        try(FileWriter writer = new FileWriter(relativePath + "/" + fileName, true)) {
            writer.write(data + "\n");
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sending request with iter time to spring app
     * @param iterStartTime
     */
    public static HttpResponse sendRequest(long iterStartTime) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://localhost:8081/postIter");

        List<NameValuePair> record = createTimeRecord(iterStartTime);

        // Request parameters and other properties.

        try {
            httppost.setEntity(new UrlEncodedFormEntity(record, "UTF-8"));

            //Execute and get the response. (connect exception)
            HttpResponse response = httpclient.execute(httppost);
            return response;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (ConnectException e) {
            // сохранение в файл с логином времени
            saveRecordToFile(record);
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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


    public static void syncUserData(String login) {
        boolean success = true;
        // начинается с 0
        int count = 0;
        String path = filePath + "/" + login + ".dxl";
        List<String> lines = readFromDxlToList(path);
        HttpResponse response;
        for (String time : lines) {
            response = sendRequest(Long.valueOf(time));
            if (response == null) {
                success = false;
                break;
            }
            count++;
        }
        // успешная доставка запросов
        if (success) {
            cleanAfterSync(login);
        }
        // проблемы
        else {
            // стираем файл с логом до проблемного запроса
            // то есть count строк
            // лучше - перезапись файла оставшимися значениями из lines
            updateFile(lines, count, path);
        }
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

    public static void main(String[] args) {
        setFilePath();

        sendRequest(new Date().getTime());
        syncUserData("login");

        //cleanAfterSync();
    }
}
