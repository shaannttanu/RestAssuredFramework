package client;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class TokenAuth {
    @Test
    public static void tokenAuth(){
        RestAssured.baseURI= GetConfigProperties.getBaseUri();

        given()
                .queryParam("key", GetConfigProperties.getRediskey())
                .when().post(String.format("api/v1/internal/testLogin/%s", GetConfigProperties.getSuperAdminToken()))
                .then().assertThat().statusCode(200);
    }
}
