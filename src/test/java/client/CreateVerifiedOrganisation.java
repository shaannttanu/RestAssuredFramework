package client;

import Globals.AllGlobals;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.github.javafaker.Faker;
import files.GlobalVariables;
import files.Payload;
import files.UtilityFunctions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

public class CreateVerifiedOrganisation {

    ExtentReports extent;
    ExtentTest test;
    ExtentSparkReporter sparkReporter;

    //setting up html-reporter :
    @BeforeSuite
    public void setupHtmlReport(){

        extent = new ExtentReports();
        sparkReporter = new ExtentSparkReporter("C:\\Users\\shant\\Desktop\\RestAssuredFramework\\src\\test\\java\\Resources\\report.html");
        extent.attachReporter(sparkReporter);

    }

    //create automation and approver accounts :
    @Test(dataProvider = "accountType" , priority = 1)
    public void createAutomationAndApproverAccount(String name, String mobile) throws Exception {

        test = extent.createTest("Create Automation and approver account ","API to create new automation and approver account");

        Response response = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .basePath("/api/v1/automationBot/account")
                    .contentType("application/json")
                    .body(Payload.automationApproverAccountPayload(name, mobile))
                .when()
                    .put();

        test.log(Status.INFO,"Response : "+ response.asString());
        UtilityFunctions.validateStatusCode(test, response.getStatusCode(), 200);

        JsonPath jsPath = new JsonPath(response.asString());

        if (name.equals("Automation Bot")) {
            GlobalVariables.automationId = jsPath.getString("data.accountId");

        } else {

            GlobalVariables.approverId = jsPath.getString("data.accountId");
        }

        response.then().assertThat().statusCode(200);
    }
    @DataProvider(name = "accountType")
    public Object[][] getData() {
        return new Object[][]{{"Automation Bot", "1000000000"}, {"Approver Bot", "1000000001"}};
    }


    //Authenticating superAdmin token :
    @Test(priority = 2)
    public void tokenAuth1() throws Exception{
        ReusableAPI.tokenAuth(GetConfigProperties.getSuperAdminToken(),test,extent);
    }

    //generating adminauthtoken and approver adminauthtoken :
    @Test(dataProvider = "authToken" , priority = 3)
    public void adminAndApproverAdminAuthToken(String mobile) throws Exception {

        test= extent.createTest("GetAdminAuthToken and ApproverAuthToken","API to genearate new adminAuthToken and ApproverAdminAuthToken ");
        Response response = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .basePath("/api/v1/oxyzo/applicant/account/login")
                    .contentType("application/json")
                    .body(Payload.adminAndApproverAdminAuthTokenPayload(mobile))
                .when()
                    .post();

        test.log(Status.INFO,"Response : "+response.asString());
        UtilityFunctions.validateStatusCode(test, response.getStatusCode(), 200);

        JsonPath jsPath = new JsonPath(response.asString());

        if (mobile.equals("1000000000")) {
            GlobalVariables.adminAuthToken = jsPath.getString("data.token");

        } else if(mobile.equals("1000000001")) {

            GlobalVariables.approverAdminAuthToken = jsPath.getString("data.token");
        }

        response.then().assertThat().statusCode(200);
    }
    @DataProvider(name = "authToken")
    public Object[] getMobile() {
        return new Object[]{"1000000000","1000000001"};
    }

    //Login part(send otp & getOtp)
    @Test(priority = 4)
    public void sendLoginOtp() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");

        test = extent.createTest("Send Login Otp","API to send OTP to login");
        Response response = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.sendLoginOtpPayload(GetConfigProperties.getRandomMobile()))
                .when()
                    .post("api/v1/account/loginOtp");

        test.log(Status.INFO,"Response : "+response.asString());
        UtilityFunctions.validateStatusCode(test,response.getStatusCode(),200);

        response.then().assertThat().statusCode(200);
    }

    //Get OTP :
    @Test(priority = 5)
    public void getOtp() throws Exception {

        Map<String,String> queryParameters = new HashMap<>();
        queryParameters.put("mobile",GetConfigProperties.getRandomMobile());
        queryParameters.put("key",GetConfigProperties.getRediskey());

        test=extent.createTest("Get OTP","API to fetch OTP");

        Response response = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .queryParams(queryParameters)
                .when()
                    .get("api/v1/internal/otp");

        test.log(Status.INFO,"Response : "+response.asString());
        UtilityFunctions.validateStatusCode(test,response.getStatusCode(),200);

        JsonPath getOtpResponseJson = UtilityFunctions.rawToJson(response.asString());
        GlobalVariables.otp = getOtpResponseJson.getString("data");

        //Storing current time :
        GlobalVariables.currentTime = UtilityFunctions.getCurrentTime();

        response.then().assertThat().statusCode(200);

    }

    //create New Account :
    @Test(priority = 6)
    public void createNewAccount() throws Exception {

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");

        test = extent.createTest("Create New Account ");

        Response response = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.accountCreatePayload(GetConfigProperties.getRandomMobile(),GlobalVariables.otp))
                .when()
                    .post("api/v1/account/create");

        GlobalVariables.panNumber = UtilityFunctions.buildPan();
        GlobalVariables.gstNumber = UtilityFunctions.buildGst(GlobalVariables.panNumber);

        test.log(Status.INFO , "Response : "+response.asString());
        JsonPath createNewAccountResponseJson = UtilityFunctions.rawToJson(response.asString());
        UtilityFunctions.validateStatusCode(test,response.getStatusCode(),201);

        GlobalVariables.buyerAccountId = createNewAccountResponseJson.getString("data.minAccountDto.accountId");
        GlobalVariables.clientAccountId = createNewAccountResponseJson.getString("data.token");

        response.then().assertThat().statusCode(201);
    }

    //create Organisation API :
    @Test(priority = 7)
    public void createNewOrganisation() throws Exception {

        //setting organisation name using FAKER :
        Faker faker=new Faker();
        String organisationName = faker.company().name()+ " Testing Company";
        GlobalVariables.organisationName = organisationName;

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GetConfigProperties.getSuperAdminToken());
        requestHeaders.put("X-OFB-PLATFORM","WEB_SITE");
        requestHeaders.put("X-REFERRER-DOMAIN","BUYER");

        test = extent.createTest("Create Organisation","API to create new organisation");

        Response response  = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.createOrganisationPayload(GetConfigProperties.getRandomMobile(),faker.name().fullName(),GlobalVariables.organisationName))
                .when()
                    .post("api/v2/lead");


        test.log(Status.INFO,"response "+response.asString());
        UtilityFunctions.validateStatusCode(test,response.getStatusCode(),201);

        JsonPath createOrganisationResponseJson = UtilityFunctions.rawToJson(response.asString());

        GlobalVariables.clientOrganisationId = createOrganisationResponseJson.getString("data.organisationId");
        System.out.println("OrganisationId : "+GlobalVariables.clientOrganisationId);

        System.out.println("OrganisationName :"+GlobalVariables.organisationName);

        response.then().assertThat().statusCode(201);
    }


    //Adding address :
    @Test(priority = 8)
    public void addAddress() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);
        requestHeaders.put("X-OFB-PLATFORM","ADMIN");

        test =  extent.createTest("Add Address","API to add address to organisation");

        Response response = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                .headers(requestHeaders)
                    .body(Payload.saveAddressPayload(GlobalVariables.clientOrganisationId, UtilityFunctions.getRandomName(), GetConfigProperties.getRandomMobile()))
                .when()
                    .put(String.format("api/v1/org/address/%s",GlobalVariables.buyerAccountId));

        test.log(Status.INFO,"Response : "+response.asString());
        response.then().assertThat().statusCode(200);
    }

    //Adding GST :
    @Test(priority = 9)
    public  void addGst() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);
        requestHeaders.put("X-OFB-PLATFORM","ADMIN");

        test = extent.createTest("Add GST","API to add gst in organisation");
        Response response =RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                .headers(requestHeaders)
                    .body(Payload.saveGstPayload(GlobalVariables.gstNumber))
                .when()
                    .post(String.format("api/v1/org/tax/%s/taxes/HR",GlobalVariables.clientOrganisationId));

        test.log(Status.INFO,"Response : "+response.asString());
        UtilityFunctions.validateStatusCode(test,response.getStatusCode(),200);

        response.then().assertThat().statusCode(200);

    }

    //get stateLevelInfo API :
    @Test(priority = 10)
    public void stateLevelInfo() throws  Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);

        test = extent.createTest("State level info ","API to fetch stateTaxInfoId");
        Response response= RestAssured.given()
                .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                .when()
                    .get(String.format("api/v1/org/organisation/stateLevelInfo/%s",GlobalVariables.clientOrganisationId));

        test.log(Status.INFO,"Response : "+response.asString());
        UtilityFunctions.validateStatusCode(test,response.getStatusCode(),200);

        JsonPath stateLevelInfoResponseJson = UtilityFunctions.rawToJson(response.asString());
        GlobalVariables.stateTaxInfoId = stateLevelInfoResponseJson.getString("data.HR.stateTaxInfoId");

    }

    //add addressWithGst API:
    @Test(priority = 11)
    public void addAddressWithGst() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);
        requestHeaders.put("X-OFB-PLATFORM","WEB_SITE");
        requestHeaders.put("X-REFERRER-DOMAIN","ADMIN");

        test = extent.createTest("Add Address with GST","API to link address with GST");
        Response response = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.addAddressWithGstPayload(GlobalVariables.gstNumber,GlobalVariables.clientOrganisationId))
                .when()
                    .post(String.format("api/v2/org/address/%s",GetConfigProperties.getAddressId()));

        test.log(Status.INFO, "Response : "+response.asString());
        UtilityFunctions.validateStatusCode(test,response.getStatusCode(),200);

        JsonPath addAddressWithGstResponseJson = UtilityFunctions.rawToJson(response.asString());

        GlobalVariables.clientAddressId = addAddressWithGstResponseJson.getString("data.addressId");
        response.then().assertThat().statusCode(200);
    }

    @Test(priority = 12)
    public void tempUpload1() throws Exception{
        ReusableAPI.tempUplaod(test,extent);
    }

    //verifyAddress API :
    @Test(priority = 13)
    public void verifyAddress() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);
        requestHeaders.put("X-OFB-PLATFORM","WEB_SITE");
        requestHeaders.put("X-REFERRER-DOMAIN","ADMIN");

        test = extent.createTest("Verify Address","API to verify address of organisation");

        Response resposne = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.verifyAddressPayload(GlobalVariables.fingerPrint,GlobalVariables.tempFileLocation))
                .when()
                    .post(String.format("api/v2/org/address/%s/%s/verify",GlobalVariables.clientOrganisationId,GlobalVariables.clientAddressId));

        test.log(Status.INFO,"Response : "+resposne.asString());
        UtilityFunctions.validateStatusCode(test,resposne.getStatusCode(),200);

        resposne.then().assertThat().statusCode(200);

    }


    @Test(priority = 14)
    public void tempUpload2() throws Exception{
        ReusableAPI.tempUplaod(test,extent);
    }

    //verifyGST API :
    @Test(priority = 15)
    public void verifyGst() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);

        test = extent.createTest("Verify GST","API to verify GST");
        Response response =RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.verifyGstPayload(GlobalVariables.fingerPrint,GlobalVariables.tempFileLocation))
                .when()
                    .put(String.format("api/v1/org/organisation/%s/GST/%s/verify",GlobalVariables.clientOrganisationId,GlobalVariables.stateTaxInfoId));

        test.log(Status.INFO,"Response : "+response.asString());
        UtilityFunctions.validateStatusCode(test,response.getStatusCode(),200);

        response.then().assertThat().statusCode(200);

    }

    @Test(priority = 16)
    public void tempUpload3() throws Exception{
        ReusableAPI.tempUplaod(test,extent);
    }

    //verifyPAN API :
    @Test(priority = 17)
    public void verifyPan() throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);

        test = extent.createTest("Verify PAN ","API to verify PAN associated with organisation");

        Response respone = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.verifyPanPayload(GlobalVariables.fingerPrint,GlobalVariables.tempFileLocation,GlobalVariables.panNumber))
                .when()
                    .put(String.format("api/v1/org/organisation/%s/verifyPan",GlobalVariables.clientOrganisationId));

        test.log(Status.INFO,"Response : "+respone.asString());
        UtilityFunctions.validateStatusCode(test,respone.getStatusCode(),200);

        respone.then().assertThat().statusCode(200);

    }

    //verifyBranchRegion API :
    @Test(priority = 18)
    public void verifyBranchRegion() throws Exception {

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);

        test = extent.createTest("Verify Branch Region","API to verify Branch Region");

        Response response =RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                .headers(requestHeaders)
                    .body(Payload.verifyBranchRegionPayload())
                .when()
                    .put(String.format("api/v1/org/organisation/%s/BRANCH_REGION/verify",GlobalVariables.clientOrganisationId));

        test.log(Status.INFO,"Response : "+response.asString());
        UtilityFunctions.validateStatusCode(test,response.getStatusCode(),200);

        response.then().assertThat().statusCode(200);
    }

    //updating SalesAgent :
    @Test(priority = 19)
    public void updateSalesAgent() throws Exception{

        Map<String ,String > requestHeaders= new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN",GlobalVariables.adminAuthToken);
        requestHeaders.put("X-OFB-PLATFORM", "WEB_SITE");
        requestHeaders.put("X-REFERRER-DOMAIN", "ADMIN");

        test = extent.createTest("Update Sales Agent","API to update sales agent");

        Response response= RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .headers(requestHeaders)
                    .body(Payload.updateSalesAgentPayload(GlobalVariables.clientOrganisationId,GetConfigProperties.getAccountId()))
                .when()
                    .put(String.format("api/v2/lead/qualification/moreDetails/%s",GlobalVariables.clientOrganisationId));

        test.log(Status.INFO,"Response : "+response.asString());
        UtilityFunctions.validateStatusCode(test,response.getStatusCode(),200);

        response.then().assertThat().statusCode(200);
    }

    @AfterSuite
    public void tearDown() throws Exception{
        extent.flush();
    }

}
