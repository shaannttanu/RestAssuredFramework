package client;

import com.github.javafaker.Faker;
import files.GlobalVariables;
import files.Payload;
import files.UtilityFunctions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateVerifiedOrganisation {

    //clear response file at the start of each script :
    @Test(priority = 0)
    public static void clearFile() throws IOException {
        UtilityFunctions.eraseFileContents();
    }

    //create automation and approver accounts :
    @Test(dataProvider = "accountType" , priority = 1)
    public void createAutomationAndApproverAccount(String name, String mobile) throws Exception {

        String response;
        response = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .basePath("/api/v1/automationBot/account")
                    .contentType("application/json")
                    .body(Payload.automationApproverAccountPayload(name, mobile))
                .when()
                    .put()
                .then()
                    .assertThat()
                    .statusCode(200)
                    .extract().response().asString();

        JsonPath jsPath = new JsonPath(response);
        if (name.equals("Automation Bot")) {
            GlobalVariables.automationId = jsPath.getString("data.accountId");
        } else {
            GlobalVariables.approverId = jsPath.getString("data.accountId");
        }

        GlobalVariables.startTime= UtilityFunctions.getCurrentTime();
    }

    @DataProvider(name = "accountType")
    public Object[][] getData() {
        return new Object[][]{{"Automation Bot", "1000000000"}, {"Approver Bot", "1000000001"}};
    }


    //Authenticating superAdmin token :
    @Test(priority = 2)
    public static void tokenAuth1() throws Exception{
        ReusableAPI.tokenAuth(GetConfigProperties.getSuperAdminToken());
    }

    //generating adminauthtoken and approver adminauthtoken :
    @Test(dataProvider = "authToken" , priority = 3)
    public void adminAndApproverAdminAuthToken(String mobile) throws Exception {

        String response;
        response = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .basePath("/api/v1/oxyzo/applicant/account/login")
                    .contentType("application/json")
                    .body(Payload.adminAndApproverAdminAuthTokenPayload(mobile))
                .when()
                    .post()
                .then()
                    .assertThat()
                    .statusCode(200)
                    .extract().response().asString();

        JsonPath jsPath = new JsonPath(response);
        if (mobile.equals("1000000000")) {
            GlobalVariables.adminAuthToken = jsPath.getString("data.token");
        } else if(mobile.equals("1000000001")) {
            GlobalVariables.approverAdminAuthToken = jsPath.getString("data.token");
        }
    }

    @DataProvider(name = "authToken")
    public Object[] getMobile() {
        return new Object[]{"1000000000","1000000001"};
    }

    //Login part(send otp & getOtp)
    @Test(priority = 4)
    public static void sendLoginOtp() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");

        String sendLoginOtpResponse = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.sendLoginOtpPayload(GlobalVariables.randomMobile))
                .when()
                    .post("api/v1/account/loginOtp")
                .then()
                    .assertThat().statusCode(200)
                    .extract().response().asString();

        JsonPath sendLoginOtpResponseJson = UtilityFunctions.rawToJson(sendLoginOtpResponse);
        Assert.assertEquals(sendLoginOtpResponseJson.get("data"),"OTP Sent");

    }

    //Get OTP :
    @Test(priority = 5)
    public static void getOtp(){

        Map<String,String> queryParameters = new HashMap<>();
        queryParameters.put("mobile",GlobalVariables.randomMobile);
        queryParameters.put("key",GetConfigProperties.getRediskey());

        String getOtpResponse = RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .queryParams(queryParameters)
                .when()
                .get("api/v1/internal/otp")
                .then()
                .assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath getOtpResponseJson = UtilityFunctions.rawToJson(getOtpResponse);
        GlobalVariables.otp = getOtpResponseJson.getString("data");

        //Storing current time :
        GlobalVariables.currentTime = UtilityFunctions.getCurrentTime();

    }

    //create New Account API:
    @Test(priority = 6)
    public static void createNewAccount() throws Exception {

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
                    .assertThat().statusCode(201)
                    .extract().response().asString();

        GlobalVariables.panNumber= UtilityFunctions.buildPan();
        GlobalVariables.gstNumber = UtilityFunctions.buildGst(GlobalVariables.panNumber);

        JsonPath createNewAccountResponseJson = UtilityFunctions.rawToJson(createNewAccountResponse);

        GlobalVariables.buyerAccountId = createNewAccountResponseJson.getString("data.minAccountDto.accountId");
        GlobalVariables.clientAuthToken = createNewAccountResponseJson.getString("data.token");

    }

    //create Organisation API :
    @Test(priority = 7)
    public static void createNewOrganisation() throws Exception {

        //setting organisation name using FAKER :
        Faker faker=new Faker();
        GlobalVariables.organisationName = faker.company().name()+ " Testing Company";

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GetConfigProperties.getSuperAdminToken());
        requestHeaders.put("X-OFB-PLATFORM","WEB_SITE");
        requestHeaders.put("X-REFERRER-DOMAIN","BUYER");

        String createOrganisationResponse = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.createOrganisationPayload(GlobalVariables.randomMobile,faker.name().fullName(),GlobalVariables.organisationName))
                .when()
                    .post("api/v2/lead")
                .then()
                    .assertThat().statusCode(201)
                    .extract().response().asString();

        JsonPath createOrganisationResponseJson = UtilityFunctions.rawToJson(createOrganisationResponse);
        GlobalVariables.clientOrganisationId = createOrganisationResponseJson.getString("data.organisationId");
        System.out.println("OrganisationId : "+GlobalVariables.clientOrganisationId);
        System.out.println("OrganisationName :"+GlobalVariables.organisationName);

        //writing response to file along with current method name :
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        UtilityFunctions.writeToFile(createOrganisationResponse,methodName);

    }

    //Adding address :
    @Test(priority = 8)
    public static void addAddress() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);
        requestHeaders.put("X-OFB-PLATFORM","ADMIN");

        RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                .headers(requestHeaders)
                    .body(Payload.saveAddressPayload(GlobalVariables.clientOrganisationId, UtilityFunctions.getRandomName(), GlobalVariables.randomMobile))
                .when()
                    .put(String.format("api/v1/org/address/%s",GlobalVariables.buyerAccountId))
                .then()
                    .assertThat().statusCode(200);

    }

    //Adding GST :
    @Test(priority = 9)
    public static void addGst() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);
        requestHeaders.put("X-OFB-PLATFORM","ADMIN");

        RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                .headers(requestHeaders)
                    .body(Payload.saveGstPayload(GlobalVariables.gstNumber))
                .when()
                    .post(String.format("api/v1/org/tax/%s/taxes/HR",GlobalVariables.clientOrganisationId))
                .then()
                    .assertThat().statusCode(200);

    }

    //get stateLevelInfo API :
    @Test(priority = 10)
    public static void stateLevelInfo() throws  Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);

        String stateLevelInfoResponse= RestAssured.given()
                .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                .when()
                    .get(String.format("api/v1/org/organisation/stateLevelInfo/%s",GlobalVariables.clientOrganisationId))
                .then()
                    .assertThat().statusCode(200)
                    .extract().response().asString();

        JsonPath stateLevelInfoResponseJson = UtilityFunctions.rawToJson(stateLevelInfoResponse);
        GlobalVariables.stateTaxInfoId= stateLevelInfoResponseJson.getString("data.HR.stateTaxInfoId");
    }

    //add addressWithGst API:
    @Test(priority = 11)
    public static void addAddressWithGst() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);
        requestHeaders.put("X-OFB-PLATFORM","WEB_SITE");
        requestHeaders.put("X-REFERRER-DOMAIN","ADMIN");

        String addAddressWithGstResponse = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.addAddressWithGstPayload(GlobalVariables.gstNumber,GlobalVariables.clientOrganisationId))
                .when()
                    .post(String.format("api/v2/org/address/%s",GetConfigProperties.getAddressId()))
                .then()
                    .assertThat().statusCode(200)
                    .extract().response().asString();

        JsonPath addAddressWithGstResponseJson = UtilityFunctions.rawToJson(addAddressWithGstResponse);
        GlobalVariables.clientAddressId = addAddressWithGstResponseJson.getString("data.addressId");

    }

    @Test(priority = 12)
    public static void tempUpload1() throws Exception{
        ReusableAPI.tempUplaod();
    }

    //verifyAddress API :
    @Test(priority = 13)
    public static void verifyAddress() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);
        requestHeaders.put("X-OFB-PLATFORM","WEB_SITE");
        requestHeaders.put("X-REFERRER-DOMAIN","ADMIN");

        RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.verifyAddressPayload(GlobalVariables.fingerPrint,GlobalVariables.tempFileLocation))
                .when()
                    .post(String.format("api/v2/org/address/%s/%s/verify",GlobalVariables.clientOrganisationId,GlobalVariables.clientAddressId))
                .then()
                    .assertThat().statusCode(200);

    }


    @Test(priority = 14)
    public static void tempUpload2() throws Exception{
        ReusableAPI.tempUplaod();
    }

    //verifyGST API :
    @Test(priority = 15)
    public static void verifyGst() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);

        RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.verifyGstPayload(GlobalVariables.fingerPrint,GlobalVariables.tempFileLocation))
                .when()
                    .put(String.format("api/v1/org/organisation/%s/GST/%s/verify",GlobalVariables.clientOrganisationId,GlobalVariables.stateTaxInfoId))
                .then()
                    .assertThat().statusCode(200);

    }

    @Test(priority = 16)
    public static void tempUpload3() throws Exception{
        ReusableAPI.tempUplaod();
    }

    //verifyPAN API :
    @Test(priority = 17)
    public static void verifyPan() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);

        RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.verifyPanPayload(GlobalVariables.fingerPrint,GlobalVariables.tempFileLocation,GlobalVariables.panNumber))
                .when()
                    .put(String.format("api/v1/org/organisation/%s/verifyPan",GlobalVariables.clientOrganisationId))
                .then()
                    .assertThat().statusCode(200);

    }

    //verifyBranchRegion API :
    @Test(priority = 18)
    public static void verifyBranchRegion() throws Exception {

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);

        RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                .headers(requestHeaders)
                    .body(Payload.verifyBranchRegionPayload())
                .when()
                    .put(String.format("api/v1/org/organisation/%s/BRANCH_REGION/verify",GlobalVariables.clientOrganisationId))
                .then()
                    .assertThat().statusCode(200)
                    .extract().response().asString();

    }

    //updating SalesAgent :
    @Test(priority = 19)
    public static void updateSalesAgent(){

        Map<String ,String > requestHeaders= new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);
        requestHeaders.put("X-OFB-PLATFORM", "WEB_SITE");
        requestHeaders.put("X-REFERRER-DOMAIN", "ADMIN");

        RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.updateSalesAgentPayload(GlobalVariables.clientOrganisationId,GetConfigProperties.getAccountId()))
                .when()
                    .put(String.format("api/v2/lead/qualification/moreDetails/%s",GlobalVariables.clientOrganisationId))
                .then()
                    .assertThat().statusCode(200);
    }

}
