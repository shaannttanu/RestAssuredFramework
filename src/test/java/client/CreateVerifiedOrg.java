package client;

import com.github.javafaker.Faker;
import files.GlobalVariables;
import files.Payload;
import files.UtilityFunctions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CreateVerifiedOrg {
    @Test
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

    }
    //Adding address :
    @Test
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
    @Test
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
    @Test
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
    @Test
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

    //verifyAddress API :
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

    //verifyGST API :
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

    //verifyPAN API :
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
}
