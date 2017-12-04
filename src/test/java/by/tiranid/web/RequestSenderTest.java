package by.tiranid.web;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RequestSenderTest {

    @Test
    public void testSendRequestPost() {
        RequestSender.sendRequestPost(null, (HttpPost) null);
    }

    @Test
    public void testCheckGetConnectionTo() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        String server_Ip = addr.getHostAddress();


        RequestSender.setServerIp(server_Ip);
        String uri = "http://" + RequestSender.getServerIp() + ":" + RequestSender.getServerPort() + RequestSender.getPostIterationURI();
        HttpResponse response = RequestSender.checkGetConnectionTo(uri);
        if (response != null) {
            Assert.assertEquals(HttpStatus.SC_ACCEPTED, response.getStatusLine().getStatusCode());
        }
    }
}
