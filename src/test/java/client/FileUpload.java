package client;

import files.GlobalVariables;
import files.UtilityFunctions;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;

public class FileUpload {


    @Test
    public static void tempUplaod(){

        File testFile  = new File("C:\\Users\\shant\\Desktop\\RestAssuredFramework\\src\\test\\java\\Resources\\download.jpg");

        String tempUploadResponse = RestAssured
                .given()
                .baseUri(GetConfigProperties.getStgfs())
                .header("X-OFB-PLATFORM","ADMIN")
                        .header("X-OFB-TOKEN",GlobalVariables.adminAuthToken)
                .multiPart("file",testFile)
                .when().post("api/v1/file/tempUpload")
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath tempUploadResponseJson = UtilityFunctions.rawToJson(tempUploadResponse);
        GlobalVariables.tempFileLocation = tempUploadResponseJson.getString("data.tempUrl");
        GlobalVariables.fingerPrint = tempUploadResponseJson.getString("data.fingerPrint");

    }
}
