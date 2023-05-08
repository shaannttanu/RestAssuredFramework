package files;
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

    public static JsonPath rawToJson(String response){
        JsonPath js=new JsonPath(response);
        return js;
    }

    public static void writeToFile(String content , String methodName) throws IOException {

        String path = "C:\\Users\\shant\\Desktop\\RestAssuredFramework\\src\\test\\java\\Resources\\response.txt";

        try {
            Files.write(Paths.get(path),String.format("%s response : \n",methodName).getBytes(),StandardOpenOption.APPEND);
            Files.write(Paths.get(path), content.getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(path), "\n".getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public static void eraseFileContents() throws IOException {
        new PrintWriter("C:\\Users\\shant\\Desktop\\RestAssuredFramework\\src\\test\\java\\Resources\\response.txt").close();
    }
}
