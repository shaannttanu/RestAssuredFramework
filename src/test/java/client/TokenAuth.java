package client;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class TokenAuth {
    @Test
    public static void tokenAuth(){
        RestAssured.baseURI= Base.getBaseUri();

        given()
                .queryParam("key",Base.getRediskey())
                .when().post(String.format("api/v1/internal/testLogin/%s",Base.getSuperAdminToken()))
                .then().assertThat().statusCode(200);
    }
}
