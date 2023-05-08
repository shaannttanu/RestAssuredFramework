package client;
import files.AccountCreatePayloads;
import files.UtilityFunctions;
import files.GlobalVariables;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class CreateAccount {
    @Test
    public static void CreateAutomationAccount(String accountType){
        String createAutomationAccountResponse;

        //CreateAutomationAccount :
        if(accountType.equalsIgnoreCase("automationAccount")){
            createAutomationAccountResponse = RestAssured
                    .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .header("Content-Type","application/json").body(AccountCreatePayloads.createAutomationAccountPayload())
                    .when()
                    .put("api/v1/automationBot/account")
                    .then().assertThat().statusCode(200)
                    .extract().response().asString();
        }else{
            createAutomationAccountResponse = RestAssured
                    .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .header("Content-Type","application/json").body(AccountCreatePayloads.createApproverAccountPayload())
                    .when()
                    .put("api/v1/automationBot/account")
                    .then().assertThat().statusCode(200)
                    .extract().response().asString();
        }

        JsonPath createAutomationAccountResponseJson = UtilityFunctions.rawToJson(createAutomationAccountResponse);

        String automationId = createAutomationAccountResponseJson.getString("data.accountId");

    }

    //SendLogin OTP and getOTP calls :
    @Test
    public static void Login(){

        //Send LoginOTP api :
        String sendLoginOtpResponse = RestAssured.given()
                .baseUri(GetConfigProperties.getStgAPI())
                .header("Content-Type","application/json")
                .body(AccountCreatePayloads.sendLoginOtpPayload(GlobalVariables.randomMobile))
                .when()
                .post("api/v1/account/loginOtp")
                .then().assertThat().statusCode(200).extract().response().asString();

        JsonPath sendLoginOtpResponseJson = UtilityFunctions.rawToJson(sendLoginOtpResponse);
        Assert.assertEquals(sendLoginOtpResponseJson.get("data"),"OTP Sent");


        //GetOTP API :
        String getOtpResponse = RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .queryParam("mobile",GlobalVariables.randomMobile)
                .queryParam("key", GetConfigProperties.getRediskey())
                .when().get("api/v1/internal/otp")
                .then().assertThat().statusCode(200).extract().response().asString();

        JsonPath getOtpResponseJson = UtilityFunctions.rawToJson(getOtpResponse);
        GlobalVariables.otp = getOtpResponseJson.getString("data");

        //Storing current time :
        GlobalVariables.currentTime = Long.toString(System.currentTimeMillis());
    }

    @Test
    public static void createNewAccount(){

        String createNewAccountResponse = RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .header("Content-Type","application/json")
                .body(AccountCreatePayloads.accountCreatePayload(GlobalVariables.randomMobile,GlobalVariables.otp))
                .when().post("api/v1/account/create")
                .then().assertThat().statusCode(201).extract().response().asString();

        GlobalVariables.panNumber= UtilityFunctions.buildPan();
        GlobalVariables.gstNumber = UtilityFunctions.buildGst(GlobalVariables.panNumber);

        JsonPath createNewAccountResponseJson = UtilityFunctions.rawToJson(createNewAccountResponse);

        GlobalVariables.buyerAccountId = createNewAccountResponseJson.getString("data.minAccountDto.accountId");
        GlobalVariables.clientAuthToken = createNewAccountResponseJson.getString("data.token");

    }

}
