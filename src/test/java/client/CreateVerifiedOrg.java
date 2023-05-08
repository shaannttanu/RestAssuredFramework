package client;

import com.github.javafaker.Faker;
import files.GlobalVariables;
import files.OrganisationPayloads;
import files.UtilityFunctions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class CreateVerifiedOrg {

    @Test
    public static void createNewOrganisation(){

        //setting organisation name using FAKER :
        Faker faker=new Faker();
        GlobalVariables.organisationName = faker.company().name()+ " Testing Company";

        String createOrganisationResponse = RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .header("Content-Type","application/json")
                .header("X-OFB-TOKEN", GetConfigProperties.getSuperAdminToken())
                .header("X-OFB-PLATFORM","WEB_SITE")
                .header("X-REFERRER-DOMAIN","BUYER")
                .body(OrganisationPayloads.createOrganisationPayload(GlobalVariables.randomMobile,faker.name().fullName(),GlobalVariables.organisationName))
                .when().post("api/v2/lead")
                .then().assertThat().statusCode(201)
                .extract().response().asString();

        JsonPath createOrganisationResponseJson = UtilityFunctions.rawToJson(createOrganisationResponse);
        GlobalVariables.clientOrganisationId = createOrganisationResponseJson.getString("data.organisationId");
        System.out.println("OrganisationId : "+GlobalVariables.clientOrganisationId);
    }
    //Adding address :
    @Test
    public static void addAddress(){

        RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .header("X-OFB-PLATFORM","ADMIN")
                .header("X-OFB-TOKEN",GlobalVariables.adminAuthToken)
                .header("Content-Type","application/json")
                .body(OrganisationPayloads.saveAddressPayload(GlobalVariables.clientOrganisationId, UtilityFunctions.getRandomName(), GlobalVariables.randomMobile))
                .when().put(String.format("api/v1/org/address/%s",GlobalVariables.buyerAccountId))
                .then().assertThat().statusCode(200);

    }
    //Adding GST :
    @Test
    public static void addGst(){

        RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .header("X-OFB-PLATFORM","ADMIN")
                .header("X-OFB-TOKEN",GlobalVariables.adminAuthToken)
                .header("Content-Type","application/json")
                .body(OrganisationPayloads.saveGstPayload(GlobalVariables.gstNumber))
                .when().post(String.format("api/v1/org/tax/%s/taxes/HR",GlobalVariables.clientOrganisationId))
                .then().assertThat().statusCode(200);

    }

    //get stateLevelInfo API :
    @Test
    public static void stateLevelInfo(){

        String stateLevelInfoResponse= RestAssured.given()
                .baseUri(GetConfigProperties.getStgAPI())
                .header("X-OFB-TOKEN",GlobalVariables.adminAuthToken)
                .header("Content-Type","application/json")
                .when().get(String.format("api/v1/org/organisation/stateLevelInfo/%s",GlobalVariables.clientOrganisationId))
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath stateLevelInfoResponseJson = UtilityFunctions.rawToJson(stateLevelInfoResponse);
        GlobalVariables.stateTaxInfoId= stateLevelInfoResponseJson.getString("data.HR.stateTaxInfoId");
    }

    //add addressWithGst API:
    @Test
    public static void addAddressWithGst(){

        String addAddressWithGstResponse = RestAssured.given()
                .baseUri(GetConfigProperties.getStgAPI())
                .header("X-REFERRER-DOMAIN","ADMIN")
                .header("X-OFB-TOKEN",GlobalVariables.adminAuthToken)
                .header("Content-type","application/json")
                .header("X-OFB-PLATFORM","WEB_SITE")
                .body(OrganisationPayloads.addAddressWithGstPayload(GlobalVariables.gstNumber,GlobalVariables.clientOrganisationId))
                .when().post(String.format("api/v2/org/address/%s",GetConfigProperties.getAddressId()))
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath addAddressWithGstResponseJson = UtilityFunctions.rawToJson(addAddressWithGstResponse);
        GlobalVariables.clientAddressId = addAddressWithGstResponseJson.getString("data.addressId");

    }

    //verifyAddress API :
    public static void verifyAddress(){

        RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .header("X-REFERRER-DOMAIN","ADMIN")
                .header("X-OFB-TOKEN",GlobalVariables.adminAuthToken)
                .header("Content-type","application/json")
                .header("X-OFB-PLATFORM","WEB_SITE")
                .body(OrganisationPayloads.verifyAddressPayload(GlobalVariables.fingerPrint,GlobalVariables.tempFileLocation))
                .when()
                .post(String.format("api/v2/org/address/%s/%s/verify",GlobalVariables.clientOrganisationId,GlobalVariables.clientAddressId))
                .then()
                .assertThat().statusCode(200);

    }

    //verifyGST API :
    public static void verifyGst(){

        RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .header("X-OFB-TOKEN",GlobalVariables.adminAuthToken)
                .header("Content-type","application/json")
                .body(OrganisationPayloads.verifyGstPayload(GlobalVariables.fingerPrint,GlobalVariables.tempFileLocation))
                .when().put(String.format("api/v1/org/organisation/%s/GST/%s/verify",GlobalVariables.clientOrganisationId,GlobalVariables.stateTaxInfoId))
                .then().assertThat().statusCode(200);

    }

    //verifyPAN API :
    public static void verifyPan(){

        RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .header("X-OFB-TOKEN",GlobalVariables.adminAuthToken)
                .header("Content-type","application/json")
                .body(OrganisationPayloads.verifyPanPayload(GlobalVariables.fingerPrint,GlobalVariables.tempFileLocation,GlobalVariables.panNumber))
                .when().put(String.format("api/v1/org/organisation/%s/verifyPan",GlobalVariables.clientOrganisationId))
                .then().assertThat().statusCode(200);

    }

    //verifyBranchRegion API :
    public static void verifyBranchRegion(){

        RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .header("X-OFB-TOKEN",GlobalVariables.adminAuthToken)
                .header("Content-type","application/json")
                .body(OrganisationPayloads.verifyBranchRegionPayload())
                .when().put(String.format("api/v1/org/organisation/%s/BRANCH_REGION/verify",GlobalVariables.clientOrganisationId))
                .then().assertThat().statusCode(200);

        System.out.println("Organisation Verified successfully!");
    }
}
