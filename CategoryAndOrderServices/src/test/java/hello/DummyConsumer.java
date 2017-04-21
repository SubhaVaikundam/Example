package hello;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class DummyConsumer {
    private String url;

    public DummyConsumer(String url) {
        this.url = url;
    }

    public String getResponseAsString(String path) throws IOException{
        HttpResponse response = getResponse(path);
        return getEntityAsString(response);
    }

    private String getEntityAsString(HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity());
    }

    public HttpResponse getResponse(String path) throws IOException {
        return Request.Get(url + path)
                .execute().returnResponse();
    }

}
