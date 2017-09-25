package by.tiranid.web;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.junit.Assert;
import org.junit.Test;

public class RequestSenderTest {

    @Test
    public void testSendRequestPost() {
        RequestSender.sendRequestPost(null, (HttpPost) null);
    }

    @Test
    public void testCheckGetConnectionTo() {
        String uri = RequestSender.postIterationURI;
        HttpResponse response = RequestSender.checkGetConnectionTo(uri);
        if (response != null) {
            Assert.assertEquals(HttpStatus.SC_ACCEPTED, response.getStatusLine().getStatusCode());
        }
    }
}
