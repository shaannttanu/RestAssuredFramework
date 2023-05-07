package client;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class TokenAuth {
    @Test
    public static void tokenAuth(String token){
        RestAssured.baseURI= GetConfigProperties.getStgAPI();

        given()
                .queryParam("key", GetConfigProperties.getRediskey())
                .when().post(String.format("api/v1/internal/testLogin/%s", token))
                .then().assertThat().statusCode(200);
    }
}
