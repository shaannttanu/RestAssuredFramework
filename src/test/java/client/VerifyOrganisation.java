package client;

import files.OrganisationPayloads;
import files.ReusableMethods;
import files.UtilityFunctions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.*;

public class VerifyOrganisation {

    public static String stateTaxInfoId;
    public static String clientAddressId;

    //Adding address :
    @Test
    public static void addAddress(){

        RestAssured.baseURI = GetConfigProperties.getStgAPI();

        given().header("X-OFB-PLATFORM","ADMIN")
                .header("X-OFB-TOKEN",GenerateAuthToken.adminAuthToken)
                .header("Content-Type","application/json")
                .body(OrganisationPayloads.saveAddressPayload(CreateOrganisation.clientOrganisationId, UtilityFunctions.getRandomName(),CreateAccount.randomMobile))
                .when().put(String.format("api/v1/org/address/%s",CreateAccount.buyerAccountId))
                .then().assertThat().statusCode(200);

    }

    //Adding GST :
    @Test
    public static void addGst(){

        RestAssured.baseURI = GetConfigProperties.getStgAPI();

        given().header("X-OFB-PLATFORM","ADMIN")
                .header("X-OFB-TOKEN",GenerateAuthToken.adminAuthToken)
                .header("Content-Type","application/json")
                .body(OrganisationPayloads.saveGstPayload(CreateAccount.gstNumber))
                .when().post(String.format("api/v1/org/tax/%s/taxes/HR",CreateOrganisation.clientOrganisationId))
                .then().assertThat().statusCode(200);

    }

    //get stateLevelInfo API :
    @Test
    public static void stateLevelInfo(){

        RestAssured.baseURI = GetConfigProperties.getStgAPI();

        String stateLevelInfoResponse=given().header("X-OFB-TOKEN",GenerateAuthToken.adminAuthToken)
                .header("Content-Type","application/json")
                .when().get(String.format("api/v1/org/organisation/stateLevelInfo/%s",CreateOrganisation.clientOrganisationId))
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath stateLevelInfoResponseJson = ReusableMethods.rawToJson(stateLevelInfoResponse);
        stateTaxInfoId= stateLevelInfoResponseJson.getString("data.HR.stateTaxInfoId");
    }

    //add addressWithGst API:
    @Test
    public static void addAddressWithGst(){

        RestAssured.baseURI = GetConfigProperties.getStgAPI();
        String addAddressWithGstResponse = given()
                .header("X-REFERRER-DOMAIN","ADMIN")
                .header("X-OFB-TOKEN",GenerateAuthToken.adminAuthToken)
                .header("Content-type","application/json")
                .header("X-OFB-PLATFORM","WEB_SITE")
                .body(OrganisationPayloads.addAddressWithGstPayload(CreateAccount.gstNumber,CreateOrganisation.clientOrganisationId))
                .when().post(String.format("api/v2/org/address/%s",GetConfigProperties.getAddressId()))
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath addAddressWithGstResponseJson = ReusableMethods.rawToJson(addAddressWithGstResponse);
        clientAddressId = addAddressWithGstResponseJson.getString("data.addressId");

    }

    //verifyAddress API :
    public static void verifyAddress(){

        RestAssured.baseURI = GetConfigProperties.getStgAPI();

        given()
                .header("X-REFERRER-DOMAIN","ADMIN")
                .header("X-OFB-TOKEN",GenerateAuthToken.adminAuthToken)
                .header("Content-type","application/json")
                .header("X-OFB-PLATFORM","WEB_SITE")
                .body(OrganisationPayloads.verifyAddressPayload(FileUpload.fingerPrint,FileUpload.tempFileLocation))
                .when().post(String.format("api/v2/org/address/%s/%s/verify",CreateOrganisation.clientOrganisationId,clientAddressId))
                .then().assertThat().statusCode(200);

    }

    //verifyGST API :
    public static void verifyGst(){

        RestAssured.baseURI = GetConfigProperties.getStgAPI();

        given()
                .header("X-OFB-TOKEN",GenerateAuthToken.adminAuthToken)
                .header("Content-type","application/json")
                .body(OrganisationPayloads.verifyGstPayload(FileUpload.fingerPrint,FileUpload.tempFileLocation))
                .when().put(String.format("api/v1/org/organisation/%s/GST/%s/verify",CreateOrganisation.clientOrganisationId,stateTaxInfoId))
                .then().assertThat().statusCode(200);

    }

    //verifyPAN API :
    public static void verifyPan(){

        RestAssured.baseURI = GetConfigProperties.getStgAPI();

        given()
                .header("X-OFB-TOKEN",GenerateAuthToken.adminAuthToken)
                .header("Content-type","application/json")
                .body(OrganisationPayloads.verifyPanPayload(FileUpload.fingerPrint,FileUpload.tempFileLocation,CreateAccount.panNumber))
                .when().put(String.format("api/v1/org/organisation/%s/verifyPan",CreateOrganisation.clientOrganisationId))
                .then().assertThat().statusCode(200);

    }

    public static void verifyBranchRegion(){

        RestAssured.baseURI = GetConfigProperties.getStgAPI();
        given()
                .header("X-OFB-TOKEN",GenerateAuthToken.adminAuthToken)
                .header("Content-type","application/json")
                .body(OrganisationPayloads.verifyBranchRegionPayload())
                .when().put(String.format("api/v1/org/organisation/%s/BRANCH_REGION/verify",CreateOrganisation.clientOrganisationId))
                .then().assertThat().statusCode(200);

        System.out.println("Organisation Verified successfully!");
    }
}
