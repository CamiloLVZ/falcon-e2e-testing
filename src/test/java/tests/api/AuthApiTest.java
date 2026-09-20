package tests.api;

import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.RequestOptions;
import config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("api")
@Tag("regression")
public class AuthApiTest extends BaseTest {

    private String loginClient() {
        APIRequestContext request = playwright.request().newContext();
        String loginJson = "{\"email\":\"e2e.client@falcon.test\",\"password\":\"Test1234!\"}";

        APIResponse response = request.post(
                TestConfig.apiBaseUrl() + "/v1/auth/login",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(loginJson)
        );

        PlaywrightAssertions.assertThat(response).isOK();

        String token;
        try {
            JsonNode responseBody = new ObjectMapper().readTree(response.text());
            token = responseBody.get("accessToken").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing login response token", e);
        }
        return token;

    }

    @Test
    public void getMyPassengerUnauthenticated() {
        APIRequestContext request = playwright.request().newContext();

        APIResponse response = request.get(TestConfig.apiBaseUrl() + "/v1/passengers/me",
                RequestOptions.create().setHeader("Content-Type", "application/json"));

        PlaywrightAssertions.assertThat(response).not().isOK();
        Assertions.assertEquals(401, response.status());
    }

    @Test
    public void getMyPassengerWithExpiredToken() {
        APIRequestContext request = playwright.request().newContext();

        String token = "invalid-token";

        APIResponse response = request.get(TestConfig.apiBaseUrl() + "/v1/passengers/me",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer " + token)
        );

        PlaywrightAssertions.assertThat(response).not().isOK();
        Assertions.assertEquals(401, response.status());
    }

    @Test
    public void authenticatedClientTriesToPostFlight(){
        String token = loginClient();
        APIRequestContext request = playwright.request().newContext();

        APIResponse response = request.post(TestConfig.apiBaseUrl() + "/v1/flights",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer " + token)
        );

        PlaywrightAssertions.assertThat(response).not().isOK();
        Assertions.assertEquals(403, response.status());
    }

    @Test
    public void getRoutesUnauthenticated() {
        APIRequestContext request = playwright.request().newContext();

        APIResponse response = request.get(TestConfig.apiBaseUrl() + "/v1/routes?size=10&page=0",
                RequestOptions.create().setHeader("Content-Type", "application/json"));

        PlaywrightAssertions.assertThat(response).isOK();
        Assertions.assertNotNull(response.text());
    }

    @Test
    public void authenticatedClientTriesToPostRoute(){
        String token = loginClient();
        APIRequestContext request = playwright.request().newContext();

        APIResponse response = request.post(TestConfig.apiBaseUrl() + "/v1/routes",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer " + token)
        );

        PlaywrightAssertions.assertThat(response).not().isOK();
        Assertions.assertEquals(403, response.status());
    }

}
