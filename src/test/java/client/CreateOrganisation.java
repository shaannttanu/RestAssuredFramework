package client;

import com.github.javafaker.Faker;
import files.OrganisationPayloads;
import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class CreateOrganisation {

    public static String organisationName;
    public static String clientOrganisationId;
    @Test
    public static void createNewOrganisation(){

        RestAssured.baseURI=Base.getBaseUri();

        //setting organisation name using FAKER :
        Faker faker=new Faker();
        organisationName = faker.company().name()+ " Testing Company";

        String createOrganisationResponse=given().header("Content-Type","application/json")
                .header("X-OFB-TOKEN",Base.getSuperAdminToken())
                .header("X-OFB-PLATFORM","WEB_SITE")
                .header("X-REFERRER-DOMAIN","BUYER")
                .body(OrganisationPayloads.createOrganisationPayload(CreateAccount.randomMobile,faker.name().fullName(),organisationName))
                .when().post("api/v2/lead")
                .then().assertThat().statusCode(201)
                .extract().response().asString();

        JsonPath createOrganisationResponseJson = ReusableMethods.rawToJson(createOrganisationResponse);
        clientOrganisationId = createOrganisationResponseJson.getString("data.organisationId");
        System.out.println("OrganisationId : "+clientOrganisationId);
        System.out.println("OrganisationName : "+organisationName);
    }
}
