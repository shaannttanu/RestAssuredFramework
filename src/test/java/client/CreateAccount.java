package client;
import files.Payload;
import files.UtilityFunctions;
import files.GlobalVariables;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount {
    @Test
    public static void CreateAutomationAccount(String accountType){
        String createAutomationAccountResponse;

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        //CreateAutomationAccount :
        if(accountType.equalsIgnoreCase("automationAccount")){
            createAutomationAccountResponse = RestAssured
                    .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.createAutomationAccountPayload())
                    .when()
                    .put("api/v1/automationBot/account")
                    .then()
                    .assertThat().statusCode(200)
                    .extract().response().asString();
        }else{
            createAutomationAccountResponse = RestAssured
                    .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders).body(Payload.createApproverAccountPayload())
                    .when()
                    .put("api/v1/automationBot/account")
                    .then()
                    .assertThat().statusCode(200)
                    .extract().response().asString();
        }

        JsonPath createAutomationAccountResponseJson = UtilityFunctions.rawToJson(createAutomationAccountResponse);

        String automationId = createAutomationAccountResponseJson.getString("data.accountId");

    }

    //Generate AdminAuthToken and ApproverAdminAuthToken
    @Test
    public static void generateAuthToken(String tokenType){

        String adminAuthTokenResponse;
        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");

        if(tokenType.equalsIgnoreCase("adminAuthToken")){
            adminAuthTokenResponse =RestAssured
                    .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.adminAuthTokenPayload())
                    .when()
                    .post("api/v1/oxyzo/applicant/account/login")
                    .then()
                    .assertThat().statusCode(200)
                    .extract().response().asString();
        }else{
            adminAuthTokenResponse =RestAssured
                    .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.approverAdminAuthTokenPayload())
                    .when()
                    .post("api/v1/oxyzo/applicant/account/login")
                    .then()
                    .assertThat().statusCode(200)
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

    //SendLogin OTP and getOTP calls :
    @Test
    public static void Login(){

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        //Send LoginOTP api :
        String sendLoginOtpResponse = RestAssured.given()
                .baseUri(GetConfigProperties.getStgAPI())
                .headers(requestHeaders)
                .body(Payload.sendLoginOtpPayload(GlobalVariables.randomMobile))
                .when()
                .post("api/v1/account/loginOtp")
                .then()
                .assertThat().statusCode(200).extract().response().asString();

        JsonPath sendLoginOtpResponseJson = UtilityFunctions.rawToJson(sendLoginOtpResponse);
        Assert.assertEquals(sendLoginOtpResponseJson.get("data"),"OTP Sent");


        //GetOTP API :
        String getOtpResponse = RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .queryParam("mobile",GlobalVariables.randomMobile)
                .queryParam("key", GetConfigProperties.getRediskey())
                .when()
                .get("api/v1/internal/otp")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();

        JsonPath getOtpResponseJson = UtilityFunctions.rawToJson(getOtpResponse);
        GlobalVariables.otp = getOtpResponseJson.getString("data");

        //Storing current time :
        GlobalVariables.currentTime = Long.toString(System.currentTimeMillis());
    }

    @Test
    public static void createNewAccount(){

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");

        String createNewAccountResponse = RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .headers(requestHeaders)
                .body(Payload.accountCreatePayload(GlobalVariables.randomMobile,GlobalVariables.otp))
                .when()
                .post("api/v1/account/create")
                .then()
                .assertThat().statusCode(201).extract().response().asString();

        GlobalVariables.panNumber= UtilityFunctions.buildPan();
        GlobalVariables.gstNumber = UtilityFunctions.buildGst(GlobalVariables.panNumber);

        JsonPath createNewAccountResponseJson = UtilityFunctions.rawToJson(createNewAccountResponse);

        GlobalVariables.buyerAccountId = createNewAccountResponseJson.getString("data.minAccountDto.accountId");
        GlobalVariables.clientAuthToken = createNewAccountResponseJson.getString("data.token");

    }

}
