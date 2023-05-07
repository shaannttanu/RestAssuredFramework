package client;

import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import files.AdminAuthTokenPayloads;

public class GenerateAuthToken {
    public static String adminAuthToken;
    public static String approverAdminAuthToken;
    @Test
    public static void generateAuthToken(String tokenType){

        RestAssured.baseURI= GetConfigProperties.getStgAPI();
        String adminAuthTokenResponse;

        if(tokenType.equalsIgnoreCase("adminAuthToken")){
            adminAuthTokenResponse =given()
                    .header("Content-Type","application/json")
                    .body(AdminAuthTokenPayloads.adminAuthTokenPayload())
                    .when().post("api/v1/oxyzo/applicant/account/login")
                    .then().assertThat().statusCode(200)
                    .extract().response().asString();
        }else{
            adminAuthTokenResponse =given().header("Content-Type","application/json")
                    .body(AdminAuthTokenPayloads.approverAdminAuthTokenPayload())
                    .when().post("api/v1/oxyzo/applicant/account/login")
                    .then().assertThat().statusCode(200)
                    .extract().response().asString();
        }

        JsonPath adminAuthTokenResponseJson = ReusableMethods.rawToJson(adminAuthTokenResponse);
        String AuthToken = adminAuthTokenResponseJson.getString("data.token");

        if(tokenType.equalsIgnoreCase("adminAuthToken")){
            adminAuthToken=AuthToken;
        }else{
            approverAdminAuthToken=AuthToken;
        }
    }
}
