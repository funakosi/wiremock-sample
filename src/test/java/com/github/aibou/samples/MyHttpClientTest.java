package com.github.aibou.samples;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import io.restassured.RestAssured;

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

        stubFor(get(urlEqualTo("/body-file"))
                .willReturn(aResponse()
                        .withBodyFile("body.json")));
    }

    @Test
    public void returns_1000_when_status_is_200() throws IOException {
        MyHttpClient client = new MyHttpClient();
        assertThat(client.fetchStatus("http://localhost:8089/200"), is(1000));
        //another example
        RestAssured.baseURI = "http://localhost:8089/200";
        given().get().then().statusCode(200);
    }

    @Test
    public void returns_1510_when_status_is_302() throws IOException {
        MyHttpClient client = new MyHttpClient();
        assertThat(client.fetchStatus("http://localhost:8089/302"), is(1510));
        //another example
        RestAssured.baseURI = "http://localhost:8089/302";
        given().get().then().statusCode(302);
    }

    @Test
    public void returns_2005_when_status_is_401() throws IOException {
        MyHttpClient client = new MyHttpClient();
        assertThat(client.fetchStatus("http://localhost:8089/401"), is(2005));
        //another example
        RestAssured.baseURI = "http://localhost:8089/401";
        given().get().then().statusCode(401);
    }

    @Test
    public void returns_2500_when_status_is_500() throws IOException {
        MyHttpClient client = new MyHttpClient();
        assertThat(client.fetchStatus("http://localhost:8089/500"), is(2500));
        //another example
        RestAssured.baseURI = "http://localhost:8089/500";
        given().get().then().statusCode(500);
    }

    @Test(expected = SocketTimeoutException.class)
    public void タイムアウト() throws IOException {
        new MyHttpClient().fetchStatus("http://localhost:8089/timeout");
        //another example
        RestAssured.baseURI = "http://localhost:8089/timeout";
        given().get().then().statusCode(200);
    }

    @Test
    public void returns_json() {
        RestAssured.baseURI = "http://localhost:8089/body-file";
        given().get().then().statusCode(200);
        String response = given().get().asString();
        System.out.println(response);
    }
}
