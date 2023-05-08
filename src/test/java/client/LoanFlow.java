package client;

import files.GlobalVariables;
import files.LoanPayloads;
import files.UtilityFunctions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class LoanFlow {
    @Test
    public static void CreateLoanApplication(){

        String response = RestAssured
                .given()
                .baseUri(GetConfigProperties.getOxyzoAPI())
                .header("X-OFB-TOKEN", GlobalVariables.adminAuthToken)
                .header("Content-Type","application/json")
                .body(LoanPayloads.getCreateLoanApplicationPayload(GlobalVariables.contactName,GlobalVariables.contactPersonEmail,GetConfigProperties.getRandomMobile(),GlobalVariables.clientOrganisationId,GlobalVariables.organisationName))
                .when()
                .post("api/v1/oxyzo/lead")
                .then()
                .assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath responseJson = UtilityFunctions.rawToJson(response);
        GlobalVariables.loanAmount= responseJson.getString("data.loanAmount");
        GlobalVariables.clientAccountId = responseJson.getString("data.loanLeadId");
        GlobalVariables.clientAppId = responseJson.getString("data.loanApplicationId");

        System.out.println("LoanApplicatioId : "+GlobalVariables.clientAppId);

    }
}
