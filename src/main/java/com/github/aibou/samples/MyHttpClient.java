package com.github.aibou.samples;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;

public class MyHttpClient {
    private HttpTransport transport = new NetHttpTransport();

    /**
     * ステータスコードの値を5倍にして返します。
     * This method returns the value of 5 times the status code.
     * @param endPoint URL with started with protocol
     * @return 5 times the status code
     * @throws IOException
     */
    public int fetchStatus(String endPoint) throws IOException {
        HttpRequestFactory requestFactory = transport.createRequestFactory();

        GenericUrl url = new GenericUrl(endPoint);
        HttpRequest request = requestFactory.buildGetRequest(url);
        request.setConnectTimeout(1000);
        request.setReadTimeout(1000);
        request.setThrowExceptionOnExecuteError(false);
        HttpResponse response = request.execute();
        return response.getStatusCode() * 5;
    }
}
