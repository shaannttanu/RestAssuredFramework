package client;

import files.GlobalVariables;
import files.UtilityFunctions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import files.AdminAuthTokenPayloads;

public class GenerateAuthToken {

    @Test
    public static void generateAuthToken(String tokenType){

        String adminAuthTokenResponse;

        if(tokenType.equalsIgnoreCase("adminAuthToken")){
            adminAuthTokenResponse =RestAssured
                    .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .header("Content-Type","application/json")
                    .body(AdminAuthTokenPayloads.adminAuthTokenPayload())
                    .when().post("api/v1/oxyzo/applicant/account/login")
                    .then().assertThat().statusCode(200)
                    .extract().response().asString();
        }else{
            adminAuthTokenResponse =RestAssured
                    .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .header("Content-Type","application/json")
                    .body(AdminAuthTokenPayloads.approverAdminAuthTokenPayload())
                    .when().post("api/v1/oxyzo/applicant/account/login")
                    .then().assertThat().statusCode(200)
                    .extract().response().asString();
        }

        JsonPath adminAuthTokenResponseJson = UtilityFunctions.rawToJson(adminAuthTokenResponse);
        String AuthToken = adminAuthTokenResponseJson.getString("data.token");

        if(tokenType.equalsIgnoreCase("adminAuthToken")){
            GlobalVariables.adminAuthToken=AuthToken;
        }else{
            GlobalVariables.approverAdminAuthToken=AuthToken;
        }
    }
}
