package client;
import files.AccountCreatePayloads;
import files.UtilityFunctions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import files.ReusableMethods;

import static io.restassured.RestAssured.*;

public class CreateAccount {

    public static String otp;
    public static String randomMobile=Base.getRandomMobile();
    public static String panNumber;
    public static String gstNumber;
    public static String buyerAccountId;
    public static String clientAuthToken;
    @Test
    public static void CreateAutomationAccount(String accountType){

        RestAssured.baseURI= Base.getBaseUri();
        String createAutomationAccountResponse;

        //CreateAutomationAccount :
        if(accountType.equalsIgnoreCase("automationAccount")){
            createAutomationAccountResponse = given()
                    .header("Content-Type","application/json").body(AccountCreatePayloads.createAutomationAccountPayload())
                    .when()
                    .put("api/v1/automationBot/account")
                    .then().assertThat().statusCode(200).extract().response().asString();
        }else{
            createAutomationAccountResponse = given()
                    .header("Content-Type","application/json").body(AccountCreatePayloads.createApproverAccountPayload())
                    .when()
                    .put("api/v1/automationBot/account")
                    .then().assertThat().statusCode(200).extract().response().asString();
        }

        JsonPath createAutomationAccountResponseJson = ReusableMethods.rawToJson(createAutomationAccountResponse);

        String automationId = createAutomationAccountResponseJson.getString("data.accountId");
        if(accountType.equalsIgnoreCase("automationAccount")){
            System.out.println("Automation account Id : "+automationId);
        }else{
            System.out.println("Approver account Id : "+automationId);
        }

    }

    //SendLogin OTP and getOTP calls :
    @Test
    public static void Login(){

        RestAssured.baseURI = Base.getBaseUri();
        //Send LoginOTP api :
        String sendLoginOtpResponse=given().header("Content-Type","application/json")
                .body(AccountCreatePayloads.sendLoginOtpPayload(randomMobile))
                .when()
                .post("api/v1/account/loginOtp")
                .then().assertThat().statusCode(200).extract().response().asString();

        JsonPath sendLoginOtpResponseJson = ReusableMethods.rawToJson(sendLoginOtpResponse);
        Assert.assertEquals(sendLoginOtpResponseJson.get("data"),"OTP Sent");


        //GetOTP API :

        String getOtpResponse = given()
                .queryParam("mobile",randomMobile)
                .queryParam("key",Base.getRediskey())
                .when().get("api/v1/internal/otp")
                .then().assertThat().statusCode(200).extract().response().asString();

        JsonPath getOtpResponseJson = ReusableMethods.rawToJson(getOtpResponse);
        otp = getOtpResponseJson.getString("data");
        System.out.println("OTP : "+otp);

        //Storing current time :
        String currentTime = Long.toString(System.currentTimeMillis());
    }

    @Test
    public static void createNewAccount(){

        RestAssured.baseURI = Base.getBaseUri();
        String createNewAccountResponse=given().header("Content-Type","application/json")
                .body(AccountCreatePayloads.accountCreatePayload(randomMobile,otp))
                .when().post("api/v1/account/create")
                .then().assertThat().statusCode(201).extract().response().asString();

        panNumber= UtilityFunctions.buildPan();
        System.out.println("PAN number : "+panNumber);
        gstNumber = UtilityFunctions.buildGst(panNumber);
        System.out.println("GST number : "+gstNumber);

        JsonPath createNewAccountResponseJson = ReusableMethods.rawToJson(createNewAccountResponse);
        buyerAccountId = createNewAccountResponseJson.getString("data.minAccountDto.accountId");
        clientAuthToken = createNewAccountResponseJson.getString("data.token");

    }

}
