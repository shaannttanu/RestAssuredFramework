package files;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class UtilityFunctions {

    //Generate PAN number for new client :
    public static String buildPan(){

        String panNumbertext="";
        String possibleAlphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (var i = 0; i < 5; i++) {
            panNumbertext +=  Character.toString(possibleAlphabets.charAt((int) Math.floor(Math.random() * possibleAlphabets.length())));
        }
        panNumbertext+= Integer.toString(((int)(Math.random()*9000)+1000));
        for (var j = 0; j < 1; j++){
            panNumbertext += Character.toString(possibleAlphabets.charAt((int)Math.floor(Math.random() * possibleAlphabets.length())));
        }
        return panNumbertext;
    }

    //Generate GST for new client :
    public static String buildGst(String panNumber){

        String gstNumber = "06"+panNumber+"1ZC";
        return gstNumber;
    }

    public static String getRandomName(){
        Faker faker = new Faker();
        String randomName = faker.name().fullName();
        return randomName;
    }

    public static String getRandomEmail(){
        Faker faker=new Faker();
        String randomEmail = faker.name().username()+"@gmail.com";
        return randomEmail;
    }

    public static String getCurrentTime(){
        String currentTime = Long.toString(System.currentTimeMillis());
        return currentTime;
    }

    //calculate total runtime of the flow based on start and end time :
    public static long getRuntime(String startTime,String endTime) {
        long start = Long.parseLong(startTime);
        long end = Long.parseLong(endTime);
        long runtime = end-start;
        return runtime;
    }
    public static JsonPath rawToJson(String response){
        JsonPath js=new JsonPath(response);
        return js;
    }

    //writing response to a .txt file :
    public static void writeToFile(String content , String methodName) throws IOException {

            FileWriter fileWriter = new FileWriter("C:\\Users\\shant\\Desktop\\RestAssuredFramework\\src\\test\\java\\Resources\\response.txt", true);
            fileWriter.write(methodName+" :\n"+content+"\n");
            fileWriter.close();
    }

    //erase contents of reponse.txt file before each run :
    public static void eraseFileContents() throws IOException {
            FileWriter fileWriter = new FileWriter("C:\\Users\\shant\\Desktop\\RestAssuredFramework\\src\\test\\java\\Resources\\response.txt", false);
            fileWriter.write("");
            fileWriter.close();
    }

    public static void validateStatusCode (ExtentTest test , int actualStatusCode , int expectedStatusCode){

        if(expectedStatusCode == actualStatusCode){
            test.log(Status.PASS,"Status code is : "+actualStatusCode);
        }else{
            test.log(Status.FAIL,"Status code is : "+expectedStatusCode);
        }
    }
}
