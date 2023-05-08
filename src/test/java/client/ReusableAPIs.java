package client;

import files.GlobalVariables;
import files.UtilityFunctions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ReusableAPIs {

    @Test
    public static void tempUplaod(){

        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-OFB-PLATFORM","ADMIN");
        requestHeaders.put("X-OFB-TOKEN", GlobalVariables.adminAuthToken);

        File testFile  = new File("C:\\Users\\shant\\Desktop\\RestAssuredFramework\\src\\test\\java\\Resources\\download.jpg");

        String tempUploadResponse = RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgfs())
                .headers(requestHeaders)
                .multiPart("file",testFile)
                .when()
                .post("api/v1/file/tempUpload")
                .then()
                .assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath tempUploadResponseJson = UtilityFunctions.rawToJson(tempUploadResponse);
        GlobalVariables.tempFileLocation = tempUploadResponseJson.getString("data.tempUrl");
        GlobalVariables.fingerPrint = tempUploadResponseJson.getString("data.fingerPrint");
    }

    @Test
    public static void tokenAuth(String token){

        RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgAPI())
                .queryParam("key", GetConfigProperties.getRediskey())
                .when()
                .post(String.format("api/v1/internal/testLogin/%s", token))
                .then()
                .assertThat().statusCode(200);
    }

}
