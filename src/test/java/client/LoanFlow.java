package client;

import files.GlobalVariables;
import files.Payload;
import files.UtilityFunctions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class LoanFlow {
    @Test
    public static void CreateLoanApplication(){

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);

        String response = RestAssured
                .given()
                .baseUri(GetConfigProperties.getOxyzoAPI())
                .headers(requestHeaders)
                .body(Payload.getCreateLoaAnApplicationPayload(GlobalVariables.contactName,GlobalVariables.contactPersonEmail,GetConfigProperties.getRandomMobile(),GlobalVariables.clientOrganisationId,GlobalVariables.organisationName))
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
