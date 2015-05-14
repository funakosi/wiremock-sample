package com.github.aibou.samples;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MyHttpClientTest {
    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(8089);

    @BeforeClass
    public static void registerStubs() {
        stubFor(get(urlPathEqualTo("/200"))
                .willReturn(
                        aResponse().withStatus(200)
                ));

        stubFor(get(urlPathEqualTo("/302"))
                .willReturn(
                        aResponse().withStatus(302)
                ));

        stubFor(get(urlPathEqualTo("/401"))
                .willReturn(
                        aResponse().withStatus(401)
                ));

        stubFor(get(urlPathEqualTo("/500"))
                .willReturn(
                        aResponse().withStatus(500)
                ));

        stubFor(get(urlPathEqualTo("/timeout"))
                .willReturn(
                        aResponse().withStatus(200)
                                .withFixedDelay(2000)
                ));
    }

    @Test
    public void returns_1000_when_status_is_200() throws IOException {
        MyHttpClient client = new MyHttpClient();
        assertThat(client.fetchStatus("http://localhost:8089/200"), is(1000));
    }

    @Test
    public void returns_1510_when_status_is_302() throws IOException {
        MyHttpClient client = new MyHttpClient();
        assertThat(client.fetchStatus("http://localhost:8089/302"), is(1510));
    }

    @Test
    public void returns_2005_when_status_is_401() throws IOException {
        MyHttpClient client = new MyHttpClient();
        assertThat(client.fetchStatus("http://localhost:8089/401"), is(2005));
    }

    @Test
    public void returns_2500_when_status_is_500() throws IOException {
        MyHttpClient client = new MyHttpClient();
        assertThat(client.fetchStatus("http://localhost:8089/500"), is(2500));
    }

    @Test(expected = SocketTimeoutException.class)
    public void タイムアウト() throws IOException {
        new MyHttpClient().fetchStatus("http://localhost:8089/timeout");
    }
}
