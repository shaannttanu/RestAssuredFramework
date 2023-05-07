package client;

import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;

public class FileUpload {

    public static String tempFileLocation;
    public static String fingerPrint;
    @Test
    public static void tempUplaod(){
        RestAssured.baseURI=GetConfigProperties.getStgfs();

        File testFile  = new File("C:\\Users\\shant\\Desktop\\RestAssuredFramework\\src\\test\\java\\Resources\\download.jpg");

        String tempUploadResponse = given()
                .header("X-OFB-PLATFORM","ADMIN")
                        .header("X-OFB-TOKEN",GenerateAuthToken.adminAuthToken)
                .multiPart("file",testFile)
                .when().post("api/v1/file/tempUpload")
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath tempUploadResponseJson = ReusableMethods.rawToJson(tempUploadResponse);
        tempFileLocation = tempUploadResponseJson.getString("data.tempUrl");
        fingerPrint = tempUploadResponseJson.getString("data.fingerPrint");

    }
}
