package files;
import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;

import java.lang.*;


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
}
