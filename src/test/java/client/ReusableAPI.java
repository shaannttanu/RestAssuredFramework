package client;

import Globals.AllGlobals;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import files.GlobalVariables;
import files.UtilityFunctions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ReusableAPI {

    @Test
    public static void tempUplaod(ExtentTest test, ExtentReports extent) throws Exception{

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-OFB-PLATFORM","ADMIN");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);

        test = extent.createTest("tempUpload","API to upload file ");

        File testFile  = new File(GetConfigProperties.getTempUplaodFilePath());
        Response response = RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgfs())
                    .headers(requestHeaders)
                    .multiPart("file",testFile)
                .when()
                    .post("api/v1/file/tempUpload");

        test.log(Status.INFO,"Response : "+ response.asString());
        UtilityFunctions.validateStatusCode(test,response.statusCode(),200);

        JsonPath tempUploadResponseJson = UtilityFunctions.rawToJson(response.asString());
        GlobalVariables.tempFileLocation = tempUploadResponseJson.getString("data.tempUrl");
        GlobalVariables.fingerPrint = tempUploadResponseJson.getString("data.fingerPrint");

        response.then().assertThat().statusCode(200);
    }

    @Test
    public static void tokenAuth(String token,ExtentTest test , ExtentReports extent) throws Exception{

        test = extent.createTest("Token Auth ","API to validate auth-token");
        Response response =RestAssured
                .given()
                    .baseUri(GetConfigProperties.getStgAPI())
                    .queryParam("key", GetConfigProperties.getRediskey())
                .when()
                    .post(String.format("api/v1/internal/testLogin/%s", token));

        test.log(Status.INFO, "Response : "+response.asString());
        UtilityFunctions.validateStatusCode(test, response.getStatusCode(), 200);

        response.then().assertThat().statusCode(200);
    }

}
