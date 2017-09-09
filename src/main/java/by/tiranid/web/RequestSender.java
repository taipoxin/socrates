package by.tiranid.web;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RequestSender {

    private static String login = "tiranid";
    private static String password = "12345";


    /**
     * sending request with iter time to spring app
     * @param iterStartTime
     */
    public static void sendRequest(long iterStartTime) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://localhost:8081/postIter");

// Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);


        params.add(new BasicNameValuePair("hash", Integer.toString((login+password).hashCode())));


        params.add(new BasicNameValuePair("time", Long.toString(iterStartTime)));


        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

//Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {

                try ( InputStream instream = entity.getContent()) {
                    // do something useful

                }
            }
        }
        catch (UnsupportedEncodingException e) {

        }
        catch (IOException e) {

        }
    }
}
