package com.vsiestov.users.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vsiestov.TodoApplication;
import com.vsiestov.users.repository.UsersRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = TodoApplication.class)
@ActiveProfiles({ "test" })
public class UserResourcesTest {

    @Autowired
    private UsersRepository usersRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    private final JsonNode getEmptyRegistrationNode() throws IOException {
        final InputStream stream = getClass().getResourceAsStream("/data/users/registration.json");

        return new ObjectMapper().readTree(stream);
    }

    @Test
    @DisplayName("it should return an error because of the invalid properties")
    public void tryToRegisterWithInvalidPayload() throws IOException {
        Response response = given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(getEmptyRegistrationNode().toString())
            .post("/api/sign-up");

        assertEquals(response.getStatusCode(), 422);

        JsonNode body = new ObjectMapper().readTree(response.getBody().print());

        assertEquals(body.get("status").asInt(), 422);
        assertNotNull(body.get("errors"));
    }

    @Test
    @DisplayName("it should provide an invalid email")
    public void tryToRegisterWithInvalidEmailPayload() throws IOException {
        JsonNode payload = getEmptyRegistrationNode();

        ((ObjectNode)payload).set("firstName", JsonNodeFactory.instance.textNode("Valerii"));
        ((ObjectNode)payload).set("lastName", JsonNodeFactory.instance.textNode("Siestov"));
        ((ObjectNode)payload).set("password", JsonNodeFactory.instance.textNode("Password1$"));

        Response response = given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(payload)
            .post("/api/sign-up");

        assertEquals(response.getStatusCode(), 422);

        JsonNode body = new ObjectMapper().readTree(response.getBody().print());

        assertEquals(body.get("status").asInt(), 422);
        assertNotNull(body.get("errors").get(0).get("param").asText(), "email");
        assertNotNull(body.get("errors").get(0).get("message").asText(), "Provided email is not valid");
    }

    @Test
    @DisplayName("it should register a new user")
    public void registerNewUser() throws IOException {
        JsonNode payload = getEmptyRegistrationNode();

        usersRepository.deleteAll();

        ((ObjectNode)payload).set("firstName", JsonNodeFactory.instance.textNode("Valerii"));
        ((ObjectNode)payload).set("lastName", JsonNodeFactory.instance.textNode("Siestov"));
        ((ObjectNode)payload).set("password", JsonNodeFactory.instance.textNode("Password1$"));
        ((ObjectNode)payload).set("email", JsonNodeFactory.instance.textNode("valerii.siestov@gmail.com"));

        Response response = given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(payload)
            .post("/api/sign-up");

        assertEquals(response.getStatusCode(), 200);

        JsonNode body = new ObjectMapper().readTree(response.getBody().print());

        assertEquals(body.get("user").get("firstName").asText(), "Valerii");
        assertEquals(body.get("user").get("lastName").asText(), "Siestov");
        assertEquals(body.get("user").get("email").asText(), "valerii.siestov@gmail.com");
        assertNotNull(body.get("user").get("id"));
        assertNotNull(body.get("token"));
    }

    @Test
    @DisplayName("it should try to register a member with the same email")
    public void tryToRegisterSameEmail() throws IOException {
        JsonNode payload = getEmptyRegistrationNode();

        ((ObjectNode)payload).set("firstName", JsonNodeFactory.instance.textNode("Valerii"));
        ((ObjectNode)payload).set("lastName", JsonNodeFactory.instance.textNode("Siestov"));
        ((ObjectNode)payload).set("password", JsonNodeFactory.instance.textNode("Password1$"));
        ((ObjectNode)payload).set("email", JsonNodeFactory.instance.textNode("valerii.siestov@gmail.com"));

        Response response = given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(payload)
            .post("/api/sign-up");

        assertEquals(response.getStatusCode(), 422);

        JsonNode body = new ObjectMapper().readTree(response.getBody().print());

        assertNotNull(body.get("errors").get(0).get("param").asText(), "email");
        assertNotNull(body.get("errors").get(0).get("message").asText(), "A member with such email already exists");
    }
}
