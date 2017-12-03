package by.tiranid.web;

import by.tiranid.sync.FileUtils;
import by.tiranid.utils.MainClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RequestSender {

    public static final String postIterationURI = "http://localhost:8080/postIter";
    private static final String login = "tiranid";
    private static final String password = "6559520";


    public static List<NameValuePair> createTimeRecord(long iterStartTime) {
        List<NameValuePair> params = new ArrayList<>(2);
        Date d = new Date(iterStartTime);
        Time t = new Time(iterStartTime);
        params.add(new BasicNameValuePair("hash", Integer.toString((login+password).hashCode())));
        params.add(new BasicNameValuePair("date", d.toString()));
        params.add(new BasicNameValuePair("time", t.toString()));
        // FIXME: it
        params.add(new BasicNameValuePair("duration", MainClientProperties.properties.getProperty("default_duration")));

        return params;
    }


    public static HttpResponse sendRequest(List<NameValuePair> requestBody, String uri, String method) {
        switch (method) {
            case "GET":
                break;
            case "POST":
                return sendRequestPost(requestBody, new HttpPost(uri));
        }
        return null;
    }


    public static HttpResponse sendRequest(List<NameValuePair> requestBody, HttpRequestBase httpRequestBase) {
        switch (httpRequestBase.getMethod()) {
            case "GET":
                break;
            case "POST":
                return sendRequestPost(requestBody, (HttpPost) httpRequestBase);
        }
        return null;
    }


    public static HttpResponse sendRequestPost(List<NameValuePair> requestBody, String uri) {
        return sendRequestPost(requestBody, new HttpPost(uri));
    }

    public static HttpResponse sendRequestPost(List<NameValuePair> requestBody, HttpPost httpPost) {
        HttpClient httpclient = HttpClients.createDefault();
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(requestBody, "UTF-8"));

            //Execute and get the response. (connect exception)
            HttpResponse response = httpclient.execute(httpPost);
            return response;

        } catch (NullPointerException e) {
            log.warn("", e);
        } catch (ConnectException e) {
            log.info("connection does not exists");
        } catch (IOException e) {
            log.warn("", e);
        }
        return null;
    }

    public static boolean syncUserData(String login, String uri) {
        boolean success = false;
        // начинается с 0
        int count = 0;
        String path = FileUtils.filePath + login + ".dxl";
        List<String> lines = FileUtils.readFromDxlToList(path);
        if (lines.size() != 0) {
            success = true;
        }
        HttpResponse response;
        for (String time : lines) {
            List<NameValuePair> pair = createTimeRecord(Long.valueOf(time));
            response = sendRequestPost(pair, uri);
            if (response == null) {
                success = false;
                break;
            }
            count++;
        }
        return success;
    }


    public static HttpResponse checkGetConnectionTo(String uri) {
        HttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet getReq = new HttpGet(uri);

            //Execute and get the response. (connect exception)
            HttpResponse response = httpclient.execute(getReq);
            return response;
        } catch (ConnectException e) {
            log.info("connection does not exists");
        } catch (IOException e) {
            log.warn("", e);
        }
        return null;
    }

    public static boolean isGetConnectionTo(String uri) {
        HttpResponse response = checkGetConnectionTo(uri);
        return response != null;
    }
}
